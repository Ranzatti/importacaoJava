package _Infra;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.*;
import java.util.Date;

public class Util{

    private static EntityManagerFactory emfLocal;
    private static EntityManager emLocal;

    public Util() {
    }

    public static EntityManager conexaoDestino(String persistence) {
        emfLocal = Persistence.createEntityManagerFactory(persistence);
        emLocal = emfLocal.createEntityManager();
        return emLocal;
    }


    // Conexão com o banco de Dados do SFI ( Cidades.Louveira )
    public static Connection conexaoOrigemOracle() {

        Connection conAux;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conAux = DriverManager.getConnection("jdbc:oracle:thin:@10.1.1.102:1521:ORCL", "SFI", "sicsadm");
            return conAux;
        } catch (Exception ex) {
            System.err.println("Erro de conexão no banco" + ex);
            return null;
        }
    }
    public static Connection conexaoOrigemSQLServer() {

        Connection conAux;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conAux = DriverManager.getConnection("jdbc:sqlserver://10.1.1.139:1433;DatabaseName=mg_trescoracoes_pm_eldados;user=sa;Password=Passw0rd");
            return conAux;
        } catch (Exception ex) {
            System.err.println("Erro de conexão no banco" + ex);
            return null;
        }
    }

    public static void closeConexao(Connection con, PreparedStatement stmt, ResultSet rs) {
        try {
            con.close();
            stmt.close();
            rs.close();

            if (emLocal.isOpen()) {
                emLocal.getTransaction().commit();
            }
            emLocal.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // até aqui

    public static void delete(String tabela, Date ano) {
        emLocal.getTransaction().begin();
        emLocal.createNativeQuery("DELETE FROM " + tabela + " Where ANO = :ano ")
                .setParameter("ano", ano)
                .executeUpdate();
        emLocal.getTransaction().commit();
    }

    public static void deleteQuery(String query, Date ano) {
        emLocal.getTransaction().begin();
        emLocal.createNativeQuery(query)
                .setParameter("ano", ano)
                .executeUpdate();
        emLocal.getTransaction().commit();
    }

    public static void delete(String tabela) {
        emLocal.getTransaction().begin();
        emLocal.createNativeQuery("DELETE FROM " + tabela)
                .executeUpdate();
        emLocal.getTransaction().commit();
    }


}
