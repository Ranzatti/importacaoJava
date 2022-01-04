package Cidades.Louveira;

import _Entity.*;
import _Entity.EntradasCaixaPK;
import _Infra.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ImportaGuiaReceita extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {

        final String ORCAMENTARIA = "O";
        final String EXTRAORCAMENTARIA = "E";

        EntityManager emLocal = conexaoDestino("louveira");

        Connection con = conexaoOrigemOracle();
        PreparedStatement stmt = null;
        PreparedStatement stmt2, stmt3, stmt4;
        ResultSet rs = null;
        ResultSet rs2, rs3;

        int numero, fichaReceita, fichaBanco, versaoRecurso, fonteRecurso, caFixo, caVariavel, tipoContaBancaria;
        Date anoAtual, dataGuia;
        String teste, receita, tipoReceita, codAplicacao;
        BigDecimal valor;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete(emLocal, anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and A.RAP_EMN_EXE = 2021";

        System.out.println("INICIANDO IMPORTAÇÃO GUIA RECEITA: " + anoSonner);
        try {
            tipoReceita = ORCAMENTARIA;
            stmt = con.prepareStatement(
                    "select Distinct ARR_DTA " +
                            " from ARRECADACOES " +
                            " where to_char(ARR_DTA, 'YYYY') = ? " + teste +
                            " order by 1 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dataGuia = rs.getDate(1);

                stmt2 = con.prepareStatement(
                        "SELECT distinct 1 FROM ARRECADACOES WHERE ARR_DTA = ? and ARR_VLR > 0 ");
                stmt2.setDate(1, new java.sql.Date(dataGuia.getTime()));
                rs2 = stmt2.executeQuery();
                if (rs2.next()) {

                    numero = getMaxGuiaReceita(emLocal, anoAtual, tipoReceita);

                    System.out.println("Guia: " + numero);

                    GuiaReceita guiaReceita = new GuiaReceita(anoAtual, tipoReceita, numero, 1, dataGuia, dataGuia, dataGuia, "Importação de Guia de Receita", "DIG", null);
                    emLocal.persist(guiaReceita);

                    // Itens da Guia
                    stmt3 = con.prepareStatement(
                            "SELECT REC_COD_RED, COR_COD, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), SUM(ARR_VLR)" +
                                    " FROM ARRECADACOES A" +
                                    "       JOIN RECEITAS_CONTAB B ON ( A.ARR_REC_SEQ = B.REC_SEQ ) " +
                                    "       JOIN CODIGOS_ORCAMENTARIOS C ON ( C.COR_SEQ = B.REC_COR_SEQ ) " +
                                    "       LEFT JOIN FONTE_DE_RECURSO E ON ( A.ARR_FRE_SEQ = E.FRE_SEQ ) " +
                                    "       LEFT JOIN FONTES_DE_RECURSO_APLICACAO F ON ( A.ARR_FON_SEQ = F.FON_SEQ ) " +
                                    " WHERE ARR_DTA = ? " +
                                    " GROUP BY REC_COD_RED, COR_COD, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000') " +
                                    " HAVING  SUM(ARR_VLR) > 0 " +
                                    " ORDER BY 1 ");
                    stmt3.setDate(1, new java.sql.Date(dataGuia.getTime()));
                    rs3 = stmt3.executeQuery();
                    while (rs3.next()) {
                        fichaReceita = rs3.getInt(1);
                        receita = rs3.getString(2).trim();
                        fonteRecurso = rs3.getInt(3);
                        codAplicacao = rs3.getString(4).trim();
                        valor = rs3.getBigDecimal(5);

                        caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                        caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                        ItensGuiaReceita itensGuiaReceita = new ItensGuiaReceita(anoAtual, tipoReceita, numero, fichaReceita, 1, fonteRecurso, caFixo, caVariavel, receita, valor);
                        emLocal.persist(itensGuiaReceita);
                    }
                    stmt3.close();
                    rs3.close();

                    // REC Conta Banco
                    int i = 1;
                    stmt3 = con.prepareStatement(
                            "SELECT REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), CBN_COD, CBN_TPO, SUM(ARR_VLR)" +
                                    " FROM ARRECADACOES A" +
                                    "       JOIN RECEITAS_CONTAB B ON ( A.ARR_REC_SEQ = B.REC_SEQ ) " +
                                    "       JOIN CODIGOS_ORCAMENTARIOS C ON ( C.COR_SEQ = B.REC_COR_SEQ ) " +
                                    "       JOIN CONTAS_BANCARIAS D ON ( A.ARR_CBN_SEQ = D.CBN_SEQ ) " +
                                    "       LEFT JOIN FONTE_DE_RECURSO E ON ( A.ARR_FRE_SEQ = E.FRE_SEQ ) " +
                                    "       LEFT JOIN FONTES_DE_RECURSO_APLICACAO F ON ( A.ARR_FON_SEQ = F.FON_SEQ ) " +
                                    " WHERE ARR_DTA = ? " +
                                    " GROUP BY REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), CBN_COD, CBN_TPO " +
                                    " HAVING  SUM(ARR_VLR) > 0 " +
                                    " ORDER BY 1 ");
                    stmt3.setDate(1, new java.sql.Date(dataGuia.getTime()));
                    rs3 = stmt3.executeQuery();
                    while (rs3.next()) {
                        fichaReceita = rs3.getInt(1);
                        fonteRecurso = rs3.getInt(2);
                        codAplicacao = rs3.getString(3).trim();
                        fichaBanco = rs3.getInt(4);
                        tipoContaBancaria = rs3.getInt(5);
                        valor = rs3.getBigDecimal(6);

                        caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                        caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                        if (tipoContaBancaria == 1) {
                            fichaBanco = 0;

                            Query q = emLocal.createQuery("Select a from EntradasCaixa a where a.anoLancto = :ano  and a.tipoGuia = :tipo and a.guia = :guia ")
                                    .setParameter("ano", anoAtual)
                                    .setParameter("tipo", tipoReceita)
                                    .setParameter("guia", numero);
                            List<EntradasCaixa> ec = q.getResultList();

                            if (ec.size() == 0) {
                                financeiro(emLocal, "E", anoAtual, tipoReceita, numero, fichaBanco, dataGuia, 1, fonteRecurso, caFixo, caVariavel, valor);
                            } else {
                                EntradasCaixa entradasCaixas = emLocal.find(EntradasCaixa.class, new EntradasCaixaPK(ec.get(0).getId().getData(), ec.get(0).getId().getEntrada()));
                                entradasCaixas.setValor(entradasCaixas.getValor().add(valor));
                                emLocal.merge(entradasCaixas);
                            }
                        } else {
                            financeiro(emLocal, "C", anoAtual, tipoReceita, numero, fichaBanco, dataGuia, 1, fonteRecurso, caFixo, caVariavel, valor);
                        }

                        RecContaBanco recContaBanco = new RecContaBanco(anoAtual, tipoReceita, numero, fichaReceita, 1, fonteRecurso, caFixo, caVariavel, fichaBanco, 1, fonteRecurso, caFixo, caVariavel, i, valor);
                        emLocal.persist(recContaBanco);

                        i++;
                    }
                    stmt3.close();
                    rs3.close();
                }
                stmt2.close();
                rs2.close();
                // FINAL DE GUIAS DE RECEITA

                // LANCAMENTO RECEITA DEDUTORA
                stmt2 = con.prepareStatement(
                        "SELECT distinct 1 FROM ARRECADACOES WHERE ARR_DTA = ? and  ARR_VLR < 0 ");
                stmt2.setDate(1, new java.sql.Date(dataGuia.getTime()));
                rs2 = stmt2.executeQuery();
                if (rs2.next()) {

                    numero = getMaxAnulacaoReceita(emLocal, anoAtual, tipoReceita);

                    AnulaReceita anulaReceita = new AnulaReceita(anoAtual, tipoReceita, numero, dataGuia, "IMPORTAÇÃO DE ANULAÇÃO DE RECEITA DEDUTORA", 2);
                    emLocal.persist(anulaReceita);

                    // Itens da Anulacao
                    stmt3 = con.prepareStatement(
                            "SELECT REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), SUM(ARR_VLR)" +
                                    " FROM ARRECADACOES A" +
                                    "       JOIN RECEITAS_CONTAB B ON ( A.ARR_REC_SEQ = B.REC_SEQ ) " +
                                    "       JOIN CODIGOS_ORCAMENTARIOS C ON ( C.COR_SEQ = B.REC_COR_SEQ ) " +
                                    "       LEFT JOIN FONTE_DE_RECURSO E ON ( A.ARR_FRE_SEQ = E.FRE_SEQ ) " +
                                    "       LEFT JOIN FONTES_DE_RECURSO_APLICACAO F ON ( A.ARR_FON_SEQ = F.FON_SEQ ) " +
                                    " WHERE ARR_DTA = ? " +
                                    " GROUP BY REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000') " +
                                    " HAVING  SUM(ARR_VLR) < 0 " +
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

                        AnulaRecItens anulaRecItens = new AnulaRecItens(anoAtual, tipoReceita, numero, fichaReceita, 1, fonteRecurso, caFixo, caVariavel, valor);
                        emLocal.persist(anulaRecItens);
                    }
                    stmt3.close();
                    rs3.close();

                    // Anul Conta Banco
                    int i = 1;
                    stmt3 = con.prepareStatement(
                            "SELECT REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), CBN_COD, SUM(ARR_VLR)" +
                                    " FROM ARRECADACOES A" +
                                    "       JOIN RECEITAS_CONTAB B ON ( A.ARR_REC_SEQ = B.REC_SEQ ) " +
                                    "       JOIN CODIGOS_ORCAMENTARIOS C ON ( C.COR_SEQ = B.REC_COR_SEQ ) " +
                                    "       JOIN CONTAS_BANCARIAS D ON ( A.ARR_CBN_SEQ = D.CBN_SEQ ) " +
                                    "       LEFT JOIN FONTE_DE_RECURSO E ON ( A.ARR_FRE_SEQ = E.FRE_SEQ ) " +
                                    "       LEFT JOIN FONTES_DE_RECURSO_APLICACAO F ON ( A.ARR_FON_SEQ = F.FON_SEQ ) " +
                                    " WHERE ARR_DTA = ? " +
                                    " GROUP BY REC_COD_RED, COALESCE(E.FRE_COD, 1), COALESCE(F.FON_COD, '11000'), CBN_COD " +
                                    " HAVING  SUM(ARR_VLR) < 0 " +
                                    " ORDER BY 1 ");
                    stmt3.setDate(1, new java.sql.Date(dataGuia.getTime()));
                    rs3 = stmt3.executeQuery();
                    while (rs3.next()) {
                        fichaReceita = rs3.getInt(1);
                        fonteRecurso = rs3.getInt(2);
                        codAplicacao = rs3.getString(3).trim();
                        fichaBanco = rs3.getInt(4);
                        valor = rs3.getBigDecimal(5).abs();

                        caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                        caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                        // Debito
                        financeiro(emLocal, "D", anoAtual, tipoReceita, numero, fichaBanco, dataGuia, 1, fonteRecurso, caFixo, caVariavel, valor);

                        // Rec Conta banco
                        AnulaRecContaBanco anulaRecContaBanco = new AnulaRecContaBanco(
                                anoAtual, tipoReceita, numero,
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
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Ops");
            e.printStackTrace();
        } finally {
            System.out.println("Acabou");
            closeConexao(con, stmt, rs);
        }
    }

    private static int getMaxGuiaReceita(EntityManager em, Date ano, String tipo) {
        Integer max = 0;
        Query q = em.createQuery("Select MAX(a.id.numero) from GuiaReceita a where a.id.ano = :ano  and a.id.tipo = :tipo ")
                .setParameter("ano", ano)
                .setParameter("tipo", tipo);
        max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }

    private static int getMaxAnulacaoReceita(EntityManager em, Date ano, String tipo) {
        Integer max = 0;
        Query q = em.createQuery("Select MAX(a.id.anulacao) from AnulaReceita a where a.id.ano = :ano  and a.id.tipo = :tipo ")
                .setParameter("ano", ano)
                .setParameter("tipo", tipo);
        max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }

    private static void financeiro(EntityManager em, String tipo, Date anoAtual, String tipoReceita, int numero, int fichaBanco, Date dataGuia, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, BigDecimal valor) {

        ContasBancarias contasBancarias = em.find(ContasBancarias.class, fichaBanco);

        Integer max;
        Query q;

        if (tipo.equals("C")) {
            q = em.createQuery("Select MAX(a.id.numero) From Credito a Where a.id.fichaConta = :ficha And a.id.data = :data ")
                    .setParameter("ficha", fichaBanco);
        } else if (tipo.equals("D")) {
            q = em.createQuery("Select MAX(a.id.numero) from Debito a where a.id.fichaConta = :ficha  and a.id.data = :data ")
                    .setParameter("ficha", fichaBanco);
        } else {
            q = em.createQuery("SELECT MAX(a.id.entrada) from EntradasCaixa a Where a.id.data = :data ");
        }
        q.setParameter("data", dataGuia);
        max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;

        if (tipo.equals("C")) {
            Credito credito = new Credito();
            credito.getId().setFichaConta(fichaBanco);
            credito.getId().setData(dataGuia);
            credito.getId().setNumero(max);
            credito.setBanco(contasBancarias.getBanco());
            credito.setAgencia(contasBancarias.getAgencia());
            credito.setConta(contasBancarias.getCodigo());
            credito.setHistorico("Recebimento de Guia de Receita Orçamentária");
            credito.setAnoLancto(anoAtual);
            credito.setLancamento(-1);
            credito.setTipoGuia(tipoReceita);
            credito.setGuia(numero);
            credito.setValor(valor);
            credito.setVersaoRecurso(versaoRecurso);
            credito.setFonteRecurso(fonteRecurso);
            credito.setCaFixo(caFixo);
            credito.setCaVariavel(caVariavel);
            em.persist(credito);
        } else if (tipo.equals("D")) {
            Debito debito = new Debito();
            debito.getId().setFichaConta(fichaBanco);
            debito.getId().setData(dataGuia);
            debito.getId().setNumero(max);
            debito.setBanco(contasBancarias.getBanco());
            debito.setAgencia(contasBancarias.getAgencia());
            debito.setConta(contasBancarias.getCodigo());
            debito.setHistorico("Anulação de Receita Orçamentaria");
            debito.setAnoLancto(anoAtual);
            debito.setLancamento(-1);
            debito.setTipoAnulacaoRec(tipoReceita);
            debito.setAnulacaoReceita(numero);
            debito.setFinalidade("P");
            debito.setValor(valor);
            debito.setVersaoRecurso(versaoRecurso);
            debito.setFonteRecurso(fonteRecurso);
            debito.setCaFixo(caFixo);
            debito.setCaVariavel(caVariavel);
            em.persist(debito);
        } else {
            EntradasCaixa entradasCaixa = new EntradasCaixa();
            entradasCaixa.getId().setData(dataGuia);
            entradasCaixa.getId().setEntrada(max);
            entradasCaixa.setHistorico("Recebimento de Guia de Receita Orçamentária");
            entradasCaixa.setTipoGuia(tipoReceita);
            entradasCaixa.setAnoLancto(anoAtual);
            entradasCaixa.setGuia(numero);
            entradasCaixa.setTransferencia(0);
            entradasCaixa.setValor(valor);
            entradasCaixa.setVersaoRecurso(versaoRecurso);
            entradasCaixa.setFonteRecurso(fonteRecurso);
            entradasCaixa.setLancamento(-1);
            em.persist(entradasCaixa);
        }
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
