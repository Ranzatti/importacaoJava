package Louveira;

import Entity.EmpFonteRecurso;
import Entity.Empenhos;
import Entity.ItensEmpenho;
import Infra.Util;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ImportaEmpenhos extends Util {

    public static void main(String[] args) {
        init(2020);
    }

    public static void init(int anoSonner) {

        EntityManager emLocal = conexaoDestino("louveira");

        Connection con = conexaoOrigemOracle();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String teste, tipoEmpenho, codAplicacao, desdobramento, descricao;
        int empenho, tipo, ficha, fornecedor, fonteRecurso, caFixo, caVariavel;
        BigDecimal valorEmpenho;
        Date anoAtual, dataEmpenho;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete("CBPEMPENHOS", anoAtual);
        delete("CBPEMPFONTERECURSO", anoAtual);
        delete("CBPITENSEMPENHO", anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and A.RAP_EMN_EXE = 2021";

        System.out.println("INICIANDO IMPORTAÇÃO EMPENHOS: " + anoSonner);
        try {
            stmt = con.prepareStatement(
                    "SELECT e.EMN_NRO, e.EMN_TPO, e.EMN_DTA, dot.DOT_COD_RED, COALESCE(f.FOR_COD_USU, '0' ), fr.FRE_COD, fra.FON_COD, co.COR_COD, e.EMN_HIS, e.EMN_VLR " +
                            " FROM EMPENHOS e " +
                            "   join DOTACOES dot on ( e.EMN_DOT_SEQ = dot.DOT_SEQ ) " +
                            "   left join FONTE_DE_RECURSO fr on ( e.EMN_FRE_SEQ = fr.FRE_SEQ ) " +
                            "   left join FONTES_DE_RECURSO_APLICACAO fra on (e.EMN_FON_SEQ = fra.FON_SEQ ) " +
                            "   left join FORNECEDORES f on ( e.EMN_FOR_SEQ = FOR_SEQ ) " +
                            "   join CODIGOS_ORCAMENTARIOS co on ( e.EMN_COR_SEQ = co.COR_SEQ ) " +
                            "   join PROGRAMAS_DE_TRABALHO_2004 pt on ( dot.DOT_PRT_SEQ = pt.PRT_SEQ ) " +
                            "   join UNIDADES_ORCAMENTARIAS_2004 uo on ( pt.PRT_UNO_SEQ = uo.UNO_SEQ) " +
                            "   join ORGAOS_2004 og on ( uo.UNO_OGO_SEQ = og.OGO_SEQ ) " +
                            " where e.EMN_EXE = ? " + teste +
                            " and og.OGO_COD_AUD = 1 " +
                            " Order by 1, 2 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                empenho = rs.getInt(1);
                tipo = rs.getInt(2);
                dataEmpenho = rs.getDate(3);
                ficha = rs.getInt(4);
                fornecedor = Integer.parseInt(rs.getString(5));
                fonteRecurso = rs.getInt(6);
                codAplicacao = rs.getString(7).trim();
                desdobramento = rs.getString(8).trim().substring(6, 8);
                descricao = rs.getString(9).trim();
                valorEmpenho = rs.getBigDecimal(10);

                //Tipo do Empenho (0=Ordinario; 1=Estimativo; 2=Global; 3=Adiantamento; 4=Empenho Complementar)
                switch (tipo) {
                    case 0:
                        tipoEmpenho = "O";
                        break;
                    case 1:
                        tipoEmpenho = "E";
                        break;
                    case 2:
                        tipoEmpenho = "G";
                        break;
                    case 3:
                        tipoEmpenho = "E";
                        break;
                    default:
                        tipoEmpenho = "O";
                        break;
                }

                caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                System.out.println("Ano: " + anoSonner + " - Empenho: " + empenho);

                Empenhos empenhos = new Empenhos(anoAtual, empenho, tipoEmpenho, ficha, dataEmpenho, fornecedor, desdobramento, valorEmpenho);
                emLocal.persist(empenhos);

                EmpFonteRecurso empFonteRecurso = new EmpFonteRecurso(anoAtual, empenho, 1, fonteRecurso, caFixo, caVariavel, valorEmpenho);
                emLocal.persist(empFonteRecurso);

                ItensEmpenho itensEmpenho = new ItensEmpenho(anoAtual, empenho, descricao, valorEmpenho);
                emLocal.persist(itensEmpenho);
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
}
