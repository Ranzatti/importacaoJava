package Cidades.Louveira;

import _Entity.OPFonteRecurso;
import _Entity.OrdensPagto;
import _Entity.PagtoOP;
import _Infra.Util;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ImportaOrdensPagto extends Util {

    public static void main(String[] args) {
        init(2020);
    }

    public static void init(int anoSonner) {

        EntityManager emLocal = conexaoDestino("louveira");

        Connection con = conexaoOrigemOracle();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int nroOP, ficha;
        BigDecimal valorOP;
        Date data;
        String teste, historico;

        Date anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete("CBPORDENSPAGTO", anoAtual);
        delete("CBPPAGTOOPS", anoAtual);
        delete("CBPOPFONTERECURSO", anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = " and CODIGO = 'OP00066' ";

        System.out.println("INICIANDO IMPORTAÇÃO ORDENS PAGTO: " + anoSonner);
        try {
            stmt = con.prepareStatement(
                    "select DSP_NRO, DSP_OPE_COD, DSP_DTA, DSP_HIS, DSP_VLR " +
                            " from DESPESAS_EXTRA " +
                            " where DSP_EXE = ? " + teste +
                            " Order by 1 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                nroOP = rs.getInt(1);
                ficha = rs.getInt(2);
                data = rs.getDate(3);
                historico = rs.getString(4).trim();
                valorOP = rs.getBigDecimal(5);

                System.out.println("Ano: " + anoSonner + " - OP: " + nroOP);

                OrdensPagto ordensPagto = new OrdensPagto(anoAtual, nroOP, ficha, 0, data, historico, valorOP, data, BigDecimal.ZERO, null, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO);
                emLocal.persist(ordensPagto);

                PagtoOP pagtoOP = new PagtoOP(anoAtual, nroOP, null, BigDecimal.ZERO);
                emLocal.persist(pagtoOP);

                OPFonteRecurso opFonteRecurso = new OPFonteRecurso(anoAtual, nroOP, 1, 1, 110, 0, valorOP);
                emLocal.persist(opFonteRecurso);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Ops");
            e.printStackTrace();
        } finally {
            closeConexao(con, stmt, rs);
            System.out.println("Acabou");
        }
    }
}
