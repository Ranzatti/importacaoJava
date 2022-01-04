package Cidades.Louveira;

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

public class ImportaAnulacaoReceita extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {

        EntityManager emLocal = conexaoDestino("louveira");

        Connection con = conexaoOrigemOracle();
        PreparedStatement stmt = null;
        PreparedStatement stmt2, stmt3;
        ResultSet rs = null;
        ResultSet rs2, rs3;

        int fichaReceita, fichaBanco, anulacao, fonteRecurso, caFixo, caVariavel, tipoContaBancaria;
        Date anoAtual, dataGuia;
        String teste, tipoReceita, codAplicacao, tipoFinanceiro;
        BigDecimal valor;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete(emLocal, anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and AAR_DTA = '04-01-21'";

        System.out.println("INICIANDO IMPORTAÇÃO ANULAÇÂO RECEITA: " + anoSonner);
        try {
            // Anulacao Receita
            tipoReceita = "O";
            stmt = con.prepareStatement(
                    "SELECT Distinct AAR_DTA " +
                            " FROM ANULACAO_DAS_ARRECADACOES " +
                            " WHERE to_char(AAR_DTA, 'YYYY') = ? " +
                            " AND AAR_TPO = 0 " + teste);
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dataGuia = rs.getDate(1);

                stmt2 = con.prepareStatement(
                        "SELECT C.*" +
                                "FROM ANULACAO_DAS_ARRECADACOES A " +
                                "    JOIN RECEITAS_CONTAB B ON ( A.AAR_REC_SEQ = B.REC_SEQ ) " +
                                "    JOIN CODIGOS_ORCAMENTARIOS C ON ( C.COR_SEQ = B.REC_COR_SEQ ) " +
                                "WHERE AAR_DTA = ? " +
                                "AND AAR_TPO = 0 " +
                                "And C.COR_COD not like '9%' " +
                                "ORDER BY 1 ");
                stmt2.setDate(1, new java.sql.Date(dataGuia.getTime()));
                rs2 = stmt2.executeQuery();
                if (rs2.next()) {

                    anulacao = getMaxAnulacao(emLocal, anoAtual, tipoReceita);

                    AnulaReceita anulaReceita = new AnulaReceita(anoAtual, tipoReceita, anulacao, dataGuia, "IMPORTAÇÃO DE ANULAÇÃO DE RECEITA", 0);
                    emLocal.persist(anulaReceita);

                    // Itens da Anulacao
                    stmt3 = con.prepareStatement(
                            "SELECT REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), SUM(AAR_VLR) " +
                                    "FROM ANULACAO_DAS_ARRECADACOES A " +
                                    "    JOIN RECEITAS_CONTAB B ON ( A.AAR_REC_SEQ = B.REC_SEQ ) " +
                                    "    JOIN CODIGOS_ORCAMENTARIOS C ON ( C.COR_SEQ = B.REC_COR_SEQ ) " +
                                    "    LEFT JOIN FONTE_DE_RECURSO E ON ( A.AAR_FRE_SEQ = E.FRE_SEQ ) " +
                                    "    LEFT JOIN FONTES_DE_RECURSO_APLICACAO F ON ( A.AAR_FON_SEQ = F.FON_SEQ ) " +
                                    " WHERE AAR_DTA = ? " +
                                    " AND AAR_TPO = 0 " +
                                    " And C.COR_COD not like '9%' " +
                                    " GROUP BY REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000') " +
                                    " ORDER BY 1 ");
                    stmt3.setDate(1, new java.sql.Date(dataGuia.getTime()));
                    rs3 = stmt3.executeQuery();
                    while (rs3.next()) {
                        fichaReceita = rs3.getInt(1);
                        fonteRecurso = rs3.getInt(2);
                        codAplicacao = rs3.getString(3).trim();
                        valor = rs3.getBigDecimal(4).abs();

                        caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                        caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                        AnulaRecItens anulaRecItens = new AnulaRecItens(anoAtual, tipoReceita, anulacao, fichaReceita, 1, fonteRecurso, caFixo, caVariavel, valor);
                        emLocal.persist(anulaRecItens);

                    }
                    stmt3.close();
                    rs3.close();

                    // Anul Rec Conta Banco
                    int i = 1;
                    stmt3 = con.prepareStatement(
                            "SELECT REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), CBN_COD, CBN_TPO, SUM(AAR_VLR) " +
                                    "FROM ANULACAO_DAS_ARRECADACOES A " +
                                    "    JOIN RECEITAS_CONTAB B ON ( A.AAR_REC_SEQ = B.REC_SEQ ) " +
                                    "    JOIN CODIGOS_ORCAMENTARIOS C ON ( C.COR_SEQ = B.REC_COR_SEQ ) " +
                                    "    JOIN CONTAS_BANCARIAS D ON ( A.AAR_CBN_SEQ = D.CBN_SEQ ) " +
                                    "    LEFT JOIN FONTE_DE_RECURSO E ON ( A.AAR_FRE_SEQ = E.FRE_SEQ ) " +
                                    "    LEFT JOIN FONTES_DE_RECURSO_APLICACAO F ON ( A.AAR_FON_SEQ = F.FON_SEQ ) " +
                                    " WHERE AAR_DTA = ? " +
                                    " AND AAR_TPO = 0 " +
                                    " And C.COR_COD not like '9%' " +
                                    " GROUP BY REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), CBN_COD, CBN_TPO " +
                                    " ORDER BY 1 ");
                    stmt3.setDate(1, new java.sql.Date(dataGuia.getTime()));
                    rs3 = stmt3.executeQuery();
                    while (rs3.next()) {
                        fichaReceita = rs3.getInt(1);
                        fonteRecurso = rs3.getInt(2);
                        codAplicacao = rs3.getString(3);
                        fichaBanco = rs3.getInt(4);
                        tipoContaBancaria = rs3.getInt(5);
                        valor = rs3.getBigDecimal(6);

                        caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                        caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                        tipoFinanceiro = "D";
                        if (tipoContaBancaria == 1) {
                            fichaBanco = 0;
                            tipoFinanceiro = "S";
                        }

                        financeiro(emLocal, tipoFinanceiro, anoAtual, tipoReceita, anulacao, fichaBanco, dataGuia, 1, fonteRecurso, caFixo, caVariavel, valor);

                        // Rec Conta banco
                        AnulaRecContaBanco anulaRecContaBanco = new AnulaRecContaBanco(
                                anoAtual, tipoReceita, anulacao,
                                fichaReceita, 1, fonteRecurso, caFixo, caVariavel,
                                fichaBanco, 1, fonteRecurso, caFixo, caVariavel, i, valor);
                        emLocal.persist(anulaRecContaBanco);

                        i++;
                    }
                    stmt3.close();
                    rs3.close();
                }
                stmt2.close();
                rs2.close();

                // Anulacao de Receitas Dedutora
                stmt2 = con.prepareStatement(
                        "SELECT C.* " +
                                " FROM ANULACAO_DAS_ARRECADACOES A " +
                                "    JOIN RECEITAS_CONTAB B ON ( A.AAR_REC_SEQ = B.REC_SEQ ) " +
                                "    JOIN CODIGOS_ORCAMENTARIOS C ON ( C.COR_SEQ = B.REC_COR_SEQ ) " +
                                "WHERE AAR_DTA = ? " +
                                "AND AAR_TPO = 0 " +
                                "And C.COR_COD like '9%' " +
                                "ORDER BY 1 ");
                stmt2.setDate(1, new java.sql.Date(dataGuia.getTime()));
                rs2 = stmt2.executeQuery();
                if (rs2.next()) {

                    anulacao = getMaxAnulacao(emLocal, anoAtual, tipoReceita);

                    AnulaReceita anulaReceita = new AnulaReceita(anoAtual, tipoReceita, anulacao, dataGuia, "IMPORTAÇÃO DE ANULAÇÃO DE RECEITA", 1);
                    emLocal.persist(anulaReceita);

                    // Itens da Anulação
                    stmt3 = con.prepareStatement(
                            "SELECT REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), SUM(AAR_VLR) " +
                                    "FROM ANULACAO_DAS_ARRECADACOES A " +
                                    "    JOIN RECEITAS_CONTAB B ON ( A.AAR_REC_SEQ = B.REC_SEQ ) " +
                                    "    JOIN CODIGOS_ORCAMENTARIOS C ON ( C.COR_SEQ = B.REC_COR_SEQ ) " +
                                    "    LEFT JOIN FONTE_DE_RECURSO E ON ( A.AAR_FRE_SEQ = E.FRE_SEQ ) " +
                                    "    LEFT JOIN FONTES_DE_RECURSO_APLICACAO F ON ( A.AAR_FON_SEQ = F.FON_SEQ ) " +
                                    " WHERE AAR_DTA = ? " +
                                    " AND AAR_TPO = 0 " +
                                    " And C.COR_COD like '9%' " +
                                    " GROUP BY REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000') " +
                                    " ORDER BY 1 ");
                    stmt3.setDate(1, new java.sql.Date(dataGuia.getTime()));
                    rs3 = stmt3.executeQuery();
                    while (rs3.next()) {
                        fichaReceita = rs3.getInt(1);
                        fonteRecurso = rs3.getInt(2);
                        codAplicacao = rs3.getString(3);
                        valor = rs3.getBigDecimal(4).abs();

                        caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                        caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                        AnulaRecItens anulaRecItens = new AnulaRecItens(anoAtual, tipoReceita, anulacao, fichaReceita, 1, fonteRecurso, caFixo, caVariavel, valor);
                        emLocal.persist(anulaRecItens);
                    }
                    stmt3.close();
                    rs3.close();

                    // Anul Rec Conta Banco
                    int i = 1;
                    stmt3 = con.prepareStatement(
                            "SELECT REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), CBN_COD, CBN_TPO, SUM(AAR_VLR) " +
                                    "FROM ANULACAO_DAS_ARRECADACOES A " +
                                    "    JOIN RECEITAS_CONTAB B ON ( A.AAR_REC_SEQ = B.REC_SEQ ) " +
                                    "    JOIN CODIGOS_ORCAMENTARIOS C ON ( C.COR_SEQ = B.REC_COR_SEQ ) " +
                                    "    JOIN CONTAS_BANCARIAS D ON ( A.AAR_CBN_SEQ = D.CBN_SEQ ) " +
                                    "    LEFT JOIN FONTE_DE_RECURSO E ON ( A.AAR_FRE_SEQ = E.FRE_SEQ ) " +
                                    "    LEFT JOIN FONTES_DE_RECURSO_APLICACAO F ON ( A.AAR_FON_SEQ = F.FON_SEQ ) " +
                                    " WHERE AAR_DTA = ? " +
                                    " AND AAR_TPO = 0 " +
                                    " And C.COR_COD like '9%' " +
                                    " GROUP BY REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), CBN_COD, CBN_TPO " +
                                    " ORDER BY 1 ");
                    stmt3.setDate(1, new java.sql.Date(dataGuia.getTime()));
                    rs3 = stmt3.executeQuery();
                    while (rs3.next()) {
                        fichaReceita = rs3.getInt(1);
                        fonteRecurso = rs3.getInt(2);
                        codAplicacao = rs3.getString(3);
                        fichaBanco = rs3.getInt(4);
                        tipoContaBancaria = rs3.getInt(5);
                        valor = rs3.getBigDecimal(6);

                        caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                        caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                        tipoFinanceiro = "C";
                        if (tipoContaBancaria == 1) {
                            fichaBanco = 0;
                            tipoFinanceiro = "E";
                        }

                        financeiro(emLocal, tipoFinanceiro, anoAtual, tipoReceita, anulacao, fichaBanco, dataGuia, 1, fonteRecurso, caFixo, caVariavel, valor);

                        // Rec Conta banco
                        AnulaRecContaBanco anulaRecContaBanco = new AnulaRecContaBanco(
                                anoAtual, tipoReceita, anulacao,
                                fichaReceita, 1, fonteRecurso, caFixo, caVariavel, i,
                                fichaBanco, 1, fonteRecurso, caFixo, caVariavel, valor);
                        emLocal.persist(anulaRecContaBanco);

                        i++;
                    }
                    stmt3.close();
                    rs3.close();
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

    private static int getMaxAnulacao(EntityManager em, Date ano, String tipo) {
        Integer max;
        Query q = em.createQuery("Select MAX(a.id.anulacao) from AnulaReceita a where a.id.ano = :ano  and a.id.tipo = :tipo ")
                .setParameter("ano", ano)
                .setParameter("tipo", tipo);
        max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }

    public static void delete(EntityManager em, Date ano) {

        deleteQuery("Delete from CBPDEBITO A WHERE A.ANOLANCTO = :ano AND EXISTS ( SELECT * FROM CBPANULARECEITAS B WHERE A.ANOLANCTO = B.ANO AND A.TIPOANULACAOREC = B.TIPO AND A.ANULACAORECEITA = B.ANULACAO AND B.DEDUTORA in (0,1) )", ano);

        deleteQuery("Delete from CBPSAIDASCAIXAS WHERE ANOLANCTO = :ano and ANULACAORECEITA > 0", ano);

        deleteQuery("Delete from CBPCREDITO Where ANOLANCTO = :ano and ANULRECDEDUTORA > 0", ano);

        deleteQuery("Delete from CBPENTRADASCAIXAS Where ANOLANCTO = :ano and ANULRECDEDUTORA > 0", ano);

        deleteQuery("Delete from CBPANULARECITENS A Where A.ANO = :ano AND EXISTS ( SELECT * from CBPANULARECEITAS B Where A.ANO = B.ANO and A.TIPO = B.TIPO AND A.ANULACAO = B.ANULACAO AND B.DEDUTORA in (0,1)) ", ano);

        deleteQuery("DELETE from CBPANULRECCONTABAN A Where A.ANO = :ano AND EXISTS ( SELECT * from CBPANULARECEITAS B Where A.ANO = B.ANO and A.TIPO = B.TIPO AND A.ANULACAO = B.ANULACAO AND B.DEDUTORA in (0,1) ) ", ano);

        deleteQuery("Delete from CBPANULARECEITAS Where ANO = :ano and DEDUTORA in (0,1)", ano);
    }

    private static void financeiro(EntityManager em, String tipo, Date anoAtual, String tipoReceita, int anulacao, int fichaBanco, Date dataGuia, int versaoRecurso, int fonteRecurso, int caFixo, int caVariavel, BigDecimal valor) {

        ContasBancarias contasBancarias = em.find(ContasBancarias.class, fichaBanco);

        Integer max;
        Query q;

        if (tipo.equals("D")) {
            q = em.createQuery("Select MAX(a.id.numero) from Debito a where a.id.fichaConta = :ficha  and a.id.data = :data ")
                    .setParameter("ficha", fichaBanco);
        } else if (tipo.equals("C")) {
            q = em.createQuery("Select MAX(a.id.numero) From Credito a Where a.id.fichaConta = :ficha And a.id.data = :data ")
                    .setParameter("ficha", fichaBanco);
        } else if (tipo.equals("S")) {
            q = em.createQuery("SELECT Max(a.id.saida) from SaidasCaixa a Where a.id.data = :data");
        } else {
            q = em.createQuery("SELECT Max(a.id.entrada) from EntradasCaixa a Where a.id.data = :data");
        }
        q.setParameter("data", dataGuia);
        max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;

        if (tipo.equals("D")) {
            Debito debito = new Debito();
            debito.getId().setFichaConta(fichaBanco);
            debito.getId().setData(dataGuia);
            debito.getId().setNumero(max);
            debito.setBanco(contasBancarias.getBanco());
            debito.setAgencia(contasBancarias.getAgencia());
            debito.setConta(contasBancarias.getCodigo());
            debito.setHistorico("IMPORTAÇÃO DE ANULAÇÃO DE RECEITA");
            debito.setAnoLancto(anoAtual);
            debito.setLancamento(-1);
            debito.setTipoAnulacaoRec(tipoReceita);
            debito.setAnulacaoReceita(anulacao);
            debito.setFinalidade("P");
            debito.setVersaoRecurso(versaoRecurso);
            debito.setFonteRecurso(fonteRecurso);
            debito.setCaFixo(caFixo);
            debito.setCaVariavel(caVariavel);
            debito.setValor(valor);
            em.persist(debito);
        } else if (tipo.equals("C")) {
            Credito credito = new Credito();
            credito.getId().setFichaConta(fichaBanco);
            credito.getId().setData(dataGuia);
            credito.getId().setNumero(max);
            credito.setBanco(contasBancarias.getBanco());
            credito.setAgencia(contasBancarias.getAgencia());
            credito.setConta(contasBancarias.getCodigo());
            credito.setHistorico("IMPORTAÇÃO DE ANULAÇÃO DE RECEITA");
            credito.setAnoLancto(anoAtual);
            credito.setLancamento(-1);
            credito.setAnulRecDedutora(anulacao);
            credito.setVersaoRecurso(versaoRecurso);
            credito.setFonteRecurso(fonteRecurso);
            credito.setCaFixo(caFixo);
            credito.setCaVariavel(caVariavel);
            credito.setValor(valor);
            em.persist(credito);
        } else if (tipo.equals("S")) {
            SaidasCaixa saidaCaixa = new SaidasCaixa();
            saidaCaixa.getId().setData(dataGuia);
            saidaCaixa.getId().setSaida(max);
            saidaCaixa.setHistorico("IMPORTAÇÃO DE ANULAÇÃO DE RECEITA");
            saidaCaixa.setAnoLancto(anoAtual);
            saidaCaixa.setLancamento(-1);
            saidaCaixa.setTipoAnulacaoRec(tipoReceita);
            saidaCaixa.setAnulacaoReceita(anulacao);
            saidaCaixa.setVersaoRecurso(versaoRecurso);
            saidaCaixa.setFonteRecurso(fonteRecurso);
            saidaCaixa.setValor(valor);
            em.persist(saidaCaixa);
        } else {
            EntradasCaixa entradasCaixa = new EntradasCaixa();
            entradasCaixa.getId().setData(dataGuia);
            entradasCaixa.getId().setEntrada(max);
            entradasCaixa.setHistorico("IMPORTAÇÃO DE ANULAÇÃO DE RECEITA");
            entradasCaixa.setAnoLancto(anoAtual);
            entradasCaixa.setLancamento(-1);
            entradasCaixa.setTipoGuia(tipoReceita);
            entradasCaixa.setAnulRecDedutora(anulacao);
            entradasCaixa.setVersaoRecurso(versaoRecurso);
            entradasCaixa.setFonteRecurso(fonteRecurso);
            entradasCaixa.setValor(valor);
            em.persist(entradasCaixa);
        }
    }
}
