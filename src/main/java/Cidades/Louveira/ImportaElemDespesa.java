package Cidades.Louveira;

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

public class ImportaElemDespesa extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {

        EntityManager emLocal = conexaoDestino("louveira");

        Connection con = conexaoOrigemOracle();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int empresa, ficha, fonteRecurso, caFixo, caVariavel;
        BigDecimal orcado, valor, subTotal;
        String orgao, unidade, subUnidade, funcao, subFuncao, programa, projAtiv, despesa, categoria, grupo, modalidade, elemento, desdobramento, codAplicacao, nomeDespesa, tipoConta;

        Date anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete("CBPTABDESPESAS", anoAtual);
        delete("CBPELEMDESPESA", anoAtual);
        delete("CBPELEMDESPRECURSO", anoAtual);
        delete("CBPELEMDESPCA", anoAtual);
        delete("CBPELEMDESPMENSAL", anoAtual);

        emLocal.getTransaction().begin();

        System.out.println("INICIANDO IMPORTAÇÃO ELEMDESPESA: " + anoSonner);
        try {
            // IMPORTANDO TABDESPESA
            stmt = con.prepareStatement(
                    "Select COR_COD, COR_TIT, COR_CTA_MOV  " +
                            " From CODIGOS_ORCAMENTARIOS " +
                            " where COR_EXE = ? " +
                            " And COR_REC_DSP = 1 " +
                            " Order by 1 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                despesa = rs.getString(1).trim();
                nomeDespesa = rs.getString(2).trim().toUpperCase();
                tipoConta = rs.getInt(3) == 0 ? "S" : "A";

                categoria = despesa.substring(0, 1);
                grupo = despesa.substring(1, 2);
                modalidade = despesa.substring(2, 4);
                elemento = despesa.substring(4, 6);
                desdobramento = despesa.substring(6, 8);

                TabDespesa tabDespesa = new TabDespesa(anoAtual, despesa, tipoConta, nomeDespesa, null, categoria, grupo, modalidade, elemento, desdobramento);
                emLocal.persist(tabDespesa);
            }
            stmt.close();
            rs.close();

            // importando elem despesa
            stmt = con.prepareStatement(
                    "Select OGO_COD_AUD, DOT_COD_RED, UNE_COD, FNC_COD, SFN_COD, PRO_COD, ACS_NRO, COR_COD, DOT_VLR_ORC, FRE_COD, COALESCE(FON_COD, '11000' )  " +
                            " From VW_DOTACOES" +
                            " where DOT_ANO_EXE = ? " +
                            " Order by 1, 2 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                empresa = rs.getInt(1);
                ficha = rs.getInt(2);
                orgao = rs.getString(3).trim().substring(0, 2);
                unidade = rs.getString(3).trim().substring(2, 4);
                subUnidade = rs.getString(3).trim().substring(4, 6);
                funcao = "00" + rs.getString(4).trim();
                subFuncao = "000" + rs.getString(5).trim();
                programa = "0000" + rs.getString(6).trim();
                projAtiv = "0000" + rs.getString(7).trim();
                despesa = rs.getString(8).trim();
                orcado = rs.getBigDecimal(9);
                fonteRecurso = rs.getInt(10);
                codAplicacao = rs.getString(11).trim();

                //System.out.println("Ficha: " + ficha);

                funcao = funcao.substring(funcao.length() - 2);
                subFuncao = subFuncao.substring(subFuncao.length() - 3);
                programa = programa.substring(programa.length() - 4);
                projAtiv = projAtiv.substring(projAtiv.length() - 4);
                categoria = despesa.substring(0, 1);
                grupo = despesa.substring(1, 2);
                modalidade = despesa.substring(2, 4);
                elemento = despesa.substring(4, 6);
                desdobramento = despesa.substring(6, 8);

                caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                // Elem despesa
                ElemDespesa elemDespesa = new ElemDespesa(anoAtual, empresa, ficha, orgao, unidade, subUnidade, funcao, subFuncao, programa, projAtiv, categoria, grupo, modalidade, elemento, desdobramento, orcado);
                emLocal.persist(elemDespesa);

                // Fonte Recurso
                ElemDespRecurso elemDespRecurso = new ElemDespRecurso(anoAtual, ficha, 1, fonteRecurso, orcado);
                emLocal.persist(elemDespRecurso);


                // Codigo Aplicacao
                ElemDespCA elemDespCA = new ElemDespCA(anoAtual, ficha, 1, fonteRecurso, caFixo, caVariavel, orcado);
                emLocal.persist(elemDespCA);

                //Distribuição Mensal
                subTotal = BigDecimal.ZERO;
                valor = orcado.divide(BigDecimal.valueOf(12), 2, RoundingMode.DOWN);
                for (int mes = 1; mes <= 12; mes++) {
                    if (mes == 12) {
                        valor = orcado.subtract(subTotal);
                    } else {
                        subTotal = subTotal.add(valor);
                    }

                    ElemDespMensal elemDespMensal = new ElemDespMensal(anoAtual, ficha, 1, fonteRecurso, caFixo, caVariavel, mes, valor);
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
