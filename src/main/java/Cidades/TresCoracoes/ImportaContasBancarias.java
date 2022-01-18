package Cidades.Louveira;

import _Entity.*;
import _Infra.Util;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class ImportaContasBancarias extends Util {

    public static void main(String[] args) {

        EntityManager emLocal = conexaoDestino("louveira");

        Connection con = conexaoOrigemOracle();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int bancoCodigo, ficha, fonteRecurso, caFixo, caVariavel, empresa;
        String agenciaCodigo, conta, nome, dv, tipo, codAplicacao;
        Date abertura, encerramento;

        delete("CBPBANCOS");
        delete("CBPAGENCIAS");
        delete("CBPCONTAS");
        delete("CBPCONTASFONTEREC");
        delete("CBPCONTASCA");

        emLocal.getTransaction().begin();

        System.out.println("INICIANDO IMPORTAÇÃO CONTAS BANCARIAS ");
        try {
            // Bancos
            stmt = con.prepareStatement(
                    "select BCO_COD, BCO_DSC " +
                            " from bancos " +
                            " ORDER BY 1");
            rs = stmt.executeQuery();
            while (rs.next()) {
                bancoCodigo = rs.getInt(1);
                nome = rs.getString(2).trim().toUpperCase();

                Banco banco = new Banco(bancoCodigo, nome);
                emLocal.persist(banco);
            }
            stmt.close();
            rs.close();

            // Agencias
            stmt = con.prepareStatement(
                    "select B.BCO_COD, A.AGE_COD, A.AGE_DSC " +
                            " from AGENCIAS A " +
                            " JOIN BANCOS B ON ( A.AGE_BCO_SEQ = B.BCO_SEQ )" +
                            " order by 1, 2");
            rs = stmt.executeQuery();
            while (rs.next()) {
                bancoCodigo = rs.getInt(1);
                agenciaCodigo = rs.getString(2);
                nome = rs.getString(3).trim().toUpperCase();

                Agencia agencia = new Agencia(bancoCodigo, agenciaCodigo, nome);
                emLocal.persist(agencia);
            }
            stmt.close();
            rs.close();

            // Contas Bancárias
            stmt = con.prepareStatement(
                    "select C.BCO_COD, B.AGE_COD, A.CBN_NRO, A.CBN_DIG_CTA, A.CBN_COD, A.CBN_DSC, A.CBN_TPO, A.CBN_DTA_ABT, A.CBN_DTA_ENC, D.FRE_COD, COALESCE(E.FON_COD, '11000'), F.OGO_COD_AUD " +
                            "from CONTAS_BANCARIAS A" +
                            "        JOIN AGENCIAS B ON ( A.CBN_AGE_SEQ = B.AGE_SEQ ) " +
                            "        JOIN BANCOS C ON ( B.AGE_BCO_SEQ = C.BCO_SEQ ) " +
                            "        left JOIN FONTE_DE_RECURSO D ON ( A.CBN_FRE_SEQ = D.FRE_SEQ ) " +
                            "        left JOIN FONTES_DE_RECURSO_APLICACAO E ON ( A.CBN_FON_SEQ = E.FON_SEQ ) " +
                            "        join ORGAOS_2004 F ON ( A.CBN_OGO_SEQ = F.OGO_SEQ ) " +
                            "where CBN_TPO > 1 " +
                            "order by A.CBN_NRO ");
            rs = stmt.executeQuery();
            while (rs.next()) {
                bancoCodigo = rs.getInt(1);
                agenciaCodigo = rs.getString(2).trim();
                conta = rs.getString(3).trim();
                dv = rs.getString(4).trim();
                ficha = Integer.parseInt(rs.getString(5));
                nome = rs.getString(6).trim().toUpperCase();
                tipo = Integer.toString(rs.getInt(7));
                abertura = rs.getDate(8);
                encerramento = rs.getDate(9);
                fonteRecurso = rs.getInt(10);
                codAplicacao = rs.getString(11);
                empresa = rs.getInt(12);

                caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                System.out.println("Ficha: " + ficha);

                tipo = tipo.equals("2") ? "M" : tipo;
                tipo = tipo.equals("5") ? "V" : tipo;
                tipo = tipo.equals("6") ? "E" : tipo;
                tipo = tipo.equals("13") ? "P" : tipo;

                if (tipo.length() > 1) {
                    tipo = "M";
                }

                ContasBancarias contas = emLocal.find(ContasBancarias.class, ficha);

                if (Objects.isNull(contas)) {
                    ContasBancarias contasBancarias = new ContasBancarias(ficha, bancoCodigo, agenciaCodigo, conta, dv, nome, tipo, "Prefeitura Municipal de Cidades.Louveira",
                            bancoCodigo, agenciaCodigo, conta, empresa, encerramento, abertura);
                    emLocal.persist(contasBancarias);

                    ContasFonteRecurso contasFonteRecurso = new ContasFonteRecurso(ficha, 1, fonteRecurso, BigDecimal.ZERO);
                    emLocal.persist(contasFonteRecurso);

                    ContasCA contasCA = new ContasCA(ficha, 1, fonteRecurso, caFixo, caVariavel, BigDecimal.ZERO);
                    emLocal.persist(contasCA);
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
