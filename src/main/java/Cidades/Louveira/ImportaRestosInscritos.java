package Cidades.Louveira;

import _Entity.RestosFonteRec;
import _Entity.RestosInscritos;
import _Entity.RestosProcParc;
import _Infra.Util;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ImportaRestosInscritos extends Util {

    public static void main(String[] args) {
        init();
    }

    public static void init() {

        EntityManager emLocal = conexaoDestino("louveira");

        Connection con = conexaoOrigemOracle();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Date anoEmpenho, dataEmpenho, ultimoDiaExercicio;
        String teste, nomeCredor, desdobramento, codAplicacao;
        int ano, empenho, credor, ficha, fonteRecurso, caFixo, caVariavel;
        BigDecimal valorProcessado, valorNaoProcessado, valorEmpenho, totalLiquidado, totalPago;

        delete("CBPRESTOSINSCRITOS");
        delete("CBPRESTOSFONTEREC");
        delete("CBPRESTOSPROCPARC");

        emLocal.getTransaction().begin();

        teste = "";
        // teste = "and A.RAP_EMN_EXE = 2020 and A.RAP_EMN_NRO = 105 ";

        System.out.println("INICIANDO IMPORTAÇÂO RESTOS INSCRITOS");
        try {
            stmt = con.prepareStatement(
                    "Select distinct RAP_EMN_EXE, A.RAP_EMN_NRO, G.FOR_COD_USU, G.FOR_RAZ_SOC, B.DOT_COD_RED, C.COR_COD, A.RAP_EMN_DTA, E.FRE_COD, F.FON_COD, " +
                            " COALESCE(D.EMN_VLR, 0) + COALESCE(EMN_VLR_CEM,0), COALESCE(D.EMN_VLR_LIQ, 0), COALESCE(D.EMN_VLR_PGO, 0) " +
                            " from RESTOS_A_PAGAR A " +
                            " join DOTACOES B ON ( A.RAP_DOT_SEQ = B.DOT_SEQ ) " +
                            " join CODIGOS_ORCAMENTARIOS C ON ( A.RAP_COR_SEQ = C.COR_SEQ ) " +
                            " join EMPENHOS D ON ( A.RAP_EMN_SEQ = D.EMN_SEQ ) " +
                            " join FONTE_DE_RECURSO E ON ( A.RAP_FRE_SEQ = E.FRE_SEQ ) " +
                            " join FONTES_DE_RECURSO_APLICACAO f on ( A.RAP_FON_SEQ = f.FON_SEQ) " +
                            " join FORNECEDORES G on ( A.RAP_FOR_SEQ = G.FOR_SEQ )" +
                            " where RAP_OGO_SEQ in ( select OGO_SEQ from ORGAOS_2004 where OGO_COD = 1 ) " +
                            " and ( A.RAP_VLR_ANL is null or A.RAP_VLR_ANL = 0) " + teste +
                            " ORDER BY 1, 2 ");
            rs = stmt.executeQuery();
            while (rs.next()) {
                ano = rs.getInt(1);
                empenho = rs.getInt(2);
                credor = rs.getInt(3);
                nomeCredor = rs.getString(4).trim();
                ficha = rs.getInt(5);
                desdobramento = rs.getString(6).trim().substring(6, 8);
                dataEmpenho = rs.getDate(7);
                fonteRecurso = rs.getInt(8);
                codAplicacao = rs.getString(9);
                valorEmpenho = rs.getBigDecimal(10);
                totalLiquidado = rs.getBigDecimal(11);
                totalPago = rs.getBigDecimal(12);

                caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                nomeCredor = nomeCredor.length() > 100 ? nomeCredor.substring(0, 100) : nomeCredor;
                anoEmpenho = java.sql.Date.valueOf(ano + "-01-01");

                valorProcessado = valorEmpenho.subtract(totalLiquidado);
                valorNaoProcessado = totalLiquidado.subtract(totalPago);

                System.out.println(ano + " - " + empenho);

                RestosInscritos restosInscritos = new RestosInscritos();
                restosInscritos.getId().setAno(anoEmpenho);
                restosInscritos.getId().setEmpenho(empenho);
                restosInscritos.setCodCredor(credor);
                restosInscritos.setNomeCredor(nomeCredor);
                restosInscritos.setFicha(ficha);
                restosInscritos.setSubElemento(desdobramento);
                restosInscritos.setDataEmpenho(dataEmpenho);
                restosInscritos.setValorProcessado(valorProcessado);
                restosInscritos.setValorNaoProcessado(valorNaoProcessado);
                restosInscritos.setValorEmpenho(valorEmpenho);
                emLocal.persist(restosInscritos);

                // Fonte Recurso
                RestosFonteRec restosFonteRec = new RestosFonteRec();
                restosFonteRec.getId().setAno(anoEmpenho);
                restosFonteRec.getId().setEmpenho(empenho);
                restosFonteRec.setVersaoRecurso(1);
                restosFonteRec.setFonteRecurso(fonteRecurso);
                restosFonteRec.setCaFixo(caFixo);
                restosFonteRec.setCaVariavel(caVariavel);
                restosFonteRec.setValor(valorProcessado.add(valorNaoProcessado));
                emLocal.persist(restosFonteRec);

                // Proc Parc
                if (valorProcessado.signum() > 0) {
                    ultimoDiaExercicio = java.sql.Date.valueOf(ano + "-12-31");
                    RestosProcParc restosProcParc = new RestosProcParc();
                    restosProcParc.getId().setAnoEmpenho(anoEmpenho);
                    restosProcParc.getId().setEmpenho(empenho);
                    restosProcParc.getId().setParcela(1);
                    restosProcParc.setVencimento(ultimoDiaExercicio);
                    restosProcParc.setValor(valorProcessado);
                    emLocal.persist(restosProcParc);
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
}
