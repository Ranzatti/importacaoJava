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
import java.util.Objects;

public class ImportaPagamento extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {

        final String ORDEMPAGTO = "O";
        final String EMPENHO = "E";

        EntityManager emLocal = conexaoDestino("TresCoracoes");

        Connection con = conexaoOrigemSQLServer();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        String teste, historicoPagamento, nomeCredor, desdobramento, tipoRestosPagar;
        Integer seqPagamento, seqliquidacao, autorizacao, empenho, liquidacao, parcela, nroOP, fichaConta, versaoRecurso, fonteRecurso, fornecedor, empenhoRestos, fichaDotacao, codCredor;
        BigDecimal valorLiquidacao, valorParcela, valorPago, valorDesconto, valorEmpenho, valorRPP, valorRPNP;
        Date anoAtual, dataAutorizacao, dataPagamento, dataLiquidacao, vencimento, anoEmpenhoRestos, dataEmpenho, ultimoDiaExercicio;
        boolean empenhos, ordemPagamento, restosPagar, restosPagarNaoProcessado;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        versaoRecurso = getVersao(anoSonner);

        delete(anoAtual);

        emLocal.getTransaction().begin();

        empenhos = false;
        ordemPagamento = false;
        restosPagar = true;

        teste = "";
        //teste = "and NRO_EMPENHO in (1) ";
        //teste = "and SEQ_CT_LIQUIDACAO in (493093) ";
        //teste = "and SEQ_CT_PAGAMENTO in (494065) ";

        try {
            if (empenhos) {
                System.out.println("INICIANDO IMPORTAÇÃO PAGAMENTOS - EMPENHOS " + anoSonner);
                stmt = con.prepareStatement(
                        "SELECT DISTINCT " +
                                "    SEQ_CT_LIQUIDACAO, " +
                                "    SEQ_CT_PAGAMENTO, " +
                                "    NRO_PAGAMENTO_ORDEM, " +
                                "    DAT_PAGAMENTO_ORDEM, " +
                                "    DAT_PAGAMENTO, " +
                                "    NRO_EMPENHO, " +
                                "    NRO_LIQUIDACAO_EMPENHO, " +
                                "    VLR_LIQUIDACAO, " +
                                "    VLR_PAGAMENTO, " +
                                "    coalesce(VLR_PAGAMENTO_DESCONTO, 0) as VLR_PAGAMENTO_DESCONTO, " +
                                "    VLR_PAGAMENTO_LIQUIDO, " +
                                "    HST_PAGAMENTO " +
                                "FROM " +
                                "    dbo.VW_CT_PAGAMENTO " +
                                "WHERE " +
                                "    ANO_PAGAMENTO = ? " +
                                "AND SBL_PAGAMENTO_TIPO = 'O' " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " + teste +
                                "ORDER BY " +
                                "    NRO_PAGAMENTO_ORDEM ");
                stmt.setInt(1, anoSonner);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    seqliquidacao = rs.getInt(1);
                    seqPagamento = rs.getInt(2);
                    autorizacao = rs.getInt(3);
                    dataAutorizacao = rs.getDate(4);
                    dataPagamento = rs.getDate(5);
                    empenho = rs.getInt(6);
                    liquidacao = rs.getInt(7);
                    valorLiquidacao = rs.getBigDecimal(8);
                    valorParcela = rs.getBigDecimal(9);
                    valorDesconto = rs.getBigDecimal(10);
                    valorPago = rs.getBigDecimal(11);
                    historicoPagamento = rs.getString(12).trim().toUpperCase();

                    parcela = 0;
                    Query q = emLocal.createQuery("Select a.id.pagamento from Pagamentos a where a.seqImportacao = :seqliquidacao ")
                            .setParameter("seqliquidacao", seqliquidacao);
                    parcela = (Integer) q.getSingleResult();

                    System.out.println("Autorizacaoo - " + autorizacao + " - TipoDoc: E " + " - Documento: " + empenho + " - Parcela: " + parcela + " [" + seqPagamento + "]");

                    AutPagto autPagto = new AutPagto();
                    autPagto.getId().setAno(anoAtual);
                    autPagto.getId().setNumero(autorizacao);
                    autPagto.setDataAutorizacao(dataAutorizacao);
                    autPagto.setDataPagamento(dataPagamento);
                    autPagto.setDescricao(historicoPagamento);
                    emLocal.persist(autPagto);

                    ItensAutPagto itensAutPagto = new ItensAutPagto();
                    itensAutPagto.getId().setAno(anoAtual);
                    itensAutPagto.getId().setAutorizacao(autorizacao);
                    itensAutPagto.getId().setTipoDoc(EMPENHO);
                    itensAutPagto.getId().setDocumento(empenho);
                    itensAutPagto.getId().setParcela(parcela);
                    emLocal.persist(itensAutPagto);

                    // Pegando Desconto
                    if (valorDesconto.signum() > 0) {
                        desconto(emLocal, con, anoAtual, seqPagamento, autorizacao, EMPENHO, empenho, parcela, versaoRecurso, dataPagamento);
                    }

                    // Dando Baixa
                    Pagamentos pagamentos = emLocal.find(Pagamentos.class, new PagamentosPK(anoAtual, empenho, parcela));
                    pagamentos.setDataPagamento(dataPagamento);
                    pagamentos.setValorPagamento(valorPago);
                    pagamentos.setDesconto(valorDesconto);
                    emLocal.merge(pagamentos);

                    // Pagamento
                    financeiro(emLocal, con, anoAtual, seqPagamento, autorizacao, EMPENHO, empenho, parcela, versaoRecurso, dataPagamento);
                }
                stmt.close();
                rs.close();
            }

            // ORDEM DE PAGAMENTO
            if (ordemPagamento) {
                System.out.println("INICIANDO IMPORTAÇÃO PAGAMENTOS - OP " + anoSonner);
                stmt = con.prepareStatement(
                        "SELECT DISTINCT " +
                                "    SEQ_CT_PAGAMENTO, " +
                                "    NRO_PAGAMENTO_ORDEM, " +
                                "    DAT_PAGAMENTO_ORDEM, " +
                                "    DAT_PAGAMENTO, " +
                                "    VLR_PAGAMENTO, " +
                                "    COALESCE(VLR_PAGAMENTO_DESCONTO, 0) AS VLR_PAGAMENTO_DESCONTO, " +
                                "    VLR_PAGAMENTO_LIQUIDO, " +
                                "    HST_PAGAMENTO, " +
                                "    SEQ_CT_PLANO_CONTA, " +
                                "    COD_FONTE_RECURSO, " +
                                "    COD_PESSOA " +
                                "FROM " +
                                "    dbo.VW_CT_PAGAMENTO " +
                                "WHERE " +
                                "    ANO_PAGAMENTO = ? " +
                                "AND SBL_PAGAMENTO_TIPO = 'E' " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " + teste +
                                "ORDER BY " +
                                "    NRO_PAGAMENTO_ORDEM ");
                stmt.setInt(1, anoSonner);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    seqPagamento = rs.getInt(1);
                    autorizacao = rs.getInt(2);
                    dataAutorizacao = rs.getDate(3);
                    dataPagamento = rs.getDate(4);
                    valorParcela = rs.getBigDecimal(5);
                    valorDesconto = rs.getBigDecimal(6);
                    valorPago = rs.getBigDecimal(7);
                    historicoPagamento = rs.getString(8).trim().toUpperCase();
                    fichaConta = rs.getInt(9);
                    fonteRecurso = rs.getInt(10);
                    fornecedor = rs.getInt(11);

                    nroOP = getMaxOP(emLocal, anoAtual);

                    System.out.println("Autorizacaoo - " + autorizacao + " - TipoDoc: O " + " - Documento: " + nroOP + " - Parcela: 1 [" + seqPagamento + "]");

                    OrdensPagto ordensPagto = new OrdensPagto();
                    ordensPagto.getId().setAno(anoAtual);
                    ordensPagto.getId().setNumero(nroOP);
                    ordensPagto.setFicha(fichaConta);
                    ordensPagto.setFornecedor(fornecedor);
                    ordensPagto.setData(dataPagamento);
                    ordensPagto.setHistorico(historicoPagamento);
                    ordensPagto.setValorOP(valorParcela);
                    ordensPagto.setVencimento(dataPagamento);
                    ordensPagto.setDesconto(valorDesconto);
                    emLocal.persist(ordensPagto);

                    PagtoOP pagtoOP = new PagtoOP();
                    pagtoOP.getId().setAno(anoAtual);
                    pagtoOP.getId().setNumero(nroOP);
                    pagtoOP.setDataPagto(dataPagamento);
                    pagtoOP.setValorPagto(valorPago);
                    emLocal.persist(pagtoOP);

                    OPFonteRecurso opFonteRecurso = new OPFonteRecurso();
                    opFonteRecurso.getId().setAno(anoAtual);
                    opFonteRecurso.getId().setNumero(nroOP);
                    opFonteRecurso.setVersaoRecurso(versaoRecurso);
                    opFonteRecurso.setFonteRecurso(fonteRecurso);
                    opFonteRecurso.setCaFixo(999);
                    opFonteRecurso.setCaVariavel(0);
                    opFonteRecurso.setValor(valorParcela);
                    emLocal.persist(opFonteRecurso);

                    // Cadastrando Conta Extra
                    ContaExtra contaExtra = emLocal.find(ContaExtra.class, new ContaExtraPK(anoAtual, fichaConta));
                    if (Objects.isNull(contaExtra)) {
                        contaExtra = new ContaExtra();
                        contaExtra.getId().setAno(anoAtual);
                        contaExtra.getId().setContaExtra(fichaConta);
                        contaExtra.setNome("Conta Extra");
                        contaExtra.setEmpresa(5);
                        emLocal.persist(contaExtra);
                    }

                    AutPagto autPagto = new AutPagto();
                    autPagto.getId().setAno(anoAtual);
                    autPagto.getId().setNumero(autorizacao);
                    autPagto.setDataAutorizacao(dataAutorizacao);
                    autPagto.setDataPagamento(dataPagamento);
                    autPagto.setDescricao(historicoPagamento);
                    emLocal.persist(autPagto);

                    ItensAutPagto itensAutPagto = new ItensAutPagto();
                    itensAutPagto.getId().setAno(anoAtual);
                    itensAutPagto.getId().setAutorizacao(autorizacao);
                    itensAutPagto.getId().setTipoDoc(ORDEMPAGTO);
                    itensAutPagto.getId().setDocumento(nroOP);
                    itensAutPagto.getId().setParcela(1);
                    emLocal.persist(itensAutPagto);

                    // Pegando Desconto
                    if (valorDesconto.signum() > 0) {
                        desconto(emLocal, con, anoAtual, seqPagamento, autorizacao, ORDEMPAGTO, nroOP, 1, versaoRecurso, dataPagamento);
                    }

                    // Pagamento
                    financeiro(emLocal, con, anoAtual, seqPagamento, autorizacao, ORDEMPAGTO, nroOP, 1, versaoRecurso, dataPagamento);
                }
                stmt.close();
                rs.close();
            }

            // RESTOS A PAGAR PROCESSADOS
            if (restosPagar) {
                System.out.println("INICIANDO IMPORTAÇÃO PAGAMENTOS - OP " + anoSonner);
                stmt = con.prepareStatement(
                        "SELECT DISTINCT " +
                                "    SEQ_CT_PAGAMENTO, " +
                                "    NRO_PAGAMENTO_ORDEM, " +
                                "    DAT_PAGAMENTO_ORDEM, " +
                                "    DAT_PAGAMENTO, " +
                                "    ANO_EMPENHO, " +
                                "    NRO_EMPENHO, " +
                                "    DAT_EMPENHO, " +
                                "    NRO_ORCAMENTO_DESPESA_FICHA, " +
                                "    NRO_LIQUIDACAO_EMPENHO, " +
                                "    VLR_EMPENHO, " +
                                "    VLR_LIQUIDACAO, " +
                                "    COALESCE(VLR_PAGAMENTO_DESCONTO, 0) AS VLR_PAGAMENTO_DESCONTO, " +
                                "    VLR_PAGAMENTO_LIQUIDO, " +
                                "    HST_PAGAMENTO," +
                                "    COD_FONTE_RECURSO, " +
                                "    COD_PESSOA, " +
                                "    SEQ_CT_PLANO_CONTA, " +
                                "    DAT_LIQUIDACAO_VENCIMENTO, " +
                                "    COD_PESSOA, " +
                                "    NOM_PESSOA, " +
                                "    coalesce(COD_PLANO_CONTA_SUBELEMENTO, '00000099000') as COD_PLANO_CONTA_SUBELEMENTO, " +
                                "    SBL_PAGAMENTO_TIPO, " +
                                "    DAT_LIQUIDACAO  " +
                                "FROM " +
                                "    dbo.VW_CT_PAGAMENTO " +
                                "WHERE " +
                                "    ANO_PAGAMENTO = ? " +
                                "AND SBL_PAGAMENTO_TIPO in ( 'RPP', 'RPNP' ) " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                                "ORDER BY " +
                                "    NRO_PAGAMENTO_ORDEM");
                stmt.setInt(1, anoSonner);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    seqPagamento = rs.getInt(1);
                    autorizacao = rs.getInt(2);
                    dataAutorizacao = rs.getDate(3);
                    dataPagamento = rs.getDate(4);
                    anoEmpenhoRestos = java.sql.Date.valueOf(rs.getInt(5) + "-01-01");
                    empenhoRestos = rs.getInt(6);
                    dataEmpenho = rs.getDate(7);
                    fichaDotacao = rs.getInt(8);
                    parcela = rs.getInt(9);
                    valorEmpenho = rs.getBigDecimal(10);
                    valorParcela = rs.getBigDecimal(11);
                    valorDesconto = rs.getBigDecimal(12);
                    valorPago = rs.getBigDecimal(13);
                    historicoPagamento = rs.getString(14).trim().toUpperCase();
                    fonteRecurso = rs.getInt(15);
                    fornecedor = rs.getInt(16);
                    fichaConta = rs.getInt(17);
                    vencimento = rs.getDate(18);
                    codCredor = rs.getInt(19);
                    nomeCredor = rs.getString(20);
                    desdobramento = rs.getString(21).trim();
                    desdobramento = desdobramento.equals("") ? "99" : desdobramento.substring(6, 8);
                    tipoRestosPagar = rs.getString(22);
                    dataLiquidacao = rs.getDate(23);

                    nroOP = getMaxOP(emLocal, anoAtual);

                    System.out.println("Autorizacaoo - " + autorizacao + " - TipoDoc: O " + " - Documento: " + nroOP + " - Parcela: 1 [" + seqPagamento + "]");

                    if (tipoRestosPagar.equals("RPP")) {

                        valorRPP = valorParcela;
                        valorRPNP = BigDecimal.ZERO;

                        RestosProcParc restosProcParc = emLocal.find(RestosProcParc.class, new RestosProcParcPK(anoEmpenhoRestos, empenhoRestos, parcela));
                        if (Objects.isNull(restosProcParc)) {
                            ultimoDiaExercicio = java.sql.Date.valueOf(anoEmpenhoRestos + "-12-31");
                            restosProcParc = new RestosProcParc();
                            restosProcParc.getId().setAnoEmpenho(anoEmpenhoRestos);
                            restosProcParc.getId().setEmpenho(empenhoRestos);
                            restosProcParc.getId().setParcela(parcela);
                            restosProcParc.setVencimento(ultimoDiaExercicio);
                            restosProcParc.setValor(valorParcela);
                            emLocal.persist(restosProcParc);
                        }
                    } else {
                        valorRPP = BigDecimal.ZERO;
                        valorRPNP = valorParcela;

                        LiquidaRestos liquidaRestos = new LiquidaRestos();
                        liquidaRestos.getId().setAnoEmpenho(anoEmpenhoRestos);
                        liquidaRestos.getId().setEmpenho(empenhoRestos);
                        liquidaRestos.getId().setLiquidacao(parcela);
                        liquidaRestos.setDataLiquidacao(dataLiquidacao);
                        liquidaRestos.setHistorico(historicoPagamento);
                        liquidaRestos.setVencimento(vencimento);
                        liquidaRestos.setValor(valorParcela);
                        emLocal.persist(liquidaRestos);

                        LiqRestCronogra liqRestCronogra = new LiqRestCronogra();
                        liqRestCronogra.getId().setAnoEmpenho(anoEmpenhoRestos);
                        liqRestCronogra.getId().setEmpenho(empenhoRestos);
                        liqRestCronogra.getId().setLiquidacao(parcela);
                        liqRestCronogra.getId().setParcela(parcela);
                        liqRestCronogra.getId().setAnoOP(anoAtual);
                        liqRestCronogra.getId().setNroOP(nroOP);
                        emLocal.persist(liqRestCronogra);
                    }

                    RestosInscritos restosInscritos = emLocal.find(RestosInscritos.class, new RestosInscritosPK(anoEmpenhoRestos, empenhoRestos));
                    if (Objects.isNull(restosInscritos)) {
                        restosInscritos = new RestosInscritos();
                        restosInscritos.getId().setAno(anoEmpenhoRestos);
                        restosInscritos.getId().setEmpenho(empenhoRestos);
                        restosInscritos.setCodCredor(codCredor);
                        restosInscritos.setNomeCredor(nomeCredor);
                        restosInscritos.setFicha(fichaDotacao);
                        restosInscritos.setSubElemento(desdobramento);
                        restosInscritos.setDataEmpenho(dataEmpenho);
                        restosInscritos.setValorProcessado(valorRPP);
                        restosInscritos.setValorNaoProcessado(valorRPNP);
                        restosInscritos.setValorEmpenho(valorEmpenho);
                        emLocal.persist(restosInscritos);

                        RestosFonteRec restosFonteRec = new RestosFonteRec();
                        restosFonteRec.getId().setAno(anoEmpenhoRestos);
                        restosFonteRec.getId().setEmpenho(empenhoRestos);
                        restosFonteRec.setVersaoRecurso(versaoRecurso);
                        restosFonteRec.setFonteRecurso(fonteRecurso);
                        restosFonteRec.setCaFixo(999);
                        restosFonteRec.setCaVariavel(0);
                        restosFonteRec.setValor(valorRPP.add(valorRPNP));
                        emLocal.persist(restosFonteRec);
                    }

                    // Cadastrando Conta Extra
                    ContaExtra contaExtra = emLocal.find(ContaExtra.class, new ContaExtraPK(anoAtual, fichaConta));
                    if (Objects.isNull(contaExtra)) {

                        tipoRestosPagar = tipoRestosPagar.equals("RPP") ? "P" : "N";

                        contaExtra = new ContaExtra();
                        contaExtra.getId().setAno(anoAtual);
                        contaExtra.getId().setContaExtra(fichaConta);
                        contaExtra.setNome("Conta Extra de Restos a Pagar");
                        contaExtra.setAnoRestos(anoEmpenhoRestos);
                        contaExtra.setTipoResto(tipoRestosPagar);
                        contaExtra.setEmpresa(5);
                        emLocal.persist(contaExtra);
                    }

                    OrdensPagto ordensPagto = new OrdensPagto();
                    ordensPagto.getId().setAno(anoAtual);
                    ordensPagto.getId().setNumero(nroOP);
                    ordensPagto.setFicha(fichaConta);
                    ordensPagto.setFornecedor(fornecedor);
                    ordensPagto.setData(dataPagamento);
                    ordensPagto.setHistorico(historicoPagamento);
                    ordensPagto.setValorOP(valorParcela);
                    ordensPagto.setVencimento(dataPagamento);
                    ordensPagto.setDesconto(valorDesconto);
                    ordensPagto.setAnoRestos(anoEmpenhoRestos);
                    ordensPagto.setEmpenhoRestos(empenhoRestos);
                    ordensPagto.setParcelaRestos(parcela);
                    ordensPagto.setValorProcessado(valorRPP);
                    ordensPagto.setValorNaoProcessado(valorRPNP);
                    emLocal.persist(ordensPagto);

                    PagtoOP pagtoOP = new PagtoOP();
                    pagtoOP.getId().setAno(anoAtual);
                    pagtoOP.getId().setNumero(nroOP);
                    pagtoOP.setDataPagto(dataPagamento);
                    pagtoOP.setValorPagto(valorPago);
                    emLocal.persist(pagtoOP);

                    OPFonteRecurso opFonteRecurso = new OPFonteRecurso();
                    opFonteRecurso.getId().setAno(anoAtual);
                    opFonteRecurso.getId().setNumero(nroOP);
                    opFonteRecurso.setVersaoRecurso(versaoRecurso);
                    opFonteRecurso.setFonteRecurso(fonteRecurso);
                    opFonteRecurso.setCaFixo(999);
                    opFonteRecurso.setCaVariavel(0);
                    opFonteRecurso.setValor(valorParcela);
                    emLocal.persist(opFonteRecurso);

                    AutPagto autPagto = new AutPagto();
                    autPagto.getId().setAno(anoAtual);
                    autPagto.getId().setNumero(autorizacao);
                    autPagto.setDataAutorizacao(dataAutorizacao);
                    autPagto.setDataPagamento(dataPagamento);
                    autPagto.setDescricao(historicoPagamento);
                    emLocal.persist(autPagto);

                    ItensAutPagto itensAutPagto = new ItensAutPagto();
                    itensAutPagto.getId().setAno(anoAtual);
                    itensAutPagto.getId().setAutorizacao(autorizacao);
                    itensAutPagto.getId().setTipoDoc(ORDEMPAGTO);
                    itensAutPagto.getId().setDocumento(nroOP);
                    itensAutPagto.getId().setParcela(1);
                    emLocal.persist(itensAutPagto);

                    // Pegando Desconto
                    if (valorDesconto.signum() > 0) {
                        desconto(emLocal, con, anoAtual, seqPagamento, autorizacao, ORDEMPAGTO, nroOP, 1, versaoRecurso, dataPagamento);
                    }

                    // Pagamento
                    financeiro(emLocal, con, anoAtual, seqPagamento, autorizacao, ORDEMPAGTO, nroOP, 1, versaoRecurso, dataPagamento);
                }
            }
        } catch (SQLException e) {
            System.out.println("Ops");
            e.printStackTrace();
        } finally {
            closeConexao(con, stmt, rs);
        }
    }

    private static int getMaxOP(EntityManager em, Date ano) {
        Integer max = 0;
        Query q = em.createQuery("Select MAX(a.id.numero) from OrdensPagto a where a.id.ano = :ano ")
                .setParameter("ano", ano);
        max = (Integer) q.getSingleResult();
        max = (max == null) ? 0 : max;
        max++;
        return max;
    }

    private static void delete(Date anoAtual) {
        delete("CBPAUTPAGTO", anoAtual);
        delete("CBPITENSAUTPAGTO", anoAtual);
        delete("CBPAUTPAGTOFONTREC", anoAtual);
        delete("CBPDESCONTOSPAGTO", anoAtual);
        delete("CBPDESCONTOSOP", anoAtual);
        delete("CBPDOCPAGTO", anoAtual);

        delete("CBPRESTOSINSCRITOS");
        delete("CBPRESTOSPROCPARC");
        delete("CBPRESTOSFONTEREC");

        delete("CBPORDENSPAGTO", anoAtual);
        delete("CBPPAGTOOPS", anoAtual);
        delete("CBPOPFONTERECURSO", anoAtual);

        deleteQuery("Delete from CBPITENSGUIA A Where A.ANO = :ano " +
                " AND EXISTS ( SELECT * from CBPGUIARECEITA B Where A.ANO = B.ANO AND A.TIPO = B.TIPO AND A.GUIA = B.NUMERO AND B.ORIGEM = 'AUT' )", anoAtual);
        deleteQuery("Delete from CBPGUIARECEITA Where ANO = :ano and ORIGEM = 'AUT' ", anoAtual);

        deleteQuery("Delete from CBPDEBITO where ANOLANCTO = :ano and AUTPAGTO > 0 ", anoAtual);
        deleteQuery("Delete from CBPCHEQUE where ANOLANCTO = :ano and AUTPAGTO > 0 ", anoAtual);

        deleteQuery("Delete from CBPLIQUIDARESTOS A Where " +
                " EXISTS ( SELECT * from CBPLIQRESTCRONOGRA B Where A.ANOEMPENHO = B.ANOEMPENHO AND A.EMPENHO = B.EMPENHO AND A.LIQUIDACAO = B.LIQUIDACAO AND B.ANOOP = :ano )", anoAtual);
        deleteQuery("Delete from CBPLIQRESTCRONOGRA where ANOOP = :ano ", anoAtual);
    }

    private static void financeiro(EntityManager emLocal, Connection con, Date anoAtual, Integer seqPagamento, Integer autorizacao, String tipoDoc, Integer documento, Integer parcela, Integer versaoRecurso, Date dataPagamento) throws SQLException {
        PreparedStatement stmt2;
        ResultSet rs2;
        BigDecimal valorPago;
        Integer fonteRecurso, banco, fichaBancaria, nroCheque;
        String contaBancaria, formaPagamento, agencia;

        stmt2 = con.prepareStatement(
                "SELECT " +
                        "    SBL_PAGAMENTO_BANCO_DOCUMENTO, " +
                        "    COALESCE(NRO_PAGAMENTO_BANCO_DOCUMENTO,0) AS NRO_PAGAMENTO_BANCO_DOCUMENTO, " +
                        "    CASE WHEN COD_BANCO = 'CEF' THEN '104' " +
                        "       ELSE COD_BANCO END AS COD_BANCO, " +
                        "    NRO_CONTA_AGENCIA, " +
                        "    NRO_CONTA, " +
                        "    NRO_CONTA_REDUZIDO, " +
                        "    COD_FONTE_RECURSO_BANCO, " +
                        "    VLR_PAGAMENTO_BANCO " +
                        "FROM " +
                        "    dbo.VW_CT_PAGAMENTO " +
                        "WHERE " +
                        "    SEQ_CT_PAGAMENTO = ? " +
                        "AND SBL_PAGAMENTO_BANCO_DOCUMENTO IS NOT NULL " +
                        "ORDER BY " +
                        "    NRO_PAGAMENTO_ORDEM ");
        stmt2.setInt(1, seqPagamento);
        rs2 = stmt2.executeQuery();
        while (rs2.next()) {
            formaPagamento = rs2.getString(1).trim();
            nroCheque = rs2.getInt(2);
            banco = rs2.getInt(3);
            agencia = rs2.getString(4).trim();
            contaBancaria = rs2.getString(5).trim();
            fichaBancaria = rs2.getInt(6);
            fonteRecurso = rs2.getInt(7);
            valorPago = rs2.getBigDecimal(8);

            Integer max = 0;
            if (formaPagamento.equals("DB") || formaPagamento.equals("RD")) {
                Query q = emLocal.createQuery("Select MAX(a.id.numero) from Debito a where a.id.fichaConta = :ficha  and a.id.data = :data ")
                        .setParameter("ficha", fichaBancaria)
                        .setParameter("data", dataPagamento);
                max = (Integer) q.getSingleResult();
                max = (max == null) ? 0 : max;
                max++;

                Debito debito = new Debito();
                debito.getId().setFichaConta(fichaBancaria);
                debito.getId().setData(dataPagamento);
                debito.getId().setNumero(max);
                debito.setBanco(banco);
                debito.setAgencia(agencia);
                debito.setConta(contaBancaria);
                debito.setHistorico("Pagamento da Autorização: " + autorizacao);
                debito.setAnoLancto(anoAtual);
                debito.setLancamento(-1);
                debito.setValor(valorPago);
                debito.setVersaoRecurso(versaoRecurso);
                debito.setFonteRecurso(fonteRecurso);
                debito.setCaFixo(999);
                debito.setCaVariavel(0);
                debito.setFinalidade("P");
                debito.setAutPagto(autorizacao);
                emLocal.persist(debito);
            } else if (formaPagamento.equals("CH")) {
                Cheque cheque = new Cheque();
                cheque.getId().setFichaConta(fichaBancaria);
                cheque.getId().setData(dataPagamento);
                cheque.getId().setNumero(nroCheque);
                cheque.getId().setHistorico("Pagamento da Autorização: " + autorizacao);
                cheque.setBanco(banco);
                cheque.setAgencia(agencia);
                cheque.setConta(contaBancaria);
                cheque.setAnoLancto(anoAtual);
                cheque.setLancamento(-1);
                cheque.setValor(valorPago);
                cheque.setVersaoRecurso(versaoRecurso);
                cheque.setFonteRecurso(fonteRecurso);
                cheque.setCaFixo(999);
                cheque.setCaVariavel(0);
                cheque.setFinalidade("P");
                cheque.setAutPagto(autorizacao);
                cheque.setDataEmissao(dataPagamento);
                cheque.setDataBaixa(dataPagamento);
                emLocal.persist(cheque);
            }

            AutPagtoFonteRec autPagtoFonteRec = new AutPagtoFonteRec();
            autPagtoFonteRec.getId().setAno(anoAtual);
            autPagtoFonteRec.getId().setAutorizacao(autorizacao);
            autPagtoFonteRec.getId().setFichaBanco(fichaBancaria);
            autPagtoFonteRec.getId().setTipoDoc(tipoDoc);
            autPagtoFonteRec.getId().setDocumento(documento);
            autPagtoFonteRec.getId().setParcela(parcela);
            autPagtoFonteRec.getId().setVersaoRecurso(versaoRecurso);
            autPagtoFonteRec.getId().setFonteRecurso(fonteRecurso);
            autPagtoFonteRec.getId().setCaFixo(999);
            autPagtoFonteRec.getId().setCaVariavel(0);
            autPagtoFonteRec.getId().setSequencial(1);
            autPagtoFonteRec.setValor(valorPago);
            emLocal.persist(autPagtoFonteRec);
        }
    }

    private static void desconto(EntityManager em, Connection con, Date anoAtual, Integer seqPagamento, Integer autorizacao, String tipoDoc, Integer documento, Integer parcela, Integer versaoRecurso, Date dataGuia) {
        PreparedStatement stmtAux;
        ResultSet rsAux;
        Integer fichaReceita, guia;
        String tipoReceita = "E";
        BigDecimal valorReceita;

        GuiaReceita guiaReceita;
        ItensGuiaReceita itensGuiaReceita;
        DescontosPagto descontosPagto;
        DescontosOP descontosOP;
        Query q;

        // Deletando Descontos Temp
        em.createNativeQuery("Delete from CBPDESCONTOSTEMP a where a.ANO = :ano and a.TIPODOC = :tipoDoc and a.DOCUMENTO = :documento and a.PARCELA = :parcela ")
                .setParameter("ano", anoAtual)
                .setParameter("tipoDoc", tipoDoc)
                .setParameter("documento", documento)
                .setParameter("parcela", parcela)
                .executeUpdate();

        try {
            stmtAux = con.prepareStatement(
                    "SELECT " +
                            "    SEQ_CT_DESCONTO, " +
                            "    VLR_PAGAMENTO_DESCONTO " +
                            "FROM " +
                            "    dbo.CT_PAGAMENTO_DESCONTO " +
                            "WHERE " +
                            "    SEQ_CT_PAGAMENTO = ? " +
                            "ORDER BY 1 ");
            stmtAux.setInt(1, seqPagamento);
            rsAux = stmtAux.executeQuery();
            while (rsAux.next()) {
                fichaReceita = rsAux.getInt(1);
                valorReceita = rsAux.getBigDecimal(2);

                q = em.createQuery("Select MAX(a.id.numero) from GuiaReceita a where a.id.ano = :ano  and a.id.tipo = :tipo ")
                        .setParameter("ano", anoAtual)
                        .setParameter("tipo", tipoReceita);
                guia = (Integer) q.getSingleResult();
                guia = (guia == null) ? 0 : guia;
                guia++;

                guiaReceita = new GuiaReceita();
                guiaReceita.getId().setAno(anoAtual);
                guiaReceita.getId().setTipo(tipoReceita);
                guiaReceita.getId().setNumero(guia);
                guiaReceita.setContribuinte(1);
                guiaReceita.setDataGuia(dataGuia);
                guiaReceita.setVencimento(dataGuia);
                guiaReceita.setRecebimento(dataGuia);
                guiaReceita.setHistorico("Guia de Receita de desconto - Autorização: " + autorizacao);
                guiaReceita.setOrigem("AUT");
                guiaReceita.setAutPagto(autorizacao);
                em.persist(guiaReceita);

                if (tipoDoc.equals("E")) {
                    descontosPagto = new DescontosPagto();
                    descontosPagto.getId().setAno(anoAtual);
                    descontosPagto.getId().setEmpenho(documento);
                    descontosPagto.getId().setParcela(parcela);
                    descontosPagto.getId().setTipoGuia(tipoReceita);
                    descontosPagto.getId().setGuiaReceita(guia);
                    em.persist(descontosPagto);
                } else {
                    descontosOP = new DescontosOP();
                    descontosOP.getId().setAno(anoAtual);
                    descontosOP.getId().setOp(documento);
                    descontosOP.getId().setParcela(1);
                    descontosOP.getId().setTipoGuia(tipoReceita);
                    descontosOP.getId().setGuiaReceita(guia);
                    em.persist(descontosOP);
                }

                itensGuiaReceita = new ItensGuiaReceita();
                itensGuiaReceita.getId().setAno(anoAtual);
                itensGuiaReceita.getId().setTipo(tipoReceita);
                itensGuiaReceita.getId().setGuia(guia);
                itensGuiaReceita.getId().setFicha(fichaReceita);
                itensGuiaReceita.getId().setVersaoRecurso(versaoRecurso);
                itensGuiaReceita.getId().setFonteRecurso(110);
                itensGuiaReceita.getId().setCaFixo(999);
                itensGuiaReceita.getId().setCaVariavel(0);
                itensGuiaReceita.setReceita(Integer.toString(fichaReceita));
                itensGuiaReceita.setValor(valorReceita);
                em.persist(itensGuiaReceita);
            }
            stmtAux.close();
            rsAux.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
