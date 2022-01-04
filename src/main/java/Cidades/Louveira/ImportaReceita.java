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

public class ImportaReceita extends Util {

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
        String receita, nomeReceita, tipoConta, codAplicacao;

        Date anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete("CBPTABRECEITAS", anoAtual);
        delete("CBPRECEITAS", anoAtual);
        delete("CBPRECFONTERECURSO", anoAtual);
        delete("CBPRECEITASCA", anoAtual);
        delete("CBPRECEITASMENSAL", anoAtual);

        emLocal.getTransaction().begin();

        System.out.println("INICIANDO IMPORTAÇÃO RECEITA: " + anoSonner);
        try {
            // importanto tab Receitas
            stmt = con.prepareStatement(
                    "Select COR_COD, COR_TIT, COR_CTA_MOV  " +
                            " From CODIGOS_ORCAMENTARIOS " +
                            " where COR_EXE = ? " +
                            " And COR_REC_DSP = 0 " +
                            " Order by 1 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                receita = rs.getString(1).trim();
                nomeReceita = rs.getString(2).trim().toUpperCase();
                tipoConta = rs.getInt(3) == 0 ? "S" : "A";

                TabReceitas tabReceitas = new TabReceitas(anoAtual, receita, tipoConta, nomeReceita, null, "S", BigDecimal.ZERO, 0, 0);
                emLocal.persist(tabReceitas);

            }
            stmt.close();
            rs.close();

            // importando receita
            stmt = con.prepareStatement(
                    "Select REC_COD_RED, REC_COR_COD, OGO_COD_AUD, COALESCE(FRE_COD, 1 ), COALESCE(FON_COD, '11000' ), REC_VLR_ORC "
                            + " From VW_RECEITAS_BURTS "
                            + " Where REC_EXE = ? "
                            + " Order by 1 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ficha = rs.getInt(1);
                receita = rs.getString(2).trim();
                empresa = rs.getInt(3);
                fonteRecurso = rs.getInt(4);
                codAplicacao = rs.getString(5);
                orcado = rs.getBigDecimal(6);

                caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                Receitas receitas = new Receitas(anoAtual, empresa, ficha, receita, orcado);
                emLocal.persist(receitas);

                ReceitaFonteRecurso receitaFonteRecurso = new ReceitaFonteRecurso(anoAtual, ficha, 1, fonteRecurso, orcado);
                emLocal.persist(receitaFonteRecurso);

                ReceitasCA receitasCA = new ReceitasCA(anoAtual, ficha, 1, fonteRecurso, caFixo, caVariavel, orcado);
                emLocal.persist(receitasCA);

                subTotal = BigDecimal.ZERO;
                valor = orcado.divide(BigDecimal.valueOf(12), 2, RoundingMode.DOWN);
                for (int mes = 1; mes <= 12; mes++) {
                    if (mes == 12) {
                        valor = orcado.subtract(subTotal);
                    } else {
                        subTotal = subTotal.add(valor);
                    }

                    ReceitaMensal receitaMensal = new ReceitaMensal(anoAtual, ficha, 1, fonteRecurso, caFixo, caVariavel, mes, valor);
                    emLocal.persist(receitaMensal);
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
