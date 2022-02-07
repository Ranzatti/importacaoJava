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

public class ImportaReceita extends Util {

    public static void main(String[] args) {
        init(2016);
    }

    public static void init(int anoSonner) {

        EntityManager emLocal = conexaoDestino("TresCoracoes");

        Connection con = conexaoOrigemSQLServer();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Integer ficha, versaoRecurso, fonteRecurso;
        BigDecimal orcado, orcadoFonteRecurso, valor, subTotal, percentual;
        String receita, nomeReceita, tipo;
        boolean arrecadaProporc;

        Date anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete("CBPTABRECEITAS", anoAtual);
        delete("CBPRECEITAS", anoAtual);
        delete("CBPRECFONTERECURSO", anoAtual);
        delete("CBPRECEITASCA", anoAtual);
        delete("CBPRECEITASMENSAL", anoAtual);

        versaoRecurso = getVersao(anoSonner);

        emLocal.getTransaction().begin();

        System.out.println("INICIANDO IMPORTAÇÃO RECEITA: " + anoSonner);
        try {
            // importanto tab Receitas
            stmt = con.prepareStatement(
                    "SELECT " +
                            "    COD_PLANO_CONTA, " +
                            "    NOM_PLANO_CONTA, " +
                            "    SBL_PLANO_CONTA_SISTEMA_TCE " +
                            "FROM " +
                            "    CT_PLANO_CONTA " +
                            "WHERE " +
                            "    ANO_PLANO_CONTA_INICIO = ? " +
                            "AND ANO_PLANO_CONTA_FIM = ANO_PLANO_CONTA_INICIO " +
                            "AND SBL_PLANO_CONTA_SISTEMA_TCE IN (0,2) " +
                            "ORDER BY " +
                            "    COD_PLANO_CONTA ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                receita = rs.getString(1).trim();
                nomeReceita = rs.getString(2).trim().toUpperCase();
                tipo = rs.getInt(3) == 0 ? "S" : "A";

                TabReceitas tabReceitas = emLocal.find(TabReceitas.class, new TabReceitasPK(anoAtual, receita));

                if (Objects.isNull(tabReceitas)) {
                    tabReceitas = new TabReceitas();
                    tabReceitas.getId().setAno(anoAtual);
                    tabReceitas.getId().setReceita(receita);
                    tabReceitas.setTipoConta(tipo);
                    tabReceitas.setNome(nomeReceita);
                    tabReceitas.setOrigemNaLei("S");
                    tabReceitas.setPercentual(BigDecimal.ZERO);
                    tabReceitas.setRecDividaAtiva(0);
                    tabReceitas.setDeducao(0);
                    emLocal.persist(tabReceitas);
                }
            }
            stmt.close();
            rs.close();

            // importando receita
            stmt = con.prepareStatement(
                    "SELECT " +
                            "    NRO_ORCAMENTO_RECEITA_FICHA, " +
                            "    COD_PLANO_CONTA, " +
                            "    COD_FONTE_RECURSO, " +
                            "    VLR_ORCAMENTO_RECEITA, " +
                            "    VLR_ORCAMENTO_RECEITA_FONTE_RECURSO, " +
                            "    PRC_ORCAMENTO_RECEITA_FONTE_RECURSO, " +
                            "    NOM_PLANO_CONTA " +
                            "FROM " +
                            "    VW_CT_ORCAMENTO_RECEITA " +
                            "WHERE " +
                            "    ANO_ORCAMENTO_RECEITA = ? " +
                            "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                            "ORDER BY 1 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ficha = rs.getInt(1);
                receita = rs.getString(2).trim();
                fonteRecurso = rs.getInt(3);
                orcado = rs.getBigDecimal(4);
                orcadoFonteRecurso = rs.getBigDecimal(5);
                percentual = rs.getBigDecimal(6);
                nomeReceita = rs.getString(7).toUpperCase();

                receita = receita.length() > 11 ? receita.substring(0, 11) : receita;
                arrecadaProporc = percentual.signum() == 0 ? false : true;

                TabReceitas tabReceitas = emLocal.find(TabReceitas.class, new TabReceitasPK(anoAtual, receita));
                if (Objects.isNull(tabReceitas)) {
                    tabReceitas = new TabReceitas();
                    tabReceitas.getId().setAno(anoAtual);
                    tabReceitas.getId().setReceita(receita);
                    tabReceitas.setTipoConta("A");
                    tabReceitas.setNome(nomeReceita);
                    tabReceitas.setOrigemNaLei("S");
                    tabReceitas.setPercentual(BigDecimal.ZERO);
                    tabReceitas.setRecDividaAtiva(0);
                    tabReceitas.setDeducao(0);
                    emLocal.persist(tabReceitas);
                }

                Receitas receitas = emLocal.find(Receitas.class, new ReceitasPK(anoAtual, ficha));
                if (Objects.isNull(receitas)) {
                    receitas = new Receitas();
                    receitas.getId().setAno(anoAtual);
                    receitas.getId().setFicha(ficha);
                    receitas.setReceita(receita);
                    receitas.setArrecadaProporc(arrecadaProporc);
                    receitas.setEmpresa(5);
                    receitas.setOrcado(orcado);
                    emLocal.persist(receitas);
                }

                ReceitaFonteRecurso receitaFonteRecurso = new ReceitaFonteRecurso();
                receitaFonteRecurso.getId().setAno(anoAtual);
                receitaFonteRecurso.getId().setFichaReceita(ficha);
                receitaFonteRecurso.getId().setVersaoRecurso(versaoRecurso);
                receitaFonteRecurso.getId().setFonteRecurso(fonteRecurso);
                receitaFonteRecurso.setPercentual(percentual);
                receitaFonteRecurso.setOrcado(orcadoFonteRecurso);
                emLocal.persist(receitaFonteRecurso);

                ReceitasCA receitasCA = new ReceitasCA();
                receitasCA.getId().setAno(anoAtual);
                receitasCA.getId().setFicha(ficha);
                receitasCA.getId().setVersaoRecurso(versaoRecurso);
                receitasCA.getId().setFonteRecurso(fonteRecurso);
                receitasCA.getId().setCaFixo(999);
                receitasCA.getId().setCaVariavel(0);
                receitasCA.setOrcado(orcadoFonteRecurso);
                emLocal.persist(receitasCA);

                subTotal = BigDecimal.ZERO;
                valor = orcadoFonteRecurso.divide(BigDecimal.valueOf(12), 2, RoundingMode.DOWN);
                for (int mes = 1; mes <= 12; mes++) {
                    if (mes == 12) {
                        valor = orcado.subtract(subTotal);
                    } else {
                        subTotal = subTotal.add(valor);
                    }

                    ReceitaMensal receitaMensal = new ReceitaMensal();
                    receitaMensal.getId().setAno(anoAtual);
                    receitaMensal.getId().setFicha(ficha);
                    receitaMensal.getId().setVersaoRecurso(versaoRecurso);
                    receitaMensal.getId().setFonteRecurso(fonteRecurso);
                    receitaMensal.getId().setCaFixo(999);
                    receitaMensal.getId().setCaVariavel(0);
                    receitaMensal.getId().setMes(mes);
                    receitaMensal.setOrcado(valor);
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
