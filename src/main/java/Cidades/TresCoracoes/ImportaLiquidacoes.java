package Cidades.TresCoracoes;

import _Entity.*;
import _Infra.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class ImportaLiquidacoes extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {
        EntityManager emLocal = conexaoDestino("TresCoracoes");

        Connection con = conexaoOrigemSQLServer();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        String teste, historico;
        Integer seqLiquidacao, empenho, liquidacao, parcela, versaoRecurso, fichaReceita;
        BigDecimal valorLiquidacao, valorDesconto, valorReceita;
        Date anoAtual, dataLiquidacao, dataVencimento;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        versaoRecurso = getVersao(anoSonner);

        delete("CBPLIQUIDACOES", anoAtual);
        delete("CBPLIQUIDAPAGTO", anoAtual);
        delete("CBPPAGAMENTOS", anoAtual);
        delete("CBPDESCONTOSTEMP", anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and NRO_EMPENHO = 1";
        //teste = "and SEQ_CT_LIQUIDACAO = 493093";

        System.out.println("INICIANDO IMPORTAÇÃO LIQUIDAÇÃO: " + anoSonner);
        try {
            stmt = con.prepareStatement(
                    "SELECT" +
                            "    SEQ_CT_LIQUIDACAO, " +
                            "    NRO_EMPENHO, " +
                            "    NRO_LIQUIDACAO_EMPENHO, " +
                            "    DAT_LIQUIDACAO, " +
                            "    DAT_LIQUIDACAO_VENCIMENTO, " +
                            "    VLR_LIQUIDACAO, " +
                            "    HST_LIQUIDACAO " +
                            "FROM" +
                            "    vw_CT_LIQUIDACAO " +
                            "WHERE" +
                            "    ANO_LIQUIDACAO = ? " +
                            "AND ANO_EMPENHO = ANO_LIQUIDACAO " + teste +
                            "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                            "ORDER BY " +
                            "    NRO_EMPENHO, " +
                            "    NRO_LIQUIDACAO_EMPENHO");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                seqLiquidacao = rs.getInt(1);
                empenho = rs.getInt(2);
                liquidacao = rs.getInt(3);
                dataLiquidacao = rs.getDate(4);
                dataVencimento = rs.getDate(5);
                valorLiquidacao = rs.getBigDecimal(6);
                historico = rs.getString(7).trim().toUpperCase();

                System.out.println("Ano: " + anoSonner + " - Empenho: " + empenho + " Liquidacao: " + liquidacao);

                LiquidacaoEmpenho liquidacaoEmpenho = emLocal.find(LiquidacaoEmpenho.class, new LiquidacaoEmpenhoPK(anoAtual, empenho, liquidacao));
                if (Objects.isNull(liquidacaoEmpenho)) {
                    liquidacaoEmpenho = new LiquidacaoEmpenho(anoAtual, empenho, liquidacao, dataLiquidacao, valorLiquidacao, historico);
                    emLocal.persist(liquidacaoEmpenho);
                } else {
                    valorLiquidacao = valorLiquidacao.add(liquidacaoEmpenho.getValorLiquidacao());
                    liquidacaoEmpenho.setValorLiquidacao(valorLiquidacao);
                    emLocal.merge(liquidacaoEmpenho);
                }

                parcela = getMaxParcela(emLocal, anoAtual, empenho);

                LiquidaPagto liquidaPagto = new LiquidaPagto(anoAtual, empenho, liquidacao, parcela);
                emLocal.persist(liquidaPagto);

                // pegando total descontos
                stmt2 = con.prepareStatement(
                        "SELECT " +
                                "    SUM(VLR_LIQUIDACAO_DESCONTO) " +
                                "FROM " +
                                "    CT_LIQUIDACAO_DESCONTO " +
                                "WHERE " +
                                "    SEQ_CT_LIQUIDACAO = ? ");
                stmt2.setInt(1, seqLiquidacao);
                rs2 = stmt2.executeQuery();
                rs2.next();
                valorDesconto = rs2.getBigDecimal(1);
                stmt2.close();
                rs2.close();

                valorDesconto = valorDesconto == null ? BigDecimal.ZERO : valorDesconto;

                Pagamentos pagamentos = new Pagamentos(anoAtual, empenho, parcela, dataLiquidacao, dataVencimento, historico, valorLiquidacao, null, null, valorDesconto, seqLiquidacao);
                emLocal.persist(pagamentos);

                if (valorDesconto.signum() > 0) {
                    descontosTemp(emLocal, con, anoAtual, seqLiquidacao, empenho, parcela, versaoRecurso);
                }
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Ops");
            e.printStackTrace();
        } finally {
            closeConexao(con, stmt, rs);
        }
    }

    private static void descontosTemp(EntityManager emLocal, Connection con, Date anoAtual, Integer seqLiquidacao, Integer empenho, Integer parcela, Integer versaoRecurso) throws SQLException {
        ResultSet rs2;
        PreparedStatement stmt2;
        Integer fichaReceita;
        BigDecimal valorReceita;

        stmt2 = con.prepareStatement(
                "SELECT " +
                        "    SEQ_CT_DESCONTO, " +
                        "    VLR_LIQUIDACAO_DESCONTO " +
                        "FROM " +
                        "    CT_LIQUIDACAO_DESCONTO " +
                        "WHERE " +
                        "    SEQ_CT_LIQUIDACAO = ? " +
                        "ORDER BY 1 ");
        stmt2.setInt(1, seqLiquidacao);
        rs2 = stmt2.executeQuery();
        while (rs2.next()) {
            fichaReceita = rs2.getInt(1);
            valorReceita = rs2.getBigDecimal(2);

            DescontosTemp descontosTemp = new DescontosTemp(anoAtual, "E", empenho, parcela, "E", fichaReceita, versaoRecurso, 110, 999, 0, null, null, null, valorReceita);
            emLocal.persist(descontosTemp);
        }
        stmt2.close();
        rs2.close();
    }

    private static int getMaxParcela(EntityManager em, Date ano, Integer empenho) {
        Query q = em.createQuery("Select MAX(l.id.pagamento) from Pagamentos l where l.id.ano = :ano  and l.id.empenho = :empenho ")
                .setParameter("ano", ano)
                .setParameter("empenho", empenho);
        Integer max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }
}
