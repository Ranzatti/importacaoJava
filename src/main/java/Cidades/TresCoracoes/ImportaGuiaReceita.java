package Cidades.TresCoracoes;

import _Entity.*;
import _Infra.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ImportaGuiaReceita extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {

        final String ORCAMENTARIA = "O";
        final String EXTRAORCAMENTARIA = "E";

        EntityManager emLocal = conexaoDestino("TresCoracoes");

        Connection con = conexaoOrigemSQLServer();
        PreparedStatement stmt = null;
        PreparedStatement stmt2;
        ResultSet rs = null;
        ResultSet rs2;

        Integer guia, fichaReceita, fichaBanco, versaoRecurso, fonteRecurso, banco, receitaTipo;
        Date anoAtual, dataGuia, recebimento;
        String teste, numeroDocumento, receita, tipoReceita, historico, agencia, contaBancaria;
        BigDecimal valor;
        Boolean orcamentaria, extraOrcamentaria, lancamentoDedutora;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        versaoRecurso = getVersao(anoSonner);

        delete(emLocal, anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and NRO_DOCUMENTO = '992' ";

        orcamentaria = false;
        extraOrcamentaria = false;
        lancamentoDedutora = true;

        try {
            // RECEITA ORÇAMENTARIA
            if (orcamentaria) {
                System.out.println("INICIANDO IMPORTAÇÃO GUIA RECEITA ORÇAMENTÁRIA: " + anoSonner);
                receitaTipo = 1;
                tipoReceita = ORCAMENTARIA;
                stmt = con.prepareStatement(
                        "SELECT distinct " +
                                "    NRO_DOCUMENTO, " +
                                "    DAT_DOCUMENTO, " +
                                "    DAT_DOCUMENTO_BANCO, " +
                                "    HST_DOCUMENTO " +
                                "FROM " +
                                "    VW_CT_ARRECADACAO_INTEGRACAO " +
                                "WHERE " +
                                "    ANO_DOCUMENTO = ? " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                                "AND SBL_RECEITA_TIPO = 1 " + teste +
                                "ORDER BY 1 ");
                stmt.setInt(1, anoSonner);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    numeroDocumento = rs.getString(1);
                    dataGuia = rs.getDate(2);
                    recebimento = rs.getDate(3);
                    historico = rs.getString(4);

                    guia = Integer.parseInt(numeroDocumento);

                    System.out.println("Guia: " + numeroDocumento);

                    GuiaReceita guiaReceita = new GuiaReceita(anoAtual, tipoReceita, guia, 1, dataGuia, dataGuia, recebimento, historico, "DIG", null);
                    emLocal.persist(guiaReceita);

                    // Itens da Guia
                    itens(emLocal, con, receitaTipo, anoSonner, tipoReceita, guia, versaoRecurso, numeroDocumento);

                    // Rec conta banco
                    recContaBanco(emLocal, con, receitaTipo, anoSonner, tipoReceita, guia, versaoRecurso, numeroDocumento);

                    // Financeiro
                    financeiro(emLocal, con, receitaTipo, anoSonner, tipoReceita, guia, versaoRecurso, numeroDocumento, recebimento);
                }
                stmt.close();
                rs.close();
            }

            // RECEITA EXTRA-ORÇAMENTARIA
            if (extraOrcamentaria) {
                System.out.println("INICIANDO IMPORTAÇÃO GUIA RECEITA EXTRA-ORÇAMENTÁRIA: " + anoSonner);
                receitaTipo = 3;
                tipoReceita = EXTRAORCAMENTARIA;
                stmt = con.prepareStatement(
                        "SELECT distinct " +
                                "    NRO_DOCUMENTO, " +
                                "    DAT_DOCUMENTO, " +
                                "    DAT_DOCUMENTO_BANCO, " +
                                "    HST_DOCUMENTO " +
                                "FROM " +
                                "    VW_CT_ARRECADACAO_INTEGRACAO " +
                                "WHERE " +
                                "    ANO_DOCUMENTO = ? " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                                "AND SBL_RECEITA_TIPO = 3 " + teste +
                                "ORDER BY 1 ");
                stmt.setInt(1, anoSonner);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    numeroDocumento = rs.getString(1);
                    dataGuia = rs.getDate(2);
                    recebimento = rs.getDate(3);
                    historico = rs.getString(4);

                    historico = historico.length() > 250 ? historico.substring(0, 250) : historico;

                    guia = Integer.parseInt(numeroDocumento);

                    System.out.println("Guia: " + numeroDocumento);

                    GuiaReceita guiaReceita = new GuiaReceita(anoAtual, tipoReceita, guia, 1, dataGuia, dataGuia, recebimento, historico, "DIG", null);
                    emLocal.persist(guiaReceita);

                    // Itens da Guia
                    itens(emLocal, con, receitaTipo, anoSonner, tipoReceita, guia, versaoRecurso, numeroDocumento);

                    // Rec conta banco
                    recContaBanco(emLocal, con, receitaTipo, anoSonner, tipoReceita, guia, versaoRecurso, numeroDocumento);

                    // Financeiro
                    financeiro(emLocal, con, receitaTipo, anoSonner, tipoReceita, guia, versaoRecurso, numeroDocumento, recebimento);
                }
                stmt.close();
                rs.close();
            }

            // LANÇAMENTO RECEITA DEDUTORA
            if (lancamentoDedutora) {
                System.out.println("INICIANDO IMPORTAÇÃO GUIA RECEITA ORÇAMENTÁRIA: " + anoSonner);
                receitaTipo = 2;
                tipoReceita = ORCAMENTARIA;
                stmt = con.prepareStatement(
                        "SELECT distinct " +
                                "    NRO_DOCUMENTO, " +
                                "    DAT_DOCUMENTO, " +
                                "    DAT_DOCUMENTO_BANCO, " +
                                "    HST_DOCUMENTO " +
                                "FROM " +
                                "    VW_CT_ARRECADACAO_INTEGRACAO " +
                                "WHERE " +
                                "    ANO_DOCUMENTO = ? " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                                "AND SBL_RECEITA_TIPO = 2 " + teste +
                                "ORDER BY 1 ");
                stmt.setInt(1, anoSonner);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    numeroDocumento = rs.getString(1);
                    dataGuia = rs.getDate(2);
                    recebimento = rs.getDate(3);
                    historico = rs.getString(4);

                    guia = Integer.parseInt(numeroDocumento);

                    System.out.println("Anulacao: " + numeroDocumento);

                    AnulaReceita anulaReceita = new AnulaReceita(anoAtual, tipoReceita, guia, dataGuia, historico, 2);
                    emLocal.persist(anulaReceita);

                    // Itens da Guia
                    itens(emLocal, con, receitaTipo, anoSonner, tipoReceita, guia, versaoRecurso, numeroDocumento);

                    // Rec conta banco
                    recContaBanco(emLocal, con, receitaTipo, anoSonner, tipoReceita, guia, versaoRecurso, numeroDocumento);

                    // Financeiro
                    financeiro(emLocal, con, receitaTipo, anoSonner, tipoReceita, guia, versaoRecurso, numeroDocumento, recebimento);
                }
                stmt.close();
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Ops");
            e.printStackTrace();
        } finally {
            System.out.println("Acabou Mestre");
            closeConexao(con, stmt, rs);
        }
    }

    private static void financeiro(EntityManager emLocal, Connection con, Integer receitaTipo, Integer anoSonner, String tipoReceita, Integer guia, Integer versaoRecurso, String numeroDocumento, Date recebimento) throws SQLException {
        ResultSet rs2;
        PreparedStatement stmt2;
        String agencia;
        String contaBancaria;
        Integer banco;
        Integer fonteRecurso;
        Integer fichaBanco;
        BigDecimal valor;
        Query q;

        Date anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        stmt2 = con.prepareStatement(
                "SELECT " +
                        "    NRO_CONTA_REDUZIDA, " +
                        "    COD_BANCO, " +
                        "    NRO_CONTA_AGENCIA, " +
                        "    NRO_CONTA, " +
                        "    COD_FONTE_RECURSO, " +
                        "    SUM(VLR_DOCUMENTO) " +
                        "FROM " +
                        "    VW_CT_ARRECADACAO_INTEGRACAO " +
                        "WHERE " +
                        "    ANO_DOCUMENTO = ? " +
                        "AND NRO_DOCUMENTO = ? " +
                        "AND SBL_RECEITA_TIPO = ? " +
                        "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                        "GROUP BY " +
                        "    NRO_CONTA_REDUZIDA, " +
                        "    COD_BANCO, " +
                        "    NRO_CONTA_AGENCIA, " +
                        "    NRO_CONTA, " +
                        "    COD_FONTE_RECURSO  " +
                        "ORDER BY 1 ");
        stmt2.setInt(1, anoSonner);
        stmt2.setString(2, numeroDocumento);
        stmt2.setInt(3, receitaTipo);
        rs2 = stmt2.executeQuery();
        while (rs2.next()) {
            fichaBanco = rs2.getInt(1);
            banco = rs2.getInt(2);
            agencia = rs2.getString(3);
            contaBancaria = rs2.getString(4);
            fonteRecurso = rs2.getInt(5);
            valor = rs2.getBigDecimal(6);

            if (receitaTipo == 1 || receitaTipo == 3) {
                q = emLocal.createQuery("Select MAX(a.id.numero) From Credito a Where a.id.fichaConta = :ficha And a.id.data = :data ");
            } else {
                q = emLocal.createQuery("Select MAX(a.id.numero) from Debito a where a.id.fichaConta = :ficha  and a.id.data = :data ");
            }
            q.setParameter("ficha", fichaBanco);
            q.setParameter("data", recebimento);
            Integer max = (Integer) q.getSingleResult();
            max = (max == null) ? 0 : max;
            max++;

            if (receitaTipo == 1 || receitaTipo == 3) {
                Credito credito = new Credito();
                credito.getId().setFichaConta(fichaBanco);
                credito.getId().setData(recebimento);
                credito.getId().setNumero(max);
                credito.setBanco(banco);
                credito.setAgencia(agencia);
                credito.setConta(contaBancaria);
                credito.setHistorico("Recebimento de Guia de Receita");
                credito.setAnoLancto(anoAtual);
                credito.setLancamento(-1);
                credito.setTipoGuia(tipoReceita);
                credito.setGuia(guia);
                credito.setValor(valor);
                credito.setVersaoRecurso(versaoRecurso);
                credito.setFonteRecurso(fonteRecurso);
                credito.setCaFixo(999);
                credito.setCaVariavel(0);
                emLocal.persist(credito);
            } else {
                Debito debito = new Debito();
                debito.getId().setFichaConta(fichaBanco);
                debito.getId().setData(recebimento);
                debito.getId().setNumero(max);
                debito.setBanco(banco);
                debito.setAgencia(agencia);
                debito.setConta(contaBancaria);
                debito.setHistorico("Lançamento Receita Dedutora");
                debito.setAnoLancto(anoAtual);
                debito.setLancamento(-1);
                debito.setTipoAnulacaoRec(tipoReceita);
                debito.setAnulacaoReceita(guia);
                debito.setFinalidade("P");
                debito.setValor(valor);
                debito.setVersaoRecurso(versaoRecurso);
                debito.setFonteRecurso(fonteRecurso);
                debito.setCaFixo(999);
                debito.setCaVariavel(0);
                emLocal.persist(debito);
            }
        }
        stmt2.close();
        rs2.close();
    }

    private static void recContaBanco(EntityManager emLocal, Connection con, Integer receitaTipo, Integer anoSonner, String tipoReceita, Integer guia, Integer versaoRecurso, String numeroDocumento) throws SQLException {
        PreparedStatement stmt2;
        ResultSet rs2;
        BigDecimal valor;
        String agencia;
        String contaBancaria;
        String receita;
        Integer banco;
        Integer fichaReceita;
        Integer fichaBanco;
        Integer fonteRecurso;

        Date anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        int i = 1;
        stmt2 = con.prepareStatement(
                "SELECT " +
                        "    COD_PLANO_CONTA, " +
                        "    COD_FONTE_RECURSO, " +
                        "    NRO_CONTA_REDUZIDA, " +
                        "    COD_BANCO, " +
                        "    NRO_CONTA_AGENCIA, " +
                        "    NRO_CONTA, " +
                        "    SUM(VLR_DOCUMENTO) " +
                        "FROM " +
                        "    VW_CT_ARRECADACAO_INTEGRACAO " +
                        "WHERE " +
                        "    ANO_DOCUMENTO = ? " +
                        "AND NRO_DOCUMENTO = ? " +
                        "AND SBL_RECEITA_TIPO = ? " +
                        "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                        "GROUP BY " +
                        "    COD_PLANO_CONTA, " +
                        "    COD_FONTE_RECURSO, " +
                        "    NRO_CONTA_REDUZIDA, " +
                        "    COD_BANCO, " +
                        "    NRO_CONTA_AGENCIA, " +
                        "    NRO_CONTA " +
                        "ORDER BY 1 ");
        stmt2.setInt(1, anoSonner);
        stmt2.setString(2, numeroDocumento);
        stmt2.setInt(3, receitaTipo);
        rs2 = stmt2.executeQuery();
        while (rs2.next()) {
            receita = rs2.getString(1);
            fonteRecurso = rs2.getInt(2);
            fichaBanco = rs2.getInt(3);
            banco = rs2.getInt(4);
            agencia = rs2.getString(5);
            contaBancaria = rs2.getString(6);
            valor = rs2.getBigDecimal(7);

            fichaReceita = getFichaReceita(con, anoSonner, tipoReceita, receita);

            if (receitaTipo == 1 || receitaTipo == 3) {
                RecContaBanco recContaBanco = new RecContaBanco(anoAtual, tipoReceita, guia, fichaReceita, versaoRecurso, fonteRecurso, 999, 0, fichaBanco, versaoRecurso, fonteRecurso, 999, 0, i, valor);
                emLocal.persist(recContaBanco);
            } else {
                AnulaRecContaBanco anulaRecContaBanco = new AnulaRecContaBanco(anoAtual, tipoReceita, guia, fichaReceita, versaoRecurso, fonteRecurso, 999, 0, fichaBanco, versaoRecurso, fonteRecurso, 999, 0, i, valor);
                emLocal.persist(anulaRecContaBanco);
            }

            i++;
        }
        stmt2.close();
        rs2.close();
    }

    private static void itens(EntityManager emLocal, Connection con, Integer receitaTipo, Integer anoSonner, String tipoReceita, Integer guia, Integer versaoRecurso, String numeroDocumento) throws SQLException {
        PreparedStatement stmt2;
        ResultSet rs2;
        String receita;
        BigDecimal valor;
        Integer fonteRecurso;
        Integer fichaReceita;

        Date anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        stmt2 = con.prepareStatement(
                "SELECT " +
                        "    COD_PLANO_CONTA, " +
                        "    COD_FONTE_RECURSO, " +
                        "    SUM(VLR_DOCUMENTO) " +
                        "FROM " +
                        "    VW_CT_ARRECADACAO_INTEGRACAO " +
                        "WHERE " +
                        "    ANO_DOCUMENTO = ? " +
                        "AND NRO_DOCUMENTO = ? " +
                        "AND SBL_RECEITA_TIPO = ? " +
                        "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                        "GROUP BY " +
                        "    COD_PLANO_CONTA, " +
                        "    COD_FONTE_RECURSO " +
                        "ORDER BY 1 ");
        stmt2.setInt(1, anoSonner);
        stmt2.setString(2, numeroDocumento);
        stmt2.setInt(3, receitaTipo);
        rs2 = stmt2.executeQuery();
        while (rs2.next()) {
            receita = rs2.getString(1);
            fonteRecurso = rs2.getInt(2);
            valor = rs2.getBigDecimal(3);

            fichaReceita = getFichaReceita(con, anoSonner, tipoReceita, receita);

            if (receitaTipo == 1 || receitaTipo == 3) {

                receita = tipoReceita.equals("E") ? Integer.toString(fichaReceita) : receita;

                ItensGuiaReceita itensGuiaReceita = new ItensGuiaReceita(anoAtual, tipoReceita, guia, fichaReceita, versaoRecurso, fonteRecurso, 999, 0, receita, valor);
                emLocal.persist(itensGuiaReceita);
            } else {
                AnulaRecItens anulaRecItens = new AnulaRecItens(anoAtual, tipoReceita, guia, fichaReceita, versaoRecurso, fonteRecurso, 999, 0, valor);
                emLocal.persist(anulaRecItens);
            }
        }
        stmt2.close();
        rs2.close();
    }

    private static Integer getFichaReceita(Connection con, Integer anoAtual, String tipoReceita, String receita) throws SQLException {
        ResultSet rs3;
        PreparedStatement stmt3;

        Integer fichaReceita = 0;

        if (tipoReceita.equals("O")) {
            stmt3 = con.prepareStatement(
                    "SELECT " +
                            "    NRO_ORCAMENTO_RECEITA_FICHA " +
                            "FROM " +
                            "    VW_CT_ORCAMENTO_RECEITA " +
                            "WHERE " +
                            "    ANO_ORCAMENTO_RECEITA = ? " +
                            "AND COD_PLANO_CONTA = ? " +
                            "AND SEQ_CT_UNIDADE_GESTORA = 21");
        } else {
            stmt3 = con.prepareStatement(
                    "SELECT " +
                            "    SEQ_CT_PLANO_CONTA " +
                            "FROM " +
                            "    CT_PLANO_CONTA " +
                            "WHERE " +
                            "    SBL_PLANO_CONTA_SISTEMA = 3 " +
                            "AND ? BETWEEN ANO_PLANO_CONTA_INICIO AND ANO_PLANO_CONTA_FIM " +
                            "AND COD_PLANO_CONTA = ? ");
        }
        stmt3.setInt(1, anoAtual);
        stmt3.setString(2, receita);
        rs3 = stmt3.executeQuery();
        rs3.next();
        fichaReceita = rs3.getInt(1);
        rs3.close();
        stmt3.close();
        return fichaReceita;
    }

    public static void delete(EntityManager em, Date ano) {

        deleteQuery("Delete from CBPDEBITO A WHERE A.ANOLANCTO = :ano AND EXISTS ( SELECT * FROM CBPANULARECEITAS B WHERE A.ANOLANCTO = B.ANO AND A.TIPOANULACAOREC = B.TIPO AND A.ANULACAORECEITA = B.ANULACAO AND B.DEDUTORA = 2 )", ano);

        deleteQuery("Delete from CBPANULARECITENS A Where A.ANO = :ano AND EXISTS ( SELECT * from CBPANULARECEITAS B Where A.ANO = B.ANO and A.TIPO = B.TIPO AND A.ANULACAO = B.ANULACAO AND B.DEDUTORA = 2 ) ", ano);

        deleteQuery("DELETE from CBPANULRECCONTABAN A Where A.ANO = :ano AND EXISTS ( SELECT * from CBPANULARECEITAS B Where A.ANO = B.ANO and A.TIPO = B.TIPO AND A.ANULACAO = B.ANULACAO AND B.DEDUTORA = 2 ) ", ano);

        deleteQuery("Delete from CBPANULARECEITAS Where ANO = :ano and DEDUTORA = 2", ano);

        deleteQuery("Delete from CBPCREDITO Where ANOLANCTO = :ano and GUIA > 0 ", ano);

        deleteQuery("Delete from CBPENTRADASCAIXAS Where ANOLANCTO = :ano and GUIA > 0 ", ano);

        deleteQuery("Delete from CBPITENSGUIA A Where A.ANO = :ano " +
                " AND EXISTS ( SELECT * from CBPGUIARECEITA B Where A.ANO = B.ANO AND A.TIPO = B.TIPO AND A.GUIA = B.NUMERO AND B.ORIGEM = 'DIG' )", ano);

        deleteQuery("Delete from CBPRECCONTABANCO A Where A.ANO = :ano " +
                " AND EXISTS ( SELECT * from CBPGUIARECEITA B Where A.ANO = B.ANO AND A.TIPO = B.TIPO AND A.GUIA = B.NUMERO AND B.ORIGEM = 'DIG' )", ano);

        deleteQuery("Delete from CBPGUIARECEITA Where ANO = :ano and ORIGEM = 'DIG' ", ano);

    }
}
