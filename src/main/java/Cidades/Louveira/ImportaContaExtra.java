package Cidades.Louveira;

import _Entity.ContaExtra;
import _Entity.ContaExtraFonteRec;
import _Infra.Util;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ImportaContaExtra extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {

        EntityManager emLocal = conexaoDestino("louveira");

        Connection con = conexaoOrigemOracle();

        Connection conSqlServer = conexaoOrigemSQLServer();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        String nome;
        int codigo;
        Date anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete("CBPCONTAEXTRA", anoAtual);
        delete("CBPCONTAEXFONTEREC", anoAtual);

        emLocal.getTransaction().begin();

        System.out.println("INICIANDO IMPORTAÇÃO CONTA EXTRA: " + anoSonner);
        try {
            stmt = con.prepareStatement(
                    "select OPE_COD, OPE_DSC " +
                            " from OPERACOES " +
                            " where OPE_MOD in (1,2) " +
                            " Order by 1 ");
            rs = stmt.executeQuery();
            while (rs.next()) {

                codigo = rs.getInt(1);
                nome = rs.getString(2).trim().toUpperCase();

                ContaExtra contaExtra = new ContaExtra();
                contaExtra.getId().setAno(anoAtual);
                contaExtra.getId().setContaExtra(codigo);
                contaExtra.setNome(nome);
                contaExtra.setEmpresa(1);
                emLocal.persist(contaExtra);

                ContaExtraFonteRec contaExtraFonteRec = new ContaExtraFonteRec();
                contaExtraFonteRec.getId().setAno(anoAtual);
                contaExtraFonteRec.getId().setContaExtra(codigo);
                contaExtraFonteRec.getId().setVersaoRecurso(1);
                contaExtraFonteRec.getId().setFonteRecurso(1);
                contaExtraFonteRec.setSaldo(BigDecimal.ZERO);
                emLocal.persist(contaExtraFonteRec);
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
