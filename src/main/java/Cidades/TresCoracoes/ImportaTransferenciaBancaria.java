package Cidades.TresCoracoes;

import _Entity.Debito;
import _Entity.Deposito;
import _Entity.Transferencia;
import _Infra.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ImportaTransferenciaBancaria extends Util {

    public static void main(String[] args) {
        init(2020);
    }

    public static void init(int anoSonner) {

        EntityManager emLocal = conexaoDestino("TresCoracoes");

        Connection con = conexaoOrigemSQLServer();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String teste, historico, contaDebito, contaCredito, agenciaDebito, agenciaCredito;
        Integer seqContaMovimento, seqContaDebito, seqContaCredito, fichaDebito, fichaCredito, bancoDebito, bancoCredito, fonteRecursoDebito, fonteRecursoCredito, transf, versaoRecurso, max;
        BigDecimal valor;
        Date anoAtual, data;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        versaoRecurso = getVersao(anoSonner);

        deleteQuery("Delete from CBPDEBITO where ANOLANCTO = :ano and TRANSFERENCIA > 0 ", anoAtual);
        deleteQuery("Delete from CBPDEPOSITO where ANOLANCTO = :ano and TRANSFERENCIA > 0 ", anoAtual);
        delete("CBPTRANSFERENCIA", anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //  teste = "and SEQ_CT_CONTA_MOVIMENTO = 141933";

        System.out.println("INICIANDO IMPORTAÇÃO TRANSFERÊNCIA BANCÁRIA: " + anoSonner);
        try {
            stmt = con.prepareStatement(
                    "SELECT" +
                            "    SEQ_CT_CONTA_MOVIMENTO, " +
                            "    SEQ_CT_CONTA_DEBITO, " +
                            "    CASE WHEN COD_BANCO_DEBITO = 'CEF' then '104' ELSE COD_BANCO_DEBITO END as COD_BANCO_DEBITO, " +
                            "    NRO_CONTA_DEBITO, " +
                            "    NRO_CONTA_AGENCIA_DEBITO, " +
                            "    COD_FONTE_RECURSO_DEBITO, " +
                            "    SEQ_CT_CONTA_CREDITO, " +
                            "    CASE WHEN COD_BANCO_CREDITO = 'CEF' then '104' ELSE COD_BANCO_CREDITO END as COD_BANCO_CREDITO, " +
                            "    NRO_CONTA_CREDITO, " +
                            "    NRO_CONTA_AGENCIA_CREDITO, " +
                            "    COD_FONTE_RECURSO_CREDITO, " +
                            "    DAT_CONTA_MOVIMENTO, " +
                            "    HST_CONTA_MOVIMENTO, " +
                            "    VLR_CONTA_MOVIMENTO " +
                            "FROM" +
                            "    VW_CT_CONTA_MOVIMENTO " +
                            "WHERE " +
                            "    SEQ_CT_UNIDADE_GESTORA = 21 " +
                            "AND ANO_CONTA_MOVIMENTO = ? " + teste +
                            "AND SBL_CONTA_MOVIMENTO_TIPO = 'TR' ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                seqContaMovimento = rs.getInt(1);
                seqContaDebito = rs.getInt(2);
                bancoDebito = rs.getInt(3);
                contaDebito = rs.getString(4);
                agenciaDebito = rs.getString(5);
                fonteRecursoDebito = rs.getInt(6);
                seqContaCredito = rs.getInt(7);
                bancoCredito = rs.getInt(8);
                contaCredito = rs.getString(9);
                agenciaCredito = rs.getString(10);
                fonteRecursoCredito = rs.getInt(11);
                data = rs.getDate(12);
                historico = rs.getString(13).trim().toUpperCase();
                valor = rs.getBigDecimal(14);

                System.out.println("Transferencia: [ " + seqContaMovimento + " ]");

                transf = getMaxTransferencia(emLocal, anoAtual);

                Transferencia transferencia = new Transferencia(anoAtual, transf);
                emLocal.persist(transferencia);

                fichaCredito = getFichaConta(con, seqContaCredito);

                max = getMaxDebito(emLocal, fichaCredito, data);

                Debito debito = new Debito();
                debito.getId().setFichaConta(fichaCredito);
                debito.getId().setData(data);
                debito.getId().setNumero(max);
                debito.setBanco(bancoCredito);
                debito.setAgencia(agenciaCredito);
                debito.setConta(contaCredito);
                debito.setHistorico(historico);
                debito.setAnoLancto(anoAtual);
                debito.setLancamento(-1);
                debito.setTransferencia(transf);
                debito.setFinalidade("T");
                debito.setValor(valor);
                debito.setVersaoRecurso(versaoRecurso);
                debito.setFonteRecurso(fonteRecursoCredito);
                debito.setCaFixo(999);
                debito.setCaVariavel(0);
                emLocal.persist(debito);

                // Contra Partida
                fichaDebito = getFichaConta(con, seqContaDebito);

                max = getMaxDeposito(emLocal, fichaDebito, data);

                Deposito deposito = new Deposito();
                deposito.getId().setFichaConta(fichaDebito);
                deposito.getId().setData(data);
                deposito.getId().setNumero(max);
                deposito.setBanco(bancoDebito);
                deposito.setAgencia(agenciaDebito);
                deposito.setConta(contaDebito);
                deposito.setHistorico(historico);
                deposito.setAnoLancto(anoAtual);
                deposito.setLancamento(-1);
                deposito.setTransferencia(transf);
                deposito.setOrigem("T");
                deposito.setValor(valor);
                deposito.setVersaoRecurso(versaoRecurso);
                deposito.setFonteRecurso(fonteRecursoDebito);
                deposito.setCaFixo(999);
                deposito.setCaVariavel(0);
                emLocal.persist(deposito);
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

    private static Integer getFichaConta(Connection con, Integer seqConta) throws SQLException {
        PreparedStatement stmt2;
        ResultSet rs2;
        Integer fichaConta;
        stmt2 = con.prepareStatement(
                "SELECT " +
                        "    NRO_CONTA_REDUZIDO " +
                        "FROM " +
                        "    CT_CONTA " +
                        "WHERE " +
                        "    SEQ_CT_CONTA = ? ");
        stmt2.setInt(1, seqConta);
        rs2 = stmt2.executeQuery();
        rs2.next();
        fichaConta = rs2.getInt(1);
        stmt2.close();
        rs2.close();
        return fichaConta;
    }

    private static int getMaxTransferencia(EntityManager em, Date ano) {
        Query q = em.createQuery("Select MAX(a.id.sequencial) from Transferencia a where a.id.ano = :ano ")
                .setParameter("ano", ano);
        Integer max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }

    private static int getMaxDebito(EntityManager em, Integer fichaConta, Date data) {
        Query q = em.createQuery("Select MAX(a.id.numero) from Debito a where a.id.fichaConta = :fichaConta and a.id.data = :data ")
                .setParameter("fichaConta", fichaConta)
                .setParameter("data", data);
        Integer max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }

    private static int getMaxDeposito(EntityManager em, Integer fichaConta, Date data) {
        Query q = em.createQuery("Select MAX(a.id.numero) from Deposito a where a.id.fichaConta = :fichaConta and a.id.data = :data ")
                .setParameter("fichaConta", fichaConta)
                .setParameter("data", data);
        Integer max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }


}
