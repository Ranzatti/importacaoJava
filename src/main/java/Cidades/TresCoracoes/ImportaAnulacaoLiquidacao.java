package Cidades.TresCoracoes;

import _Entity.AnulacaoLiquida;
import _Infra.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ImportaAnulacaoLiquidacao extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {

        EntityManager emLocal = conexaoDestino("TresCoracoes");

        Connection con = conexaoOrigemSQLServer();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String teste, historico;
        Integer empenho, liquidacao, anulacao;
        BigDecimal valor;
        Date anoAtual, data;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete("CBPANULACAOLIQUIDA", anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and A.RAP_EMN_EXE = 2021";

        System.out.println("INICIANDO IMPORTAÇÃO ANULAÇÂO EMPENHOS: " + anoSonner);
        try {
            stmt = con.prepareStatement(
                    "SELECT " +
                            "    NRO_EMPENHO, " +
                            "    NRO_LIQUIDACAO_EMPENHO, " +
                            "    DAT_LIQUIDACAO_ANULADA, " +
                            "    VLR_LIQUIDACAO_ANULADA, " +
                            "    HST_LIQUIDACAO_ANULADA " +
                            "FROM " +
                            "    vw_CT_LIQUIDACAO_ANULADA " +
                            "WHERE " +
                            "    ANO_LIQUIDACAO_ANULADA = ? " +
                            "AND SEQ_CT_UNIDADE_GESTORA = 21" +
                            "ORDER BY 1,2,3 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                empenho = rs.getInt(1);
                liquidacao = rs.getInt(2);
                data = rs.getDate(3);
                valor = rs.getBigDecimal(4);
                historico = rs.getString(5).trim().toUpperCase();

                System.out.println("Ano: " + anoSonner + " - Empenho: " + empenho);

                anulacao = getMaxAnulacao(emLocal, anoAtual, empenho, liquidacao);

                AnulacaoLiquida anulacaoLiquida = new AnulacaoLiquida();
                anulacaoLiquida.getId().setAno(anoAtual);
                anulacaoLiquida.getId().setEmpenho(empenho);
                anulacaoLiquida.getId().setLiquidacao(liquidacao);
                anulacaoLiquida.getId().setAnulacao(anulacao);
                anulacaoLiquida.setData(data);
                anulacaoLiquida.setHistorico(historico);
                anulacaoLiquida.setValor(valor);
                emLocal.persist(anulacaoLiquida);
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

    private static int getMaxAnulacao(EntityManager em, Date ano, Integer empenho, Integer liquidacao) {
        Query q = em.createQuery("Select MAX(a.id.anulacao) from AnulacaoLiquida a where a.id.ano = :ano  and a.id.empenho = :empenho and a.id.liquidacao = :liquidacao ")
                .setParameter("ano", ano)
                .setParameter("empenho", empenho)
                .setParameter("liquidacao", liquidacao);
        Integer max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }
}
