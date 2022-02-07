package Cidades.TresCoracoes;

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

        EntityManager emLocal = conexaoDestino("TresCoracoes");

        Connection con = conexaoOrigemSQLServer();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        Integer seqConta, bancoCodigo, ficha, fonteRecurso, versaoRecurso, anoFonte;
        String agenciaCodigo, conta, nome, dv, tipo, titular;
        Date abertura, encerramento, anoAtual;

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
                    "SELECT " +
                            "    COD_BANCO, " +
                            "    NOM_BANCO " +
                            "FROM " +
                            "    dbo.CT_BANCO " +
                            "WHERE " +
                            "    COD_BANCO >= '001' " +
                            "AND COD_BANCO < '999' ");
            rs = stmt.executeQuery();
            while (rs.next()) {
                bancoCodigo = rs.getInt(1);
                nome = rs.getString(2).trim().toUpperCase();

                nome = nome.length() > 50 ? nome.substring(0, 50) : nome;

                Banco banco = emLocal.find(Banco.class, bancoCodigo);

                if (Objects.isNull(banco)) {
                    banco = new Banco();
                    banco.setCodigo(bancoCodigo);
                    banco.setNome(nome);
                    emLocal.persist(banco);
                }
            }
            stmt.close();
            rs.close();

            // Agencias
            stmt = con.prepareStatement(
                    "SELECT " +
                            "    COD_BANCO, " +
                            "    NRO_AGENCIA, " +
                            "    NOM_AGENCIA " +
                            "FROM " +
                            "    dbo.CT_AGENCIA a " +
                            "JOIN " +
                            "    dbo.CT_BANCO b " +
                            "ON " +
                            "    ( a.SEQ_GG_BANCO = b.SEQ_GG_BANCO)");
            rs = stmt.executeQuery();
            while (rs.next()) {
                bancoCodigo = rs.getInt(1);
                agenciaCodigo = rs.getString(2).trim();
                nome = rs.getString(3).trim().toUpperCase();

                nome = nome.length() > 50 ? nome.substring(0, 50) : nome;

                Agencia agencia = emLocal.find(Agencia.class, new AgenciaPK(bancoCodigo, agenciaCodigo));
                if (Objects.isNull(agencia)) {
                    agencia = new Agencia();
                    agencia.getId().setBanco(bancoCodigo);
                    agencia.getId().setCodigo(agenciaCodigo);
                    agencia.setNome(nome);
                    emLocal.persist(agencia);
                }
            }
            stmt.close();
            rs.close();

            // Contas Bancárias
            stmt = con.prepareStatement(
                    "SELECT " +
                            "    SEQ_CT_CONTA, " +
                            "    CASE " +
                            "          WHEN COD_BANCO = 'CEF' then '104' " +
                            "          ELSE COD_BANCO END as COD_BANCO, " +
                            "    NRO_CONTA_AGENCIA, " +
                            "    NRO_CONTA, " +
                            "    NRO_CONTA_REDUZIDO, " +
                            "    NOM_CONTA, " +
                            "    SBL_CONTA_TIPO, " +
                            "    DAT_CONTA_CRIACAO, " +
                            "    DAT_CONTA_INATIVACAO, " +
                            "    NOM_UNIDADE_GESTORA " +
                            "FROM " +
                            "    VW_CT_CONTA " +
                            "WHERE " +
                            "    SEQ_CT_UNIDADE_GESTORA = 21 " +
                            "ORDER by NRO_CONTA_REDUZIDO");
            rs = stmt.executeQuery();
            while (rs.next()) {
                seqConta = rs.getInt(1);
                bancoCodigo = rs.getInt(2);
                agenciaCodigo = rs.getString(3).trim();
                conta = rs.getString(4).trim();
                ficha = rs.getInt(5);
                nome = rs.getString(6).trim().toUpperCase();
                tipo = rs.getString(7);
                abertura = rs.getDate(8);
                encerramento = rs.getDate(9);
                titular = rs.getString(10);

                nome = nome.length() > 250 ? nome.substring(0, 250) : nome;
                titular = titular.length() > 50 ? titular.substring(0, 50) : titular;


                if (encerramento.after(java.sql.Date.valueOf("9999-01-01"))) {
                    encerramento = null;
                }

                System.out.println("Ficha: " + ficha);

                switch (tipo) {
                    case "11":
                        tipo = "M";
                        break;
                    case "12":
                    case "13":
                    case "14":
                    case "15":
                        tipo = "V";
                        break;
                    case "16":
                        tipo = "A";
                        break;
                    default:
                        tipo = "";
                        break;
                }

                ContasBancarias contas = emLocal.find(ContasBancarias.class, ficha);

                if (Objects.isNull(contas)) {
                    ContasBancarias contasBancarias = new ContasBancarias();
                    contasBancarias.setFicha(ficha);
                    contasBancarias.setBanco(bancoCodigo);
                    contasBancarias.setAgencia(agenciaCodigo);
                    contasBancarias.setCodigo(conta);
                    contasBancarias.setNome(nome);
                    contasBancarias.setTipoConta(tipo);
                    contasBancarias.setTitular(titular);
                    contasBancarias.setBancoAudesp(bancoCodigo);
                    contasBancarias.setAgenciaAudesp(agenciaCodigo);
                    contasBancarias.setContaAudesp(conta);
                    contasBancarias.setEmpresa(5);
                    contasBancarias.setEncerramento(encerramento);
                    contasBancarias.setAbertura(abertura);
                    emLocal.persist(contasBancarias);

                    // Fonte Recurso
                    stmt2 = con.prepareStatement(
                            "SELECT " +
                                    "    ANO_CONTA_FONTE_RECURSO, " +
                                    "    B.COD_FONTE_RECURSO " +
                                    "FROM " +
                                    "    dbo.CT_CONTA_FONTE_RECURSO A " +
                                    "JOIN " +
                                    "    dbo.CT_FONTE_RECURSO B " +
                                    "ON " +
                                    "    (A.SEQ_CT_FONTE_RECURSO = B.SEQ_CT_FONTE_RECURSO) " +
                                    "WHERE " +
                                    "    SEQ_CT_CONTA = ? " +
                                    "ORDER BY 1, 2 ");
                    stmt2.setInt(1, seqConta);
                    rs2 = stmt2.executeQuery();
                    while (rs2.next()) {
                        anoFonte = rs2.getInt(1);
                        fonteRecurso = rs2.getInt(2);

                        versaoRecurso = getVersao(anoFonte);

                        System.out.println("Conta: " + conta + " - Fonte: " + fonteRecurso);

                        ContasFonteRecurso contasFonteRecurso = emLocal.find(ContasFonteRecurso.class, new ContasFonteRecursoPK(ficha, versaoRecurso, fonteRecurso));
                        if (Objects.isNull(contasFonteRecurso)) {
                            contasFonteRecurso = new ContasFonteRecurso();
                            contasFonteRecurso.getId().setFicha(ficha);
                            contasFonteRecurso.getId().setVersaoRecurso(versaoRecurso);
                            contasFonteRecurso.getId().setFonteRecurso(fonteRecurso);
                            contasFonteRecurso.setSaldoInicial(BigDecimal.ZERO);
                            emLocal.persist(contasFonteRecurso);
                        }

                        ContasCA contasCA = emLocal.find(ContasCA.class, new ContasCAPK(ficha, versaoRecurso, fonteRecurso, 999, 0));
                        if (Objects.isNull(contasCA)) {
                            contasCA = new ContasCA();
                            contasCA.getId().setFicha(ficha);
                            contasCA.getId().setVersaoRecurso(versaoRecurso);
                            contasCA.getId().setFonteRecurso(fonteRecurso);
                            contasCA.getId().setCaFixo(999);
                            contasCA.getId().setCaVariavel(0);
                            contasCA.setSaldoInicial(BigDecimal.ZERO);
                            emLocal.persist(contasCA);
                        }
                    }

                    // ContaCXPlanoC
                    for (int i = 2015; i <= 2021; i++) {
                        anoAtual = java.sql.Date.valueOf(i + "-01-01");
                        ContasCXPlanoC contasCXPlanoC = new ContasCXPlanoC();
                        contasCXPlanoC.getId().setAno(anoAtual);
                        contasCXPlanoC.getId().setFichaConta(ficha);
                        contasCXPlanoC.setBanco(bancoCodigo);
                        contasCXPlanoC.setAgencia(agenciaCodigo);
                        contasCXPlanoC.setConta(conta);
                        contasCXPlanoC.setFichaPC(13);
                        contasCXPlanoC.setFichaPCAplic(18);
                        emLocal.persist(contasCXPlanoC);
                    }
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
