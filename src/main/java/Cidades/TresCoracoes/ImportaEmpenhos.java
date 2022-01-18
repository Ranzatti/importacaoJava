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
        Integer empenho, complemento, ficha, fornecedor, versaoRecurso, fonteRecurso, classeDespesa;
        BigDecimal valorEmpenho;
        Date anoAtual, dataEmpenho;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        versaoRecurso = getVersao(anoSonner);

        delete("CBPEMPENHOS", anoAtual);
        delete("CBPEMPFONTERECURSO", anoAtual);
        delete("CBPITENSEMPENHO", anoAtual);
        delete("CBPEMPCOMPLEMENTAR", anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and NRO_EMPENHO = 10314";

        System.out.println("INICIANDO IMPORTAÇÃO EMPENHOS: " + anoSonner);
        try {
            stmt = con.prepareStatement(
                    "SELECT " +
                            "    NRO_EMPENHO, " +
                            "    SBL_EMPENHO_TIPO, " +
                            "    DAT_EMPENHO, " +
                            "    NRO_ORCAMENTO_DESPESA_FICHA, " +
                            "    VLR_EMPENHO, " +
                            "    HST_EMPENHO, " +
                            "    SEQ_GG_PESSOA, " +
                            "    COD_FONTE_RECURSO, " +
                            "    coalesce(COD_PLANO_CONTA_SUBELEMENTO, '00000099000') as COD_PLANO_CONTA_SUBELEMENTO, " +
                            "    coalesce(SEQ_CT_CENTRO_CUSTO, 0) as SEQ_CT_CENTRO_CUSTO " +
                            "FROM " +
                            "    dbo.VW_CT_EMPENHO " +
                            "WHERE " +
                            "    ANO_EMPENHO = ? " + teste +
                            "AND SEQ_CT_UNIDADE_GESTORA = 21 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                empenho = rs.getInt(1);
                tipoEmpenho = rs.getString(2).trim();
                dataEmpenho = rs.getDate(3);
                ficha = rs.getInt(4);
                valorEmpenho = rs.getBigDecimal(5);
                historico = rs.getString(6).trim().toUpperCase();
                fornecedor = rs.getInt(7);
                fonteRecurso = rs.getInt(8);
                desdobramento = rs.getString(9).trim();
                desdobramento = desdobramento.equals("") ? "99" : desdobramento.substring(6, 8);
                classeDespesa = rs.getInt(10);

                System.out.println("Ano: " + anoSonner + " - Empenho: " + empenho);

                Empenhos empenhos = new Empenhos(anoAtual, empenho, tipoEmpenho, ficha, dataEmpenho, fornecedor, desdobramento, classeDespesa, valorEmpenho);
                emLocal.persist(empenhos);

                EmpFonteRecurso empFonteRecurso = new EmpFonteRecurso(anoAtual, empenho, versaoRecurso, fonteRecurso, 999, 0, valorEmpenho);
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
