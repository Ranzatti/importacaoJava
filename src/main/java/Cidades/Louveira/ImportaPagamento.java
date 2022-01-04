package Cidades.Louveira;

import _Entity.*;
import _Infra.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ImportaPagamento extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {

        final String EMPENHO = "E";
        final String ORDEMPAGTO = "O";

        EntityManager emLocal = conexaoDestino("louveira");

        Connection con = conexaoOrigemOracle();
        PreparedStatement stmt = null;
        PreparedStatement stmt2, stmt3;
        ResultSet rs = null;
        ResultSet rs2, rs3;

        Integer opgSeq, iopSeq, restosPagarSeq, empenhoSeq, despesaExtraSeq, fornecedor, autorizacao, documento, parcela, notaFiscalSeq, liquidacao,
                nroOP, fonteRecurso, caFixo, caVariavel;
        Date anoAtual, dataAutorizacao, vencimento, anoDocumento, dataPagamento;
        BigDecimal valorParcela, valorPago, valorDesconto, valorProcessado, valorNaoProcessado, totalGeral, linha;
        String teste, historico, tipoDoc, historicoRestosPagar, codAplicacao;
        boolean geraAutorizacao;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        delete(emLocal, anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and OPG_SEQ = 5062381 ";
        //teste = "and OPG_NRO_ORD >= 100 and OPG_NRO_ORD <= 200 ";

        System.out.println("INICIANDO IMPORTAÇÃO PAGAMENTO: " + anoSonner);
        try {

            stmt = con.prepareStatement(
                    "select count(*) " +
                            " from OP " +
                            " where OPG_OGO_SEQ in ( select OGO_SEQ from ORGAOS_2004 where OGO_COD = 1 ) " +
                            " and to_char(OPG_DTA, 'YYYY')  = ? " + teste +
                            " and OPG_SEQ in ( select IOP_OPG_SEQ from ITENS_DE_OP where  (IOP_EMN_SEQ > 0 or IOP_DSP_SEQ > 0 or IOP_RAP_SEQ > 0 ) )");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            rs.next();
            totalGeral = rs.getBigDecimal(1);
            stmt.close();
            rs.close();


            linha = BigDecimal.ZERO;
            stmt = con.prepareStatement(
                    "select DISTINCT OPG_SEQ, OPG_NRO_ORD, OPG_DTA, OPG_HIS " +
                            " from OP " +
                            " where OPG_OGO_SEQ in ( select OGO_SEQ from ORGAOS_2004 where OGO_COD = 1 ) " +
                            " and to_char(OPG_DTA, 'YYYY')  = ? " + teste +
                            " and OPG_SEQ in ( select IOP_OPG_SEQ from ITENS_DE_OP where (IOP_EMN_SEQ > 0 or IOP_DSP_SEQ > 0 or IOP_RAP_SEQ > 0 ) )" +
                            " order by 1 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                opgSeq = rs.getInt(1);
                autorizacao = rs.getInt(2);
                dataAutorizacao = rs.getDate(3);
                historico = rs.getString(4);

                System.out.println("Ano: " + anoSonner + " [" + opgSeq + "] " +
                        linha + "/" + totalGeral + " " +
                        (linha.multiply(BigDecimal.valueOf(100)).divide(totalGeral, 4, RoundingMode.DOWN)));

                geraAutorizacao = false;
                tipoDoc = "";
                parcela = 0;

                // Itens da Autorizacao
                stmt2 = con.prepareStatement(
                        "select IOP_SEQ, IOP_RAP_SEQ, IOP_EMN_SEQ, IOP_DSP_SEQ, IOP_VLR, IOP_DTA_VEN, IOP_NTF_SEQ, " +
                                " case " +
                                "        when IOP_EMN_SEQ > 0 then ( select EMN_EXE from EMPENHOS where EMN_SEQ = IOP_EMN_SEQ ) " +
                                "        when IOP_DSP_SEQ > 0 then ( select DSP_EXE from DESPESAS_EXTRA where DSP_SEQ = IOP_DSP_SEQ ) " +
                                "        when IOP_RAP_SEQ > 0 then ( select RAP_EMN_EXE from RESTOS_A_PAGAR where RAP_SEQ = IOP_RAP_SEQ ) " +
                                " end as anodocumento, " +
                                " case " +
                                "        when IOP_EMN_SEQ > 0 then ( select EMN_NRO from EMPENHOS where EMN_SEQ = IOP_EMN_SEQ ) " +
                                "        when IOP_DSP_SEQ > 0 then ( select DSP_NRO from DESPESAS_EXTRA where DSP_SEQ = IOP_DSP_SEQ ) " +
                                "        when IOP_RAP_SEQ > 0 then ( select RAP_EMN_NRO from RESTOS_A_PAGAR where RAP_SEQ = IOP_RAP_SEQ ) " +
                                " end as documento  " +
                                " from ITENS_DE_OP " +
                                " where IOP_OPG_SEQ = ? " +
                                " and (IOP_VLR_ANL is null or IOP_VLR_ANL = 0) " +
                                " and ( exists ( select * from EMPENHOS where EMN_SEQ = IOP_EMN_SEQ ) or " +
                                "        exists ( select * from DESPESAS_EXTRA where DSP_SEQ = IOP_DSP_SEQ ) or " +
                                "        exists ( select * from RESTOS_A_PAGAR where RAP_SEQ = IOP_RAP_SEQ ) " +
                                "    ) ");
                stmt2.setInt(1, opgSeq);
                rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    iopSeq = rs2.getInt(1);
                    restosPagarSeq = rs2.getInt(2);
                    empenhoSeq = rs2.getInt(3);
                    despesaExtraSeq = rs2.getInt(4);
                    valorParcela = rs2.getBigDecimal(5);
                    vencimento = rs2.getDate(6);
                    notaFiscalSeq = rs2.getInt(7);
                    anoDocumento = java.sql.Date.valueOf(rs2.getInt(8) + "-01-01");
                    documento = rs2.getInt(9);

                    // EMPENHOS
                    if (empenhoSeq > 0) {
                        tipoDoc = EMPENHO;

                        parcela = getMaxLiquidacao(emLocal, anoAtual, documento);

                        LiquidacaoEmpenho liquidacaoEmpenho = new LiquidacaoEmpenho(anoAtual, documento, parcela, dataAutorizacao, valorParcela, historico);
                        emLocal.persist(liquidacaoEmpenho);

                        LiquidaPagto liquidaPagto = new LiquidaPagto(anoAtual, documento, parcela, parcela);
                        emLocal.persist(liquidaPagto);

                        Pagamentos pagamentos = new Pagamentos(anoAtual, documento, parcela, dataAutorizacao, vencimento, historico, valorParcela, null, null, null);
                        emLocal.persist(pagamentos);

                        //Pegando Data de Pagamento
                        stmt3 = con.prepareStatement(
                                "SELECT B.CHE_DTA_PAG, SUM(a.CHI_VLR), SUM(COALESCE(A.CHI_VLR_RTC, 0)) " +
                                        "FROM CHEQUES_ITENS A " +
                                        "JOIN CHEQUES B ON ( A.CHI_CHE_SEQ = B.CHE_SEQ ) " +
                                        "WHERE CHI_IOP_SEQ = ? " +
                                        "And CHE_DTA_PAG is not null " +
                                        "Group by B.CHE_DTA_PAG ");
                        stmt3.setInt(1, iopSeq);
                        rs3 = stmt3.executeQuery();
                        if (rs3.next()) {
                            dataPagamento = rs3.getDate(1);
                            valorPago = rs3.getBigDecimal(2);
                            valorDesconto = rs3.getBigDecimal(3);

                            updatePagamento(emLocal, anoDocumento, tipoDoc, documento, parcela, dataPagamento, valorPago, valorDesconto);

                            // Pegando Desconto
                            if ((valorDesconto.signum() > 0)) {
                                desconto(con, emLocal, anoAtual, tipoDoc, documento, parcela, dataPagamento, autorizacao, iopSeq);
                            }

                            geraAutorizacao = true;
                        }
                        stmt3.close();
                        rs3.close();
                    }

                    // DESPESA EXTRA-ORCAMENTARIA
                    if (despesaExtraSeq > 0) {
                        tipoDoc = ORDEMPAGTO;
                        parcela = 1;
                        stmt3 = con.prepareStatement(
                                "SELECT B.CHE_DTA_PAG, SUM(a.CHI_VLR), SUM(COALESCE(A.CHI_VLR_RTC, 0)) " +
                                        "FROM CHEQUES_ITENS A " +
                                        "JOIN CHEQUES B ON ( A.CHI_CHE_SEQ = B.CHE_SEQ ) " +
                                        "WHERE CHI_IOP_SEQ = ? " +
                                        "And CHE_DTA_PAG is not null " +
                                        "Group by B.CHE_DTA_PAG ");
                        stmt3.setInt(1, iopSeq);
                        rs3 = stmt3.executeQuery();
                        if (rs3.next()) {
                            dataPagamento = rs3.getDate(1);
                            valorPago = rs3.getBigDecimal(2);
                            valorDesconto = rs3.getBigDecimal(3);

                            updatePagamento(emLocal, anoDocumento, tipoDoc, documento, parcela, dataPagamento, valorPago, valorDesconto);

                            // Pegando Desconto
                            if ((valorDesconto.signum() > 0)) {
                                desconto(con, emLocal, anoAtual, tipoDoc, documento, parcela, dataPagamento, autorizacao, iopSeq);
                            }

                            geraAutorizacao = true;
                        }
                        stmt3.close();
                        rs3.close();
                    }

                    // RESTOS APAGAR
                    if (restosPagarSeq > 0) {
                        stmt3 = con.prepareStatement(
                                "select RAP_HIS, FOR_COD_USU, FRE_COD, COALESCE(FON_COD, '11000' ) " +
                                        "from RESTOS_A_PAGAR " +
                                        "join FORNECEDORES on (RAP_FOR_SEQ = FOR_SEQ) " +
                                        "left join FONTE_DE_RECURSO on ( RAP_FRE_SEQ = FRE_SEQ ) " +
                                        "left join FONTES_DE_RECURSO_APLICACAO on (RAP_FON_SEQ = FON_SEQ)" +
                                        "where RAP_SEQ = ? ");
                        stmt3.setInt(1, restosPagarSeq);
                        rs3 = stmt3.executeQuery();
                        rs3.next();
                        historicoRestosPagar = rs3.getString(1);
                        fornecedor = Integer.parseInt(rs3.getString(2));
                        fonteRecurso = rs3.getInt(3);
                        codAplicacao = rs3.getString(4);
                        stmt3.close();
                        rs3.close();

                        caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                        caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                        nroOP = getMaxOP(emLocal, anoAtual);

                        parcela = getRestosProcParc(emLocal, anoDocumento, documento, valorParcela);

                        // PROCESSADO
                        if (parcela > 0) {
                            valorProcessado = valorParcela;
                            valorNaoProcessado = BigDecimal.ZERO;

                            op(emLocal, anoAtual, nroOP, 0, fornecedor, 1, fonteRecurso, caFixo, caVariavel, dataAutorizacao, null, anoDocumento, documento, parcela, valorParcela, valorProcessado, valorNaoProcessado, historicoRestosPagar);
                        } else {
                            // NÃO PROCESSADO

                            valorNaoProcessado = valorParcela;
                            valorProcessado = BigDecimal.ZERO;

                            liquidacao = getMaxLiquidacaoResto(emLocal, anoDocumento, documento);

                            LiquidaRestos liquidaRestos = new LiquidaRestos(anoDocumento, documento, liquidacao, dataAutorizacao, historicoRestosPagar, dataAutorizacao, null, anoAtual, -1, valorNaoProcessado);
                            emLocal.persist(liquidaRestos);

                            op(emLocal, anoAtual, nroOP, 0, fornecedor, 1, fonteRecurso, caFixo, caVariavel, dataAutorizacao, null, anoDocumento, documento, parcela, valorParcela, valorProcessado, valorNaoProcessado, historicoRestosPagar);

                            LiqRestCronogra liqRestCronogra = new LiqRestCronogra(anoDocumento, documento, liquidacao, liquidacao, anoAtual, nroOP);
                            emLocal.persist(liqRestCronogra);
                        }

                        tipoDoc = ORDEMPAGTO;
                        parcela = 1;
                        stmt3 = con.prepareStatement(
                                "SELECT B.CHE_DTA_PAG, SUM(a.CHI_VLR), SUM(COALESCE(A.CHI_VLR_RTC, 0)) " +
                                        "FROM CHEQUES_ITENS A " +
                                        "JOIN CHEQUES B ON ( A.CHI_CHE_SEQ = B.CHE_SEQ ) " +
                                        "WHERE CHI_IOP_SEQ = ? " +
                                        "And CHE_DTA_PAG is not null " +
                                        "Group by B.CHE_DTA_PAG ");
                        stmt3.setInt(1, iopSeq);
                        rs3 = stmt3.executeQuery();
                        if (rs3.next()) {
                            dataPagamento = rs3.getDate(1);
                            valorPago = rs3.getBigDecimal(2);
                            valorDesconto = rs3.getBigDecimal(3);

                            updatePagamento(emLocal, anoAtual, tipoDoc, nroOP, parcela, dataPagamento, valorPago, valorDesconto);

                            BaixaRestos baixaRestos = new BaixaRestos(anoDocumento, documento, anoAtual, nroOP, dataPagamento, valorPago, valorNaoProcessado, valorProcessado);
                            emLocal.persist(baixaRestos);

                            documento = nroOP;

                            // Pegando Desconto
                            if (valorDesconto.signum() > 0) {
                                desconto(con, emLocal, anoAtual, tipoDoc, documento, parcela, dataPagamento, autorizacao, iopSeq);
                            }

                            geraAutorizacao = true;
                        }
                        stmt3.close();
                        rs3.close();
                    }

                    if (geraAutorizacao) {

//                        System.out.println("Autorizacaoo - " + autorizacao + " - TipoDoc: " + tipoDoc + " - Documento: " + documento + " - Parcela: " + parcela + " [" + opgSeq + "]");

                        AutPagto autPagto = emLocal.find(AutPagto.class, new AutPagtoPK(anoAtual, autorizacao));

                        if (Objects.isNull(autPagto)) {
                            autPagto = new AutPagto(anoAtual, autorizacao, dataAutorizacao, dataAutorizacao, historico);
                            emLocal.persist(autPagto);
                        }

                        ItensAutPagto itensAutPagto = emLocal.find(ItensAutPagto.class, new ItensAutPagtoPK(anoAtual, autorizacao, tipoDoc, documento, parcela));

                        if (Objects.isNull(itensAutPagto)) {
                            itensAutPagto = new ItensAutPagto(anoAtual, autorizacao, tipoDoc, documento, parcela);
                            emLocal.persist(itensAutPagto);
                        }

                        // documento de pagamento
                        if (notaFiscalSeq > 0) {
                            docPagto(con, emLocal, anoAtual, tipoDoc, documento, parcela, notaFiscalSeq);
                        }
                    }
                }
                stmt2.close();
                rs2.close();

                if (geraAutorizacao) {
                    // REALIZANDO O PAGAMENTO DA AP
                    financeiro(con, emLocal, anoAtual, autorizacao, opgSeq);

                    // REC CONTA BANCO
                    contaBanco(emLocal, anoAtual, autorizacao);
                }

                linha = linha.add(BigDecimal.valueOf(1));
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

    private static void op(EntityManager emLocal, Date anoAtual, Integer nroOP, Integer ficha, Integer fornecedor, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, Date dataAutorizacao, Date dataPagamento, Date anoDocumento, Integer documento, Integer parcela,
                           BigDecimal valorParcela, BigDecimal valorProcessado, BigDecimal valorNaoProcessado, String historicoRestosPagar) {
        OrdensPagto ordensPagto = new OrdensPagto(anoAtual, nroOP, ficha, fornecedor, dataAutorizacao, historicoRestosPagar, valorParcela, dataAutorizacao, BigDecimal.ZERO, anoDocumento, documento, parcela, valorProcessado, valorNaoProcessado);
        emLocal.persist(ordensPagto);

        PagtoOP pagtoOP = new PagtoOP(anoAtual, nroOP, dataPagamento, valorParcela);
        emLocal.persist(pagtoOP);

        OPFonteRecurso opFonteRecurso = new OPFonteRecurso(anoAtual, nroOP, versaoRecurso, fonteRecurso, caFixo, caVariavel, valorParcela);
        emLocal.persist(opFonteRecurso);
    }

    private static void docPagto(Connection con, EntityManager em, Date anoAtual, String tipoDoc, int documento, int liquidacao, int notaFiscalSeq) {

        PreparedStatement stmtAux;
        ResultSet rsAux;
        Date dataEmissao;
        String notaFiscalNro;
        BigDecimal valor;
        Integer notaFiscalTipo;

        try {
            stmtAux = con.prepareStatement(
                    "select NTF_DTA_DOC, NTF_NRO_DOC, NTF_TPO_DOC, NTF_VLR " +
                            " from NOTAS_FISCAIS " +
                            " where NTF_SEQ = ? ");
            stmtAux.setInt(1, notaFiscalSeq);
            rsAux = stmtAux.executeQuery();
            while (rsAux.next()) {
                dataEmissao = rsAux.getDate(1);
                notaFiscalNro = rsAux.getString(2).trim();
                notaFiscalTipo = rsAux.getInt(3);
                valor = rsAux.getBigDecimal(4);

                //Tipo Documentos 0=Nota Fiscal, 1=Fatura, 2=Documento, 3=Oficio, 4=Recibo, 5=Folha de Pagamento, 6=Diaria, 7=Bilhete de Passagem, 8=Repasse de Recursos, 9=Documentos Diversos, 10=Nota Fiscal Eletronica, 11=Nota Fiscal Servicos'
                switch (notaFiscalTipo) {
                    case 0:
                        notaFiscalTipo = 1; // nota fiscal
                        break;
                    case 1:
                        notaFiscalTipo = 2; // fatura
                        break;
                    case 4:
                        notaFiscalTipo = 7; // recibo
                        break;
                    case 5:
                        notaFiscalTipo = 6; // folha de pagamento
                        break;
                    case 10:
                        notaFiscalTipo = 10; // Nota Fiscal Eletronica
                        break;
                    default:
                        notaFiscalTipo = 999; // outros
                        break;
                }

                DocPagto docPagto = em.find(DocPagto.class, new DocPagtoPK(anoAtual, tipoDoc, documento, liquidacao, notaFiscalTipo, notaFiscalNro));

                if (Objects.isNull(docPagto)) {
                    docPagto = new DocPagto(anoAtual, tipoDoc, documento, liquidacao, notaFiscalTipo, notaFiscalNro, dataEmissao, "Importacao Nota Fiscal SEQ: " + notaFiscalSeq, 1, valor);
                    em.persist(docPagto);
                }
            }
            stmtAux.close();
            rsAux.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void contaBanco(EntityManager em, Date ano, Integer autorizacao) {

        AutPagtoFonteRec autPagtoFonteRec;

        Integer totalItens, totalPago, documento, parcela, fonteRecurso, caFixo, caVariavel, fichaConta;
        String tipoDoc;
        BigDecimal valor;
        int indice;

        Query q;

        // AJUSTES DE AUTP PAGTO FONTE REC
        q = em.createQuery("Select count (a) from ItensAutPagto a where a.id.ano = :ano  and a.id.autorizacao = :autorizacao ")
                .setParameter("ano", ano)
                .setParameter("autorizacao", autorizacao);
        totalItens = ((Long) q.getSingleResult()).intValue();

        q = em.createQuery("Select count (a) from Debito a where a.anoLancto = :ano  and a.autPagto = :autorizacao ")
                .setParameter("ano", ano)
                .setParameter("autorizacao", autorizacao);
        totalPago = ((Long) q.getSingleResult()).intValue();

        q = em.createQuery("Select count (a) from Cheque a where a.anoLancto = :ano  and a.autPagto = :autorizacao ")
                .setParameter("ano", ano)
                .setParameter("autorizacao", autorizacao);
        totalPago += ((Long) q.getSingleResult()).intValue();


        if (totalItens > 1 && totalPago > 1) {
        } else {
            if (totalItens == 1) {
                q = em.createQuery("Select i from ItensAutPagto i where i.id.ano = :ano  and i.id.autorizacao = :autorizacao ")
                        .setParameter("ano", ano)
                        .setParameter("autorizacao", autorizacao);
                List<ItensAutPagto> itensAutPagtos = q.getResultList();

                // Buscando dados dos Debitos
                q = em.createQuery("Select d from Debito d where d.anoLancto = :ano  and d.autPagto = :autorizacao ")
                        .setParameter("ano", ano)
                        .setParameter("autorizacao", autorizacao);
                List<Debito> debitos = q.getResultList();

                indice = 1;
                for (int i = 0; i < debitos.size(); i++) {

                    tipoDoc = itensAutPagtos.get(0).getId().getTipoDoc();
                    documento = itensAutPagtos.get(0).getId().getDocumento();
                    parcela = itensAutPagtos.get(0).getId().getParcela();

                    autPagtoFonteRec = new AutPagtoFonteRec(
                            ano,
                            autorizacao,
                            debitos.get(i).getId().getFichaConta(),
                            tipoDoc,
                            documento,
                            parcela,
                            indice,
                            1,
                            debitos.get(i).getFonteRecurso(),
                            debitos.get(i).getCaFixo(),
                            debitos.get(i).getCaVariavel(),
                            debitos.get(i).getValor());
                    em.persist(autPagtoFonteRec);
                    indice++;
                }

                //Buscando dados dos Cheques
                q = em.createQuery("Select c from Cheque c where c.anoLancto = :ano  and c.autPagto = :autorizacao ")
                        .setParameter("ano", ano)
                        .setParameter("autorizacao", autorizacao);
                List<Cheque> cheques = q.getResultList();

                indice = 1;
                for (int i = 0; i < cheques.size(); i++) {

                    tipoDoc = itensAutPagtos.get(0).getId().getTipoDoc();
                    documento = itensAutPagtos.get(0).getId().getDocumento();
                    parcela = itensAutPagtos.get(0).getId().getParcela();

                    autPagtoFonteRec = new AutPagtoFonteRec(
                            ano,
                            autorizacao,
                            cheques.get(i).getId().getFichaConta(),
                            tipoDoc,
                            documento,
                            parcela,
                            indice,
                            1,
                            cheques.get(i).getFonteRecurso(),
                            cheques.get(i).getCaFixo(),
                            cheques.get(i).getCaVariavel(),
                            cheques.get(i).getValor());
                    em.persist(autPagtoFonteRec);
                    indice++;
                }
            } else {
                if (totalPago == 1) {
                    // Buscando dados dos Debitos
                    q = em.createQuery("Select d from Debito d where d.anoLancto = :ano  and d.autPagto = :autorizacao ")
                            .setParameter("ano", ano)
                            .setParameter("autorizacao", autorizacao);
                    List<Debito> debitos = q.getResultList();

                    if (debitos.size() > 0) {
                        // Buscando itens da Autorizacao
                        q = em.createQuery("Select i from ItensAutPagto i where i.id.ano = :ano  and i.id.autorizacao = :autorizacao ")
                                .setParameter("ano", ano)
                                .setParameter("autorizacao", autorizacao);
                        List<ItensAutPagto> itensAutPagtos = q.getResultList();

                        indice = 1;
                        for (int i = 0; i < itensAutPagtos.size(); i++) {

                            tipoDoc = itensAutPagtos.get(i).getId().getTipoDoc();
                            documento = itensAutPagtos.get(i).getId().getDocumento();
                            parcela = itensAutPagtos.get(i).getId().getParcela();

                            if (tipoDoc.equals("E")) {
                                q = em.createQuery("Select p.valorPagamento from Pagamentos p where p.id.ano = :ano  and p.id.empenho = :documento and p.id.pagamento = :parcela ")
                                        .setParameter("ano", ano)
                                        .setParameter("documento", documento)
                                        .setParameter("parcela", parcela);
                            } else {
                                q = em.createQuery("Select p.valorPagto from PagtoOP p where p.id.ano = :ano  and p.id.numero = :documento ")
                                        .setParameter("ano", ano)
                                        .setParameter("documento", documento);
                            }
                            valor = (BigDecimal) q.getSingleResult();

                            autPagtoFonteRec = new AutPagtoFonteRec(
                                    ano,
                                    autorizacao,
                                    debitos.get(0).getId().getFichaConta(),
                                    tipoDoc,
                                    documento,
                                    parcela,
                                    indice,
                                    1,
                                    debitos.get(0).getFonteRecurso(),
                                    debitos.get(0).getCaFixo(),
                                    debitos.get(0).getCaVariavel(),
                                    valor);
                            em.persist(autPagtoFonteRec);
                            indice++;
                        }
                    }
                    // Buscando dados dos Debitos
                    q = em.createQuery("Select c from Cheque c where c.anoLancto = :ano and c.autPagto = :autorizacao ")
                            .setParameter("ano", ano)
                            .setParameter("autorizacao", autorizacao);
                    List<Cheque> cheques = q.getResultList();

                    if (cheques.size() > 0) {
                        // Buscando itens da Autorizacao
                        q = em.createQuery("Select i from ItensAutPagto i where i.id.ano = :ano  and i.id.autorizacao = :autorizacao ")
                                .setParameter("ano", ano)
                                .setParameter("autorizacao", autorizacao);
                        List<ItensAutPagto> itensAutPagtos = q.getResultList();

                        indice = 1;
                        for (int i = 0; i < itensAutPagtos.size(); i++) {

                            tipoDoc = itensAutPagtos.get(i).getId().getTipoDoc();
                            documento = itensAutPagtos.get(i).getId().getDocumento();
                            parcela = itensAutPagtos.get(i).getId().getParcela();

                            if (tipoDoc.equals("E")) {
                                q = em.createQuery("Select p.valorPagamento from Pagamentos p where p.id.ano = :ano  and p.id.empenho = :documento and p.id.pagamento = :parcela ")
                                        .setParameter("ano", ano)
                                        .setParameter("documento", documento)
                                        .setParameter("parcela", parcela);
                            } else {
                                q = em.createQuery("Select p.valorPagto from PagtoOP p where p.id.ano = :ano  and p.id.numero = :documento ")
                                        .setParameter("ano", ano)
                                        .setParameter("documento", documento);
                            }
                            valor = (BigDecimal) q.getSingleResult();

                            autPagtoFonteRec = new AutPagtoFonteRec(
                                    ano,
                                    autorizacao,
                                    cheques.get(0).getId().getFichaConta(),
                                    tipoDoc,
                                    documento,
                                    parcela,
                                    indice,
                                    1,
                                    cheques.get(0).getFonteRecurso(),
                                    cheques.get(0).getCaFixo(),
                                    cheques.get(0).getCaVariavel(),
                                    valor);
                            em.persist(autPagtoFonteRec);
                            indice++;
                        }
                    }
                }
            }
        }
    }

    private static void financeiro(Connection con, EntityManager em, Date anoAtual, Integer autorizacao, Integer opgSeq) {

        PreparedStatement stmtAux, stmtAux1;
        ResultSet rsAux, rsAux1;
        String codAplicacao;
        Integer fichaBanco, banco, fonteRecurso, caFixo, caVariavel, contaBancariaSeq, formaPagamento, nroCheque;
        String agencia, contaBancaria, historico;
        Date dataCheque, dataPagamento;
        BigDecimal valorPago;

        historico = "Importação OPSEQ: " + opgSeq;

        try {
            stmtAux = con.prepareStatement(
                    "SELECT CHE_CBN_SEQ, CHE_TPO, CHE_NRO, CHE_DTA, CHE_DTA_PAG, CHE_VLR " +
                            "FROM CHEQUES " +
                            "WHERE CHE_SEQ in ( " +
                            "        SELECT CHI_CHE_SEQ FROM CHEQUES_ITENS WHERE CHI_IOP_SEQ in ( " +
                            "                SELECT IOP_SEQ FROM ITENS_DE_OP WHERE IOP_OPG_SEQ = ? )) " +
                            "And CHE_DTA_PAG is not null " +
                            "And CHE_VLR > 0 ");
            stmtAux.setInt(1, opgSeq);
            rsAux = stmtAux.executeQuery();
            while (rsAux.next()) {
                contaBancariaSeq = rsAux.getInt(1);
                formaPagamento = rsAux.getInt(2);
                //nroCheque = formaPagamento == 2 ? Integer.parseInt(rsAux.getString(3)) : 0;
                dataCheque = rsAux.getDate(4);
                dataPagamento = rsAux.getDate(5);
                valorPago = rsAux.getBigDecimal(6);

                nroCheque = 0;
                if (rsAux.getString(3).matches("[0-9]*")) { // verificando se é numero
                    nroCheque = Integer.parseInt(rsAux.getString(3));
                }

                stmtAux1 = con.prepareStatement(
                        "select A.CBN_COD, C.BCO_COD, B.AGE_COD, A.CBN_NRO, D.FRE_COD, COALESCE(E.FON_COD, '11000' ) " +
                                "from CONTAS_BANCARIAS A " +
                                "JOIN AGENCIAS B ON ( A.CBN_AGE_SEQ = B.AGE_SEQ ) " +
                                "JOIN BANCOS C ON ( B.AGE_BCO_SEQ = C.BCO_SEQ ) " +
                                "left JOIN FONTE_DE_RECURSO D ON ( A.CBN_FRE_SEQ = D.FRE_SEQ ) " +
                                "left JOIN FONTES_DE_RECURSO_APLICACAO E ON ( A.CBN_FON_SEQ = E.FON_SEQ ) " +
                                "join ORGAOS_2004 F ON ( A.CBN_OGO_SEQ = F.OGO_SEQ ) " +
                                "where A.CBN_SEQ = ? ");
                stmtAux1.setInt(1, contaBancariaSeq);
                rsAux1 = stmtAux1.executeQuery();
                rsAux1.next();
                fichaBanco = rsAux1.getInt(1);
                banco = rsAux1.getInt(2);
                agencia = rsAux1.getString(3);
                contaBancaria = rsAux1.getString(4);
                fonteRecurso = rsAux1.getInt(5);
                codAplicacao = rsAux1.getString(6);
                stmtAux1.close();
                rsAux1.close();

                caFixo = Integer.parseInt(codAplicacao.substring(0, 3));
                caVariavel = Integer.parseInt(codAplicacao.substring(3, 5));

                Integer max = 0;
                if (formaPagamento == 1 || formaPagamento == 3) {
                    Query q;
                    if (formaPagamento == 1) {
                        q = em.createQuery("SELECT MAX(a.id.saida) from SaidasCaixa a Where a.id.data = :data ")
                                .setParameter("data", dataPagamento);
                    } else {
                        q = em.createQuery("Select MAX(a.id.numero) from Debito a where a.id.fichaConta = :ficha  and a.id.data = :data ")
                                .setParameter("ficha", fichaBanco)
                                .setParameter("data", dataPagamento);
                    }
                    max = (Integer) q.getSingleResult();
                    max = (max == null) ? 0 : max;
                    max++;
                }

                if (formaPagamento == 1) {
                    SaidasCaixa saidasCaixa = new SaidasCaixa();
                    saidasCaixa.getId().setData(dataPagamento);
                    saidasCaixa.getId().setSaida(max);
                    saidasCaixa.setHistorico(historico);
                    saidasCaixa.setAnoLancto(anoAtual);
                    saidasCaixa.setLancamento(-1);
                    saidasCaixa.setAutPagto(autorizacao);
                    saidasCaixa.setValor(valorPago);
                    saidasCaixa.setVersaoRecurso(1);
                    saidasCaixa.setFonteRecurso(fonteRecurso);
                    em.persist(saidasCaixa);
                } else if (formaPagamento == 2) {
                    Cheque cheque = new Cheque();
                    cheque.getId().setFichaConta(fichaBanco);
                    cheque.getId().setNumero(nroCheque);
                    cheque.getId().setData(dataPagamento);
                    cheque.getId().setHistorico(historico);
                    cheque.setBanco(banco);
                    cheque.setAgencia(agencia);
                    cheque.setConta(contaBancaria);
                    cheque.setAnoLancto(anoAtual);
                    cheque.setLancamento(-1);
                    cheque.setValor(valorPago);
                    cheque.setVersaoRecurso(1);
                    cheque.setFonteRecurso(fonteRecurso);
                    cheque.setCaFixo(caFixo);
                    cheque.setCaVariavel(caVariavel);
                    cheque.setFinalidade("P");
                    cheque.setAutPagto(autorizacao);
                    cheque.setDataEmissao(dataCheque);
                    cheque.setDataBaixa(dataPagamento);
                    em.persist(cheque);
                } else if (formaPagamento == 3) {
                    Debito debito = new Debito();
                    debito.getId().setFichaConta(fichaBanco);
                    debito.getId().setData(dataPagamento);
                    debito.getId().setNumero(max);
                    debito.setBanco(banco);
                    debito.setAgencia(agencia);
                    debito.setConta(contaBancaria);
                    debito.setHistorico(historico);
                    debito.setAnoLancto(anoAtual);
                    debito.setLancamento(-1);
                    debito.setValor(valorPago);
                    debito.setVersaoRecurso(1);
                    debito.setFonteRecurso(fonteRecurso);
                    debito.setCaFixo(caFixo);
                    debito.setCaVariavel(caVariavel);
                    debito.setFinalidade("P");
                    debito.setAutPagto(autorizacao);
                    em.persist(debito);
                }
            }
            stmtAux.close();
            rsAux.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void delete(EntityManager em, Date ano) {
        delete("CBPAUTPAGTO", ano);
        delete("CBPITENSAUTPAGTO", ano);
        delete("CBPAUTPAGTOFONTREC", ano);
        delete("CBPLIQUIDACOES", ano);
        delete("CBPLIQUIDAPAGTO", ano);
        delete("CBPPAGAMENTOS", ano);
        delete("CBPDESCONTOSPAGTO", ano);
        delete("CBPDESCONTOSOP", ano);
        delete("CBPDOCPAGTO", ano);

        deleteQuery("Delete from CBPITENSGUIA A Where A.ANO = :ano " +
                " AND EXISTS ( SELECT * from CBPGUIARECEITA B Where A.ANO = B.ANO AND A.TIPO = B.TIPO AND A.GUIA = B.NUMERO AND B.ORIGEM = 'AUT' )", ano);
        deleteQuery("Delete from CBPRECCONTABANCO A Where A.ANO = :ano " +
                " AND EXISTS ( SELECT * from CBPGUIARECEITA B Where A.ANO = B.ANO AND A.TIPO = B.TIPO AND A.GUIA = B.NUMERO AND B.ORIGEM = 'AUT' )", ano);
        deleteQuery("Delete from CBPGUIARECEITA Where ANO = :ano and ORIGEM = 'AUT' ", ano);

        deleteQuery("Delete from CBPDEBITO where ANOLANCTO = :ano and AUTPAGTO > 0 ", ano);
        deleteQuery("Delete from CBPCHEQUE where ANOLANCTO = :ano and AUTPAGTO > 0 ", ano);
        deleteQuery("Delete from CBPSAIDASCAIXAS where ANOLANCTO = :ano and AUTPAGTO > 0 ", ano);

        deleteQuery("Delete from CBPBAIXARESTOS Where ANOOP = :ano ", ano);
        deleteQuery("Delete from CBPLIQRESTCRONOGRA Where ANOOP = :ano ", ano);
        deleteQuery("Delete from CBPLIQUIDARESTOS Where ANOFATO = :ano ", ano);

        deleteQuery("Delete from CBPOPFONTERECURSO A " +
                " Where A.ANO = :ano AND exists ( Select * from CBPORDENSPAGTO B Where A.ANO = B.ANO AND A.OP = B.NUMERO AND B.ANORESTOS IS NOT NULL ) ", ano);
        deleteQuery("Delete from CBPPAGTOOPS A " +
                " Where A.ANO = :ano AND exists ( Select * from CBPORDENSPAGTO B Where A.ANO = B.ANO AND A.OP = B.NUMERO AND B.ANORESTOS IS NOT NULL ) ", ano);
        deleteQuery("Delete from CBPORDENSPAGTO Where ANO = :ano AND ANORESTOS IS NOT NULL  ", ano);

        em.getTransaction().begin();
        em.createNativeQuery("Update CBPORDENSPAGTO set DESCONTO = 0 Where ANO = :ano ")
                .setParameter("ano", ano)
                .executeUpdate();
        em.createNativeQuery("Update CBPPAGTOOPS set DATAPAGTO = null, VALORPAGTO = 0 Where ANO = :ano ")
                .setParameter("ano", ano)
                .executeUpdate();
        em.getTransaction().commit();
    }

    private static void desconto(Connection con, EntityManager em, Date anoAtual, String tipoDoc, Integer
            documento, Integer parcela, Date dataGuia, Integer autorizacao, Integer iopSeq) {
        PreparedStatement stmtAux;
        ResultSet rsAux;
        Integer fichaExtra, fichaOrcamentaria, fichaReceita, guia;
        String codigoReceita, tipoReceita;
        BigDecimal valorReceita;

        GuiaReceita guiaReceita;
        DescontosPagto descontosPagto;
        DescontosOP descontosOP;
        ItensGuiaReceita itensGuiaReceita;

        try {
            stmtAux = con.prepareStatement(
                    "select RTC_OPE_REC, REC_COD_RED, COR_COD, SUM(RTC_VLR) " +
                            " from RETENCOES_EM_OP " +
                            " left JOIN RECEITAS_CONTAB ON ( RTC_OPE_REC_ORC = REC_SEQ ) " +
                            " left JOIN CODIGOS_ORCAMENTARIOS ON ( REC_COR_SEQ = COR_SEQ  ) " +
                            " where RTC_IOP_SEQ = ? " +
                            " GRoup by RTC_OPE_REC, REC_COD_RED, COR_COD ");
            stmtAux.setInt(1, iopSeq);
            rsAux = stmtAux.executeQuery();
            while (rsAux.next()) {
                fichaExtra = rsAux.getInt(1);
                fichaOrcamentaria = rsAux.getInt(2);
                codigoReceita = rsAux.getString(3);
                valorReceita = rsAux.getBigDecimal(4);

                tipoReceita = fichaExtra > 0 ? "E" : "O";
                fichaReceita = fichaExtra > 0 ? fichaExtra : fichaOrcamentaria;
                codigoReceita = (tipoReceita.equals("E") ? Integer.toString(fichaExtra) : codigoReceita);

                Query q = em.createQuery("Select MAX(a.id.numero) from GuiaReceita a where a.id.ano = :ano  and a.id.tipo = :tipo ")
                        .setParameter("ano", anoAtual)
                        .setParameter("tipo", tipoReceita);
                guia = (Integer) q.getSingleResult();
                guia = (guia == null) ? 0 : guia;
                guia++;

                guiaReceita = new GuiaReceita(anoAtual, tipoReceita, guia, 1, dataGuia, dataGuia, dataGuia, "Guia de Receita de desconto - IOSEQ: " + iopSeq, "AUT", autorizacao);
                em.persist(guiaReceita);

                if (tipoDoc.equals("O")) {
                    descontosOP = new DescontosOP(anoAtual, documento, parcela, tipoReceita, guia);
                    em.persist(descontosOP);
                } else {
                    descontosPagto = new DescontosPagto(anoAtual, documento, parcela, tipoReceita, guia);
                    em.persist(descontosPagto);
                }

                itensGuiaReceita = new ItensGuiaReceita(anoAtual, tipoReceita, guia, fichaReceita, 1, 1, 110, 0, codigoReceita, valorReceita);
                em.persist(itensGuiaReceita);
            }
            stmtAux.close();
            rsAux.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private static Integer getRestosProcParc(EntityManager em, Date ano, Integer empenho, BigDecimal valor) {
        Query q = em.createQuery("Select max (l.id.parcela) from RestosProcParc l where l.id.anoEmpenho = :ano and l.id.empenho = :empenho and l.valor = :valor ")
                .setParameter("ano", ano)
                .setParameter("empenho", empenho)
                .setParameter("valor", valor);
        Integer parcela = (Integer) q.getSingleResult();
        parcela = (parcela == null) ? 0 : parcela;
        return parcela;

//        Query q = em.createNativeQuery("select max(PARCELA) from CBPRESTOSPROCPARC where ANOEMPENHO = :ano and EMPENHO = :empenho and VALOR = :valor ")
//                .setParameter("ano", ano)
//                .setParameter("empenho", empenho)
//                .setParameter("valor", valor);
//        BigDecimal parcela = (BigDecimal) q.getSingleResult();
//        parcela = (parcela == null) ? BigDecimal.ZERO: parcela;
//        return parcela.intValue();

//        BigDecimal parcelaBD = (BigDecimal) em.createNativeQuery("select max(PARCELA) from CBPRESTOSPROCPARC where ANOEMPENHO = :ano and EMPENHO = :empenho and VALOR = :valor ")
//                .setParameter("ano", ano)
//                .setParameter("empenho", empenho)
//                .setParameter("valor", valor).getSingleResult();
//        parcelaBD = (parcelaBD == null) ? BigDecimal.ZERO : parcelaBD;
//        return parcelaBD.intValue();
    }

    private static void updatePagamento(EntityManager em, Date ano, String tipoDoc, Integer documento, Integer
            parcela, Date dataPagamento, BigDecimal valorPago, BigDecimal valorDesconto) {
        if (tipoDoc.equals("O")) {
            OrdensPagto ordensPagto = em.find(OrdensPagto.class, new OrdensPagtoPK(ano, documento));
            ordensPagto.setDesconto(valorDesconto);
            em.merge(ordensPagto);

            PagtoOP pagtoOP = em.find(PagtoOP.class, new PagtoOPPK(ano, documento));
            pagtoOP.setDataPagto(dataPagamento);
            valorPago = valorPago.add(pagtoOP.getValorPagto());
            pagtoOP.setValorPagto(valorPago);
            em.merge(pagtoOP);
        } else {
            Pagamentos pagamentos = em.find(Pagamentos.class, new PagamentosPK(ano, documento, parcela));
            pagamentos.setDataPagamento(dataPagamento);
            pagamentos.setValorPagamento(valorPago);
            pagamentos.setDesconto(valorDesconto);
            em.merge(pagamentos);
        }
    }

    private static int getMaxLiquidacao(EntityManager em, Date ano, Integer empenho) {
        Query q = em.createQuery("Select MAX(l.id.liquidacao) from LiquidacaoEmpenho l where l.id.ano = :ano  and l.id.empenho = :empenho ")
                .setParameter("ano", ano)
                .setParameter("empenho", empenho);
        Integer max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }

    private static int getMaxOP(EntityManager em, Date ano) {
        Query q = em.createQuery("Select MAX(o.id.numero) from OrdensPagto o where o.id.ano = :ano ")
                .setParameter("ano", ano);
        Integer max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }

    private static int getMaxLiquidacaoResto(EntityManager em, Date ano, Integer empenho) {
        Query q = em.createQuery("Select MAX(l.id.liquidacao) from LiquidaRestos l where l.id.anoEmpenho = :ano and l.id.empenho = :empenho")
                .setParameter("ano", ano)
                .setParameter("empenho", empenho);
        Integer max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }
}
