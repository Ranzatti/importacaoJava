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

public class ImportaPagamento extends Util {

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

        String teste, historicoLiquidacao, historicoPagamento;
        Integer seqPagamento, autorizacao, empenho, liquidacao, parcela, versaoRecurso;
        BigDecimal valorLiquidacao, valorParcela, valorPago, valorDesconto;
        Date anoAtual, dataAutorizacao, dataPagamento, dataLiquidacao, vencimento;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        versaoRecurso = getVersao(anoSonner);

        delete(anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and NRO_EMPENHO in(1) ";

        System.out.println("INICIANDO IMPORTAÇÃO PAGAMENTOS: " + anoSonner);
        try {
            stmt = con.prepareStatement(
                    "SELECT DISTINCT " +
                            "    SEQ_CT_PAGAMENTO, " +
                            "    NRO_PAGAMENTO_ORDEM, " +
                            "    DAT_PAGAMENTO_ORDEM, " +
                            "    DAT_PAGAMENTO, " +
                            "    NRO_EMPENHO, " +
                            "    NRO_LIQUIDACAO_EMPENHO, " +
                            "    DAT_LIQUIDACAO, " +
                            "    DAT_PAGAMENTO_VENCIMENTO, " +
                            "    VLR_LIQUIDACAO, " +
                            "    VLR_PAGAMENTO, " +
                            "    coalesce(VLR_PAGAMENTO_DESCONTO, 0) as VLR_PAGAMENTO_DESCONTO, " +
                            "    VLR_PAGAMENTO_LIQUIDO, " +
                            "    HST_LIQUIDACAO " +
                            "    HST_PAGAMENTO " +
                            "FROM " +
                            "    dbo.VW_CT_PAGAMENTO " +
                            "WHERE " +
                            "    ANO_PAGAMENTO = ? " +
                            "AND ANO_PAGAMENTO_ORDEM = ANO_PAGAMENTO " +
                            "AND ANO_EMPENHO = ANO_PAGAMENTO " +
                            "AND SEQ_CT_UNIDADE_GESTORA = 21 " + teste +
                            "ORDER BY " +
                            "    NRO_PAGAMENTO_ORDEM ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                seqPagamento = rs.getInt(1);
                autorizacao = rs.getInt(2);
                dataAutorizacao = rs.getDate(3);
                dataPagamento = rs.getDate(4);
                empenho = rs.getInt(5);
                liquidacao = rs.getInt(6);
                dataLiquidacao = rs.getDate(7);
                vencimento = rs.getDate(8);
                valorLiquidacao = rs.getBigDecimal(9);
                valorParcela = rs.getBigDecimal(10);
                valorDesconto = rs.getBigDecimal(11);
                valorPago = rs.getBigDecimal(12);
                historicoLiquidacao = rs.getString(13).trim().toUpperCase();
                historicoPagamento = rs.getString(13).trim().toUpperCase();

                parcela = getMaxParcela(emLocal, anoAtual, empenho);

                System.out.println("Autorizacaoo - " + autorizacao + " - TipoDoc: E " + " - Documento: " + empenho + " - Parcela: " + parcela + " [" + seqPagamento + "]");

                AutPagto autPagto = new AutPagto(anoAtual, autorizacao, dataAutorizacao, dataPagamento, historicoLiquidacao);
                emLocal.persist(autPagto);

                ItensAutPagto itensAutPagto = new ItensAutPagto(anoAtual, autorizacao, "E", empenho, parcela);
                emLocal.persist(itensAutPagto);

                LiquidacaoEmpenho liquidacaoEmpenho = emLocal.find(LiquidacaoEmpenho.class, new LiquidacaoEmpenhoPK(anoAtual, empenho, liquidacao));
                if (Objects.isNull(liquidacaoEmpenho)) {
                    liquidacaoEmpenho = new LiquidacaoEmpenho(anoAtual, empenho, liquidacao, dataLiquidacao, valorLiquidacao, historicoLiquidacao);
                    emLocal.persist(liquidacaoEmpenho);
                } else {
                    valorLiquidacao = valorLiquidacao.add(liquidacaoEmpenho.getValorLiquidacao());
                    liquidacaoEmpenho.setValorLiquidacao(valorLiquidacao);
                    emLocal.merge(liquidacaoEmpenho);
                }

                LiquidaPagto liquidaPagto = new LiquidaPagto(anoAtual, empenho, liquidacao, parcela);
                emLocal.persist(liquidaPagto);

                Pagamentos pagamentos = new Pagamentos(anoAtual, empenho, parcela, dataLiquidacao, vencimento, historicoPagamento, valorParcela, dataPagamento, valorPago, valorDesconto);
                emLocal.persist(pagamentos);

                // Pegando Desconto
                if (valorDesconto.signum() > 0) {
                    desconto(con, emLocal, anoAtual, "E", empenho, parcela, dataPagamento, autorizacao, seqPagamento, versaoRecurso, 110, 999);
                }

                // Pagamento
                financeiro(emLocal, con, seqPagamento, autorizacao, empenho, parcela, versaoRecurso, anoAtual, dataPagamento);
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

    private static void delete(Date anoAtual) {
        delete("CBPAUTPAGTO", anoAtual);
        delete("CBPITENSAUTPAGTO", anoAtual);
        delete("CBPAUTPAGTOFONTREC", anoAtual);
        delete("CBPLIQUIDACOES", anoAtual);
        delete("CBPLIQUIDAPAGTO", anoAtual);
        delete("CBPPAGAMENTOS", anoAtual);
        delete("CBPDESCONTOSPAGTO", anoAtual);
        delete("CBPDESCONTOSOP", anoAtual);
        delete("CBPDOCPAGTO", anoAtual);

        deleteQuery("Delete from CBPITENSGUIA A Where A.ANO = :ano " +
                " AND EXISTS ( SELECT * from CBPGUIARECEITA B Where A.ANO = B.ANO AND A.TIPO = B.TIPO AND A.GUIA = B.NUMERO AND B.ORIGEM = 'AUT' )", anoAtual);
        deleteQuery("Delete from CBPGUIARECEITA Where ANO = :ano and ORIGEM = 'AUT' ", anoAtual);

        deleteQuery("Delete from CBPDEBITO where ANOLANCTO = :ano and AUTPAGTO > 0 ", anoAtual);
        deleteQuery("Delete from CBPCHEQUE where ANOLANCTO = :ano and AUTPAGTO > 0 ", anoAtual);
    }

    private static void financeiro(EntityManager emLocal, Connection con, Integer seqPagamento, Integer autorizacao, Integer documento, Integer parcela, Integer versaoRecurso, Date anoAtual, Date dataPagamento) throws SQLException {
        PreparedStatement stmt2;
        ResultSet rs2;
        BigDecimal valorPago;
        Integer fonteRecurso,banco, fichaBancaria, nroCheque;
        String contaBancaria, formaPagamento, agencia;

        stmt2 = con.prepareStatement(
                "SELECT " +
                        "    SBL_PAGAMENTO_BANCO_DOCUMENTO, " +
                        "    COALESCE(NRO_PAGAMENTO_BANCO_DOCUMENTO,0) AS NRO_PAGAMENTO_BANCO_DOCUMENTO, " +
                        "    CASE WHEN COD_BANCO = 'CEF' THEN '104' " +
                        "       ELSE COD_BANCO END AS COD_BANCO, " +
                        "    NRO_CONTA_AGENCIA, " +
                        "    NRO_CONTA, " +
                        "    NRO_CONTA_REDUZIDO, " +
                        "    COD_FONTE_RECURSO_BANCO, " +
                        "    VLR_PAGAMENTO_BANCO " +
                        "FROM " +
                        "    dbo.VW_CT_PAGAMENTO " +
                        "WHERE " +
                        "    SEQ_CT_PAGAMENTO = ? " +
                        "AND SBL_PAGAMENTO_BANCO_DOCUMENTO IS NOT NULL " +
                        "ORDER BY " +
                        "    NRO_PAGAMENTO_ORDEM ");
        stmt2.setInt(1, seqPagamento);
        rs2 = stmt2.executeQuery();
        while (rs2.next()) {
            formaPagamento = rs2.getString(1).trim();
            nroCheque = rs2.getInt(2);
            banco = rs2.getInt(3);
            agencia = rs2.getString(4).trim();
            contaBancaria = rs2.getString(5).trim();
            fichaBancaria = rs2.getInt(6);
            fonteRecurso = rs2.getInt(7);
            valorPago = rs2.getBigDecimal(8);

            Integer max = 0;
            if (formaPagamento.equals("DB") || formaPagamento.equals("RD")) {
                Query q = emLocal.createQuery("Select MAX(a.id.numero) from Debito a where a.id.fichaConta = :ficha  and a.id.data = :data ")
                        .setParameter("ficha", fichaBancaria)
                        .setParameter("data", dataPagamento);
                max = (Integer) q.getSingleResult();
                max = (max == null) ? 0 : max;
                max++;

                Debito debito = new Debito();
                debito.getId().setFichaConta(fichaBancaria);
                debito.getId().setData(dataPagamento);
                debito.getId().setNumero(max);
                debito.setBanco(banco);
                debito.setAgencia(agencia);
                debito.setConta(contaBancaria);
                debito.setHistorico("Pagamento da Autorização: " + autorizacao );
                debito.setAnoLancto(anoAtual);
                debito.setLancamento(-1);
                debito.setValor(valorPago);
                debito.setVersaoRecurso(versaoRecurso);
                debito.setFonteRecurso(fonteRecurso);
                debito.setCaFixo(999);
                debito.setCaVariavel(0);
                debito.setFinalidade("P");
                debito.setAutPagto(autorizacao);
                emLocal.persist(debito);
            } else if(formaPagamento.equals("CH")){
                Cheque cheque = new Cheque();
                cheque.getId().setFichaConta(fichaBancaria);
                cheque.getId().setNumero(nroCheque);
                cheque.getId().setData(dataPagamento);
                cheque.getId().setHistorico("Pagamento da Autorização: " + autorizacao );
                cheque.setBanco(banco);
                cheque.setAgencia(agencia);
                cheque.setConta(contaBancaria);
                cheque.setAnoLancto(anoAtual);
                cheque.setLancamento(-1);
                cheque.setValor(valorPago);
                cheque.setVersaoRecurso(versaoRecurso);
                cheque.setFonteRecurso(fonteRecurso);
                cheque.setCaFixo(999);
                cheque.setCaVariavel(0);
                cheque.setFinalidade("P");
                cheque.setAutPagto(autorizacao);
                cheque.setDataEmissao(dataPagamento);
                cheque.setDataBaixa(dataPagamento);
                emLocal.persist(cheque);
            } else {
                max = 1;
            }

            AutPagtoFonteRec autPagtoFonteRec = new AutPagtoFonteRec(
                    anoAtual,
                    autorizacao,
                    fichaBancaria,
                    "E",
                    documento,
                    parcela,
                    1,
                    versaoRecurso,
                    fonteRecurso,
                    999,
                    0,
                    valorPago);
            emLocal.persist(autPagtoFonteRec);
        }
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

    private static void desconto(Connection con, EntityManager em, Date anoAtual, String tipoDoc, Integer
            documento, Integer parcela, Date dataGuia, Integer autorizacao, Integer seqPagamento, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo) {
        PreparedStatement stmtAux;
        ResultSet rsAux;
        Integer fichaReceita, guia;
        String tipoReceita = "E";
        BigDecimal valorReceita;

        GuiaReceita guiaReceita;
        ItensGuiaReceita itensGuiaReceita;
        DescontosPagto descontosPagto;

        try {
            stmtAux = con.prepareStatement(
                    "select SEQ_CT_DESCONTO, VLR_PAGAMENTO_DESCONTO " +
                            "from dbo.CT_PAGAMENTO_DESCONTO " +
                            "WHERE SEQ_CT_PAGAMENTO IN (?) " +
                            "Order by 1 ");
            stmtAux.setInt(1, seqPagamento);
            rsAux = stmtAux.executeQuery();
            while (rsAux.next()) {
                fichaReceita = rsAux.getInt(1);
                valorReceita = rsAux.getBigDecimal(2);

                Query q = em.createQuery("Select MAX(a.id.numero) from GuiaReceita a where a.id.ano = :ano  and a.id.tipo = :tipo ")
                        .setParameter("ano", anoAtual)
                        .setParameter("tipo", tipoReceita);
                guia = (Integer) q.getSingleResult();
                guia = (guia == null) ? 0 : guia;
                guia++;

                guiaReceita = new GuiaReceita(anoAtual, tipoReceita, guia, 1, dataGuia, dataGuia, dataGuia, "Guia de Receita de desconto - Autorização: " + autorizacao, "AUT", autorizacao);
                em.persist(guiaReceita);

                descontosPagto = new DescontosPagto(anoAtual, documento, parcela, tipoReceita, guia);
                em.persist(descontosPagto);

                itensGuiaReceita = new ItensGuiaReceita(anoAtual, tipoReceita, guia, fichaReceita, versaoRecurso, fonteRecurso, caFixo, 0, Integer.toString(fichaReceita), valorReceita);
                em.persist(itensGuiaReceita);
            }
            stmtAux.close();
            rsAux.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
