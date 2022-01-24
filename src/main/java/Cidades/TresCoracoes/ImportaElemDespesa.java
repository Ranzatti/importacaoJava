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
import java.util.Date;
import java.util.Objects;

public class ImportaElemDespesa extends Util {

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

        Integer empresa, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel, seqDespesa;
        BigDecimal orcado, orcadoFonteRecurso, valor, subTotal, ordinario, vinculado;
        String orgao, unidade, subUnidade, funcao, subFuncao, programa, projAtiv, despesa, categoria, grupo, modalidade, elemento, desdobramento, codAplicacao, nomeDespesa, tipoConta, ordinario_vinculado;

        Date anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        versaoRecurso = getVersao(anoSonner);

        delete("CBPTABDESPESAS", anoAtual);
        delete("CBPELEMDESPESA", anoAtual);
        delete("CBPELEMDESPRECURSO", anoAtual);
        delete("CBPELEMDESPCA", anoAtual);
        delete("CBPELEMDESPMENSAL", anoAtual);

        emLocal.getTransaction().begin();

        System.out.println("INICIANDO IMPORTAÇÃO ELEMDESPESA: " + anoSonner);
        try {
            // importando elem despesa
            stmt = con.prepareStatement(
                    "SELECT " +
                            "    NRO_ORCAMENTO_DESPESA_FICHA, " +
                            "    COD_UNIDADE_GESTORA, " +
                            "    COD_UNIDADE_ORCAMENTARIA, " +
                            "    COD_SUBUNIDADE_ORCAMENTARIA, " +
                            "    COD_FUNCAO, " +
                            "    COD_SUBFUNCAO, " +
                            "    COD_PROGRAMA, " +
                            "    COD_ATIVIDADE_PROJETO, " +
                            "    COD_PLANO_CONTA_GRUPO, " +
                            "    SBL_ORCAMENTO_DESPESA_ORDINARIO_VINCULADO, " +
                            "    COD_FONTE_RECURSO, " +
                            "    VLR_ORCAMENTO_DESPESA, " +
                            "    VLR_ORCAMENTO_DESPESA_FONTE_RECURSO " +
                            "FROM " +
                            "    VW_CT_ORCAMENTO_DESPESA " +
                            "WHERE " +
                            "    ANO_ORCAMENTO_DESPESA = ? " +
                            "AND SEQ_CT_UNIDADE_GESTORA = 21" +
                            "Order by 1");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ficha = rs.getInt(1);
                orgao = rs.getString(2).trim();
                unidade = rs.getString(3).trim();
                subUnidade = rs.getString(4);
                funcao = "00" + rs.getString(5).trim();
                subFuncao = "000" + rs.getString(6).trim();
                programa = "0000" + rs.getString(7).trim();
                projAtiv = "0000" + rs.getString(8).trim().replace(".", "");
                despesa = rs.getString(9).trim();
                ordinario_vinculado = rs.getString(10).trim();
                fonteRecurso = rs.getInt(11);
                orcado = rs.getBigDecimal(12);
                orcadoFonteRecurso = rs.getBigDecimal(13);

                System.out.println("Ficha: " + ficha);

                ElemDespesa elemDespesa = emLocal.find(ElemDespesa.class, new ElemDespesaPK(anoAtual, ficha));

                if(Objects.isNull(elemDespesa)) {
                    unidade = unidade.substring(unidade.length() - 2);
                    funcao = funcao.substring(funcao.length() - 2);
                    subFuncao = subFuncao.substring(subFuncao.length() - 3);
                    programa = programa.substring(programa.length() - 4);
                    projAtiv = projAtiv.substring(projAtiv.length() - 4);
                    categoria = despesa.substring(0, 1);
                    grupo = despesa.substring(1, 2);
                    modalidade = despesa.substring(2, 4);
                    elemento = despesa.substring(4, 6);

                    ordinario = BigDecimal.ZERO;
                    vinculado = BigDecimal.ZERO;
                    if(ordinario_vinculado.equals("O")){
                        ordinario = orcado;
                    } else {
                        vinculado = orcado;
                    }

                    // Elem despesa
                    elemDespesa = new ElemDespesa(anoAtual, 5, ficha, orgao, unidade, null, funcao, subFuncao, programa, projAtiv, categoria, grupo, modalidade, elemento, "00", orcado, ordinario, vinculado);
                    emLocal.persist(elemDespesa);
                }

                // Fonte Recurso
                ElemDespRecurso elemDespRecurso = new ElemDespRecurso(anoAtual, ficha, versaoRecurso, fonteRecurso, orcadoFonteRecurso);
                emLocal.persist(elemDespRecurso);

                // Codigo Aplicacao
                ElemDespCA elemDespCA = new ElemDespCA(anoAtual, ficha, versaoRecurso, fonteRecurso, 999, 0, orcadoFonteRecurso);
                emLocal.persist(elemDespCA);

                //Distribuição Mensal
                subTotal = BigDecimal.ZERO;
                valor = orcadoFonteRecurso.divide(BigDecimal.valueOf(12), 2, RoundingMode.DOWN);
                for (int mes = 1; mes <= 12; mes++) {
                    if (mes == 12) {
                        valor = orcado.subtract(subTotal);
                    } else {
                        subTotal = subTotal.add(valor);
                    }

                    ElemDespMensal elemDespMensal = new ElemDespMensal(anoAtual, ficha, versaoRecurso, fonteRecurso, 999, 0, mes, valor);
                    emLocal.persist(elemDespMensal);
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
