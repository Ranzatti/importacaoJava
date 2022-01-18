package Cidades.TresCoracoes;

import _Entity.EmpFonteRecurso;
import _Entity.EmpenhoComplemento;
import _Entity.Empenhos;
import _Entity.ItensEmpenho;
import _Infra.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ImportaEmpenhos extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {
        EntityManager emLocal = conexaoDestino("TresCoracoes");

        Connection con = conexaoOrigemSQLServer();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String teste, tipoEmpenho, desdobramento, historico;
        Integer empenho, complemento, ficha, fornecedor, fonteRecurso;
        BigDecimal valorEmpenho;
        Date anoAtual, dataEmpenho;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete("CBPEMPENHOS", anoAtual);
        delete("CBPEMPFONTERECURSO", anoAtual);
        delete("CBPITENSEMPENHO", anoAtual);
        delete("CBPEMPCOMPLEMENTAR", anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and A.RAP_EMN_EXE = 2021";

        System.out.println("INICIANDO IMPORTAÇÃO EMPENHOS: " + anoSonner);
        try {
            stmt = con.prepareStatement(
                    "SELECT A.NRO_EMPENHO, A.SBL_EMPENHO_TIPO, A.DAT_EMPENHO, A.VLR_EMPENHO, A.HST_EMPENHO, A.SEQ_GG_PESSOA, K.COD_FONTE_RECURSO, P.COD_PLANO_CONTA_GRUPO " +
                            "FROM DBO.CT_EMPENHO  A " +
                            "Join dbo.CT_ORCAMENTO_DESPESA_FONTE_RECURSO B ON ( A.SEQ_CT_ORCAMENTO_DESPESA_FONTE_RECURSO = B.SEQ_CT_ORCAMENTO_DESPESA_FONTE_RECURSO ) " +
                            "join dbo.CT_FONTE_RECURSO K ON ( B.SEQ_CT_FONTE_RECURSO = k.SEQ_CT_FONTE_RECURSO ) " +
                            "join dbo.CT_PLANO_CONTA P ON ( A.SEQ_CT_PLANO_CONTA = P.SEQ_CT_PLANO_CONTA)" +
                            "WHERE A.ANO_EMPENHO = ? " +
                            "and A.SEQ_CT_UNIDADE_GESTORA = 21 " + teste +
                            "order by 1 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                empenho = rs.getInt(1);
                tipoEmpenho = rs.getString(2).trim();
                dataEmpenho = rs.getDate(3);
                valorEmpenho = rs.getBigDecimal(4);
                historico = rs.getString(5).trim().toUpperCase();
                fornecedor = rs.getInt(6);
                fonteRecurso = rs.getInt(7);
                desdobramento = rs.getString(8).trim();
                desdobramento = desdobramento.length() == 8 ? desdobramento.substring(6, 8) : "99";

                ficha = 0;


                System.out.println("Ano: " + anoSonner + " - Empenho: " + empenho);

                Empenhos empenhos = new Empenhos(anoAtual, empenho, tipoEmpenho, ficha, dataEmpenho, fornecedor, desdobramento, valorEmpenho);
                emLocal.persist(empenhos);

                EmpFonteRecurso empFonteRecurso = new EmpFonteRecurso(anoAtual, empenho, 1, fonteRecurso, 999, 0, valorEmpenho);
                emLocal.persist(empFonteRecurso);

                ItensEmpenho itensEmpenho = new ItensEmpenho(anoAtual, empenho, historico, valorEmpenho);
                emLocal.persist(itensEmpenho);
            }
            stmt.close();
            rs.close();


            // EMPENHOS COMPLEMENTARES
            stmt = con.prepareStatement(
                    "SELECT NRO_EMPENHO_COMPLEMENTACAO, DAT_EMPENHO_COMPLEMENTACAO, HST_EMPENHO_COMPLEMENTACAO, VLR_EMPENHO_COMPLEMENTACAO " +
                            "FROM DBO.CT_EMPENHO_COMPLEMENTACAO  " +
                            "WHERE ANO_EMPENHO_COMPLEMENTACAO = ? " +
                            "and SEQ_CT_UNIDADE_GESTORA = 21 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                empenho = rs.getInt(1);
                dataEmpenho = rs.getDate(2);
                historico = rs.getString(3).trim().toUpperCase();
                valorEmpenho = rs.getBigDecimal(4);

                System.out.println("Ano: " + anoSonner + " - Empenho Complementar: " + empenho);

                complemento = getMaxEmpComplementar(emLocal, anoAtual, empenho);

                EmpenhoComplemento empenhoComplemento = new EmpenhoComplemento(anoAtual, empenho, complemento, dataEmpenho, historico, valorEmpenho);
                emLocal.persist(empenhoComplemento);

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

    private static int getMaxEmpComplementar(EntityManager em, Date ano, Integer empenho) {
        Query q = em.createQuery("Select MAX(a.id.complemento) from EmpenhoComplemento a where a.id.ano = :ano  and a.id.empenho = :empenho ")
                .setParameter("ano", ano)
                .setParameter("empenho", empenho);
        Integer max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }
}
