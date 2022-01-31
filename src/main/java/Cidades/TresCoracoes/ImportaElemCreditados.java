package Cidades.TresCoracoes;

import _Entity.*;
import _Infra.Util;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ImportaElemCreditados extends Util {

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

        SimpleDateFormat sdfMes = new SimpleDateFormat("MM");

        Integer codigo, fichaCredito, versaoRecurso, fonteRecursoCredito, fichaAnulada, fonteRecursoAnulada, lei, mes;
        BigDecimal valor;
        String teste;
        Date anoAtual, data, dataLei;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        versaoRecurso = getVersao(anoSonner);

        delete("CBPCRDANL", anoAtual);
        delete("CBPELEMCREDITADOS", anoAtual);
        delete("CBPELEMCREDMES", anoAtual);
        delete("CBPELEMANULADOS", anoAtual);
        delete("CBPELEMANULMES", anoAtual);
        delete("CBPELEMDISTCREDANU", anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and NRO_CREDITO_ADICIONAL = 4289 ";

        System.out.println("INICIANDO IMPORTAÇÃO ELEMDESPESA: " + anoSonner);
        try {
            // importando elem despesa
            stmt = con.prepareStatement(
                    "SELECT distinct " +
                            "    NRO_CREDITO_ADICIONAL, " +
                            "    DAT_CREDITO_ADICIONAL, " +
                            "    NRO_CREDITO_ADICIONAL_LEI, " +
                            "    DAT_CREDITO_ADICIONAL_LEI " +
                            "FROM " +
                            "    VW_CT_CREDITO_ADICIONAL " +
                            "WHERE " +
                            "    ANO_CREDITO_ADICIONAL = ? " +
                            "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                            "AND SBL_CREDITO_ADICIONAL_MOVIMENTO_RECURSO = '03' " + teste +
                            "Order by 1 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                codigo = rs.getInt(1);
                data = rs.getDate(2);
                lei = rs.getInt(3);
                dataLei = rs.getDate(4);

                mes = Integer.parseInt(sdfMes.format(data));

                System.out.println("Codigo: " + codigo);

                CreditoDotacao creditoDotacao = emLocal.find(CreditoDotacao.class, new CreditoDotacaoPK(anoAtual, codigo));
                if(Objects.isNull(creditoDotacao)) {
                    creditoDotacao = new CreditoDotacao(anoAtual, codigo, data, "S", "Crédito Suplementar", "A", null, lei, dataLei, 0, 0);
                    emLocal.persist(creditoDotacao);
                }

                // Ficha Credito
                stmt2 = con.prepareStatement(
                        "SELECT " +
                                "    NRO_ORCAMENTO_DESPESA_FICHA_SUPLEMENTADA, " +
                                "    COD_FONTE_RECURSO_SUPLEMENTADA, " +
                                "    SUM(VLR_CREDITO_ADICIONAL_SUPLEMENTADA ) " +
                                "FROM " +
                                "    VW_CT_CREDITO_ADICIONAL " +
                                "WHERE " +
                                "    ANO_CREDITO_ADICIONAL = ? " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                                "AND NRO_CREDITO_ADICIONAL = ? " +
                                "GROUP BY NRO_ORCAMENTO_DESPESA_FICHA_SUPLEMENTADA, COD_FONTE_RECURSO_SUPLEMENTADA ");
                stmt2.setInt(1, anoSonner);
                stmt2.setInt(2, codigo);
                rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    fichaCredito = rs2.getInt(1);
                    fonteRecursoCredito = rs2.getInt(2);
                    valor = rs2.getBigDecimal(3);

                    ElemCreditados elemCreditados = new ElemCreditados(anoAtual, codigo, fichaCredito, versaoRecurso, fonteRecursoCredito, 999, 0, valor);
                    emLocal.persist(elemCreditados);

                    ElemCredMes elemCredMes = new ElemCredMes(anoAtual, codigo, fichaCredito, versaoRecurso, fonteRecursoCredito, 999, 0, mes, valor);
                    emLocal.persist(elemCredMes);
                }
                stmt2.close();
                rs2.close();


                // Ficha Anulada
                stmt2 = con.prepareStatement(
                        "SELECT " +
                                "    NRO_ORCAMENTO_DESPESA_FICHA_ANULADA, " +
                                "    COD_FONTE_RECURSO_ANULADA, " +
                                "    SUM(VLR_CREDITO_ADICIONAL_ANULADA) " +
                                "FROM " +
                                "    VW_CT_CREDITO_ADICIONAL " +
                                "WHERE " +
                                "    ANO_CREDITO_ADICIONAL = ? " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                                "AND NRO_CREDITO_ADICIONAL = ? " +
                                "GROUP BY NRO_ORCAMENTO_DESPESA_FICHA_ANULADA, COD_FONTE_RECURSO_ANULADA ");
                stmt2.setInt(1, anoSonner);
                stmt2.setInt(2, codigo);
                rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    fichaAnulada = rs2.getInt(1);
                    fonteRecursoAnulada = rs2.getInt(2);
                    valor = rs2.getBigDecimal(3);

                    ElemAnulados elemAnulados = new ElemAnulados(anoAtual, codigo, fichaAnulada, versaoRecurso, fonteRecursoAnulada, 999, 0, valor);
                    emLocal.persist(elemAnulados);

                    ElemAnulMes elemAnulMes = new ElemAnulMes(anoAtual, codigo, fichaAnulada, versaoRecurso, fonteRecursoAnulada, 999, 0, mes, valor);
                    emLocal.persist(elemAnulMes);
                }
                stmt2.close();
                rs2.close();

                // DISTRIBUIÇÃO
                stmt2 = con.prepareStatement(
                        "SELECT " +
                                "    NRO_ORCAMENTO_DESPESA_FICHA_SUPLEMENTADA, " +
                                "    COD_FONTE_RECURSO_SUPLEMENTADA, " +
                                "    NRO_ORCAMENTO_DESPESA_FICHA_ANULADA, " +
                                "    COD_FONTE_RECURSO_ANULADA, " +
                                "    VLR_CREDITO_ADICIONAL_MOVIMENTO " +
                                "FROM " +
                                "    VW_CT_CREDITO_ADICIONAL " +
                                "WHERE " +
                                "    ANO_CREDITO_ADICIONAL = ? " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                                "AND NRO_CREDITO_ADICIONAL = ? " );
                stmt2.setInt(1, anoSonner);
                stmt2.setInt(2, codigo);
                rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    fichaCredito = rs2.getInt(1);
                    fonteRecursoCredito = rs2.getInt(2);
                    fichaAnulada = rs2.getInt(3);
                    fonteRecursoAnulada = rs2.getInt(4);
                    valor = rs2.getBigDecimal(5);

                    ElemCredDistrib elemCredDistrib = new ElemCredDistrib(anoAtual, codigo,
                            fichaCredito, versaoRecurso, fonteRecursoCredito, 999, 0,
                            fichaAnulada, versaoRecurso, fonteRecursoAnulada, 999, 0, valor);
                    emLocal.persist(elemCredDistrib);
                }
                stmt2.close();
                rs2.close();
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
