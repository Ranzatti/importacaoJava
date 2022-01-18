package Cidades.TresCoracoes;

import _Entity.ElemDespCA;
import _Entity.ElemDespMensal;
import _Entity.ElemDespRecurso;
import _Entity.ElemDespesa;
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

        EntityManager emLocal = conexaoDestino("TresCoracoes");

        Connection con = conexaoOrigemSQLServer();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        Integer empresa, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel, seqDespesa;
        BigDecimal orcado, valor, subTotal;
        String orgao, unidade, subUnidade, funcao, subFuncao, programa, projAtiv, despesa, categoria, grupo, modalidade, elemento, desdobramento, codAplicacao, nomeDespesa, tipoConta;

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
                    "select A.NRO_ORCAMENTO_DESPESA_FICHA, " +
                            "B.COD_UNIDADE_GESTORA_TCE, " +
                            "C.COD_UNIDADE_ORCAMENTARIA, " +
                            "E.COD_FUNCAO, " +
                            "F.COD_SUBFUNCAO, " +
                            "G.COD_PROGRAMA, " +
                            "H.COD_ATIVIDADE_PROJETO, " +
                            "I.COD_PLANO_CONTA_GRUPO, " +
                            "A.VLR_ORCAMENTO_DESPESA, " +
                            "A.SEQ_CT_ORCAMENTO_DESPESA  " +
                            " from dbo.CT_ORCAMENTO_DESPESA A " +
                            " join dbo.CT_UNIDADE_GESTORA B on ( A.SEQ_CT_UNIDADE_GESTORA = B.SEQ_CT_UNIDADE_GESTORA ) " +
                            " join dbo.CT_UNIDADE_ORCAMENTARIA C on ( A.SEQ_CT_UNIDADE_ORCAMENTARIA = C.SEQ_CT_UNIDADE_ORCAMENTARIA ) " +
                            " join dbo.CT_ORGAO D ON ( C.SEQ_CT_ORGAO = D.SEQ_CT_ORGAO ) " +
                            " join dbo.CT_FUNCAO E ON ( A.SEQ_CT_FUNCAO = E.SEQ_CT_FUNCAO ) " +
                            " join dbo.CT_SUBFUNCAO F ON ( A.SEQ_CT_SUBFUNCAO = F.SEQ_CT_SUBFUNCAO ) " +
                            " join dbo.CT_PROGRAMA G ON ( A.SEQ_CT_PROGRAMA = G.SEQ_CT_PROGRAMA ) " +
                            " join dbo.CT_ATIVIDADE_PROJETO H ON ( A.SEQ_CT_ATIVIDADE_PROJETO = H.SEQ_CT_ATIVIDADE_PROJETO ) " +
                            " join dbo.CT_PLANO_CONTA I ON ( A.SEQ_CT_PLANO_CONTA = I.SEQ_CT_PLANO_CONTA ) " +
                            " where ANO_ORCAMENTO_DESPESA = ? " +
                            " and A.SEQ_CT_UNIDADE_GESTORA = 21 " +
                            " order by 1");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ficha = rs.getInt(1);
                orgao = rs.getString(2).trim();
                unidade = rs.getString(3).trim();
                funcao = "00" + rs.getString(4).trim();
                subFuncao = "000" + rs.getString(5).trim();
                programa = "0000" + rs.getString(6).trim();
                projAtiv = "0000" + rs.getString(7).trim().replace(".", "");
                despesa = rs.getString(8).trim();
                orcado = rs.getBigDecimal(9);
                seqDespesa = rs.getInt(10);

                System.out.println("Ficha: " + ficha);

                unidade = unidade.substring(unidade.length() - 2);
                subUnidade = unidade.length() > 3 ? unidade.substring(3, 6) : "";
                funcao = funcao.substring(funcao.length() - 2);
                subFuncao = subFuncao.substring(subFuncao.length() - 3);
                programa = programa.substring(programa.length() - 4);
                projAtiv = projAtiv.substring(projAtiv.length() - 4);
                categoria = despesa.substring(0, 1);
                grupo = despesa.substring(1, 2);
                modalidade = despesa.substring(2, 4);
                elemento = despesa.substring(4, 6);

                // Elem despesa
                ElemDespesa elemDespesa = new ElemDespesa(anoAtual, 5, ficha, orgao, unidade, null, funcao, subFuncao, programa, projAtiv, categoria, grupo, modalidade, elemento, "00", orcado);
                emLocal.persist(elemDespesa);

                // Fonte Recurso
                caFixo = 999;
                caVariavel = 0;
                stmt2 = con.prepareStatement(
                        "select K.COD_FONTE_RECURSO, VLR_ORCAMENTO_DESPESA_FONTE_RECURSO " +
                                "from dbo.CT_ORCAMENTO_DESPESA_FONTE_RECURSO J " +
                                "join dbo.CT_FONTE_RECURSO K ON ( j.SEQ_CT_FONTE_RECURSO = k.SEQ_CT_FONTE_RECURSO ) " +
                                "where J.SEQ_CT_ORCAMENTO_DESPESA = ? ");
                stmt2.setInt(1, seqDespesa);
                rs2 = stmt2.executeQuery();
                while (rs2.next()) {

                    fonteRecurso = rs2.getInt(1);
                    orcado = rs2.getBigDecimal(2);

                    ElemDespRecurso elemDespRecurso = new ElemDespRecurso(anoAtual, ficha, versaoRecurso, fonteRecurso, orcado);
                    emLocal.persist(elemDespRecurso);

                    // Codigo Aplicacao
                    ElemDespCA elemDespCA = new ElemDespCA(anoAtual, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel, orcado);
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

                        ElemDespMensal elemDespMensal = new ElemDespMensal(anoAtual, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel, mes, valor);
                        emLocal.persist(elemDespMensal);
                    }

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
