package Cidades.TresCoracoes;

import _Entity.AnulacaoEmpenho;
import _Infra.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ImportaAnulacaoEmpenho extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {

        EntityManager emLocal = conexaoDestino("TresCoracoes");

        Connection con = conexaoOrigemSQLServer();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String teste, historico;
        int empenho, anulacao;
        BigDecimal valor;
        Date anoAtual, data;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete("CBPANULACAOEMPENHO", anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and A.RAP_EMN_EXE = 2021";

        System.out.println("INICIANDO IMPORTAÇÃO ANULAÇÂO EMPENHOS: " + anoSonner);
        try {
            stmt = con.prepareStatement(
                    "SELECT NRO_EMPENHO_ANULADO, DAT_EMPENHO_ANULADO, HST_EMPENHO_ANULADO, VLR_EMPENHO_ANULADO " +
                            "FROM DBO.CT_EMPENHO_ANULADO " +
                            "WHERE ANO_EMPENHO_ANULADO = ? " +
                            "and SEQ_CT_UNIDADE_GESTORA = 21" +
                            "order by 1 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                empenho = rs.getInt(1);
                data = rs.getDate(2);
                historico = rs.getString(3).trim().toUpperCase();
                valor = rs.getBigDecimal(4);

                System.out.println("Ano: " + anoSonner + " - Empenho: " + empenho);

                anulacao = getMaxAnulacao(emLocal, anoAtual, empenho);

                AnulacaoEmpenho anulacaoEmpenho = new AnulacaoEmpenho(anoAtual, empenho, anulacao, data, valor, historico);
                emLocal.persist(anulacaoEmpenho);
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

    private static int getMaxAnulacao(EntityManager em, Date ano, Integer empenho) {
        Query q = em.createQuery("Select MAX(a.id.anulacao) from AnulacaoEmpenho a where a.id.ano = :ano  and a.id.empenho = :empenho ")
                .setParameter("ano", ano)
                .setParameter("empenho", empenho);
        Integer max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }
}
