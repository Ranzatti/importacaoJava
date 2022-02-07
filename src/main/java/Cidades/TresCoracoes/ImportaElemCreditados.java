package Cidades.TresCoracoes;

import _Entity.*;
import _Infra.Util;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ImportaElemCreditados extends Util {

    public static void main(String[] args) {
        init(2021);
    }

    public static void init(int anoSonner) {

        EntityManager emLocal = conexaoDestino("TresCoracoes");

        Connection con = conexaoOrigemSQLServer();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        SimpleDateFormat sdfMes = new SimpleDateFormat("MM");

        Integer codigo, fichaCredito, versaoRecurso, fonteRecursoCredito, fichaAnulada, fonteRecursoAnulada, lei, mes;
        BigDecimal valor;
        String teste;
        Date anoAtual, data, dataLei;

        anoAtual = java.sql.Date.valueOf(anoSonner + "-01-01");

        versaoRecurso = getVersao(anoSonner);

        delete("CBPCRDANL", anoAtual);
        delete("CBPELEMCREDITADOS", anoAtual);
        delete("CBPELEMCREDMES", anoAtual);
        delete("CBPELEMANULADOS", anoAtual);
        delete("CBPELEMANULMES", anoAtual);
        delete("CBPELEMDISTCREDANU", anoAtual);

        emLocal.getTransaction().begin();

        teste = "";
        //teste = "and NRO_CREDITO_ADICIONAL = 4289 ";

        System.out.println("INICIANDO IMPORTAÇÃO ELEMDESPESA: " + anoSonner);
        try {
            // importando elem despesa
            stmt = con.prepareStatement(
                    "SELECT distinct " +
                            "    NRO_CREDITO_ADICIONAL, " +
                            "    DAT_CREDITO_ADICIONAL, " +
                            "    NRO_CREDITO_ADICIONAL_LEI, " +
                            "    DAT_CREDITO_ADICIONAL_LEI " +
                            "FROM " +
                            "    VW_CT_CREDITO_ADICIONAL " +
                            "WHERE " +
                            "    ANO_CREDITO_ADICIONAL = ? " +
                            "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                            "AND SBL_CREDITO_ADICIONAL_MOVIMENTO_RECURSO = '03' " + teste +
                            "Order by 1 ");
            stmt.setInt(1, anoSonner);
            rs = stmt.executeQuery();
            while (rs.next()) {
                codigo = rs.getInt(1);
                data = rs.getDate(2);
                lei = rs.getInt(3);
                dataLei = rs.getDate(4);

                mes = Integer.parseInt(sdfMes.format(data));

                System.out.println("Codigo: " + codigo);

                CreditoDotacao creditoDotacao = emLocal.find(CreditoDotacao.class, new CreditoDotacaoPK(anoAtual, codigo));
                if (Objects.isNull(creditoDotacao)) {
                    creditoDotacao = new CreditoDotacao();
                    creditoDotacao.getId().setAno(anoAtual);
                    creditoDotacao.getId().setCodigo(codigo);
                    creditoDotacao.setDataCredito(data);
                    creditoDotacao.setTipo("S");
                    creditoDotacao.setHistorico("Crédito Suplementar");
                    creditoDotacao.setNatureza("A");
                    creditoDotacao.setLei(lei);
                    creditoDotacao.setDataLei(dataLei);
                    creditoDotacao.setTransposicao(0);
                    creditoDotacao.setTransposicaoFonte(0);
                    emLocal.persist(creditoDotacao);
                }

                // Ficha Credito
                stmt2 = con.prepareStatement(
                        "SELECT " +
                                "    NRO_ORCAMENTO_DESPESA_FICHA_SUPLEMENTADA, " +
                                "    COD_FONTE_RECURSO_SUPLEMENTADA, " +
                                "    SUM(VLR_CREDITO_ADICIONAL_SUPLEMENTADA ) " +
                                "FROM " +
                                "    VW_CT_CREDITO_ADICIONAL " +
                                "WHERE " +
                                "    ANO_CREDITO_ADICIONAL = ? " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                                "AND NRO_CREDITO_ADICIONAL = ? " +
                                "GROUP BY NRO_ORCAMENTO_DESPESA_FICHA_SUPLEMENTADA, COD_FONTE_RECURSO_SUPLEMENTADA ");
                stmt2.setInt(1, anoSonner);
                stmt2.setInt(2, codigo);
                rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    fichaCredito = rs2.getInt(1);
                    fonteRecursoCredito = rs2.getInt(2);
                    valor = rs2.getBigDecimal(3);

                    ElemCreditados elemCreditados = new ElemCreditados();
                    elemCreditados.getId().setAno(anoAtual);
                    elemCreditados.getId().setCodigo(codigo);
                    elemCreditados.getId().setFicha(fichaCredito);
                    elemCreditados.getId().setVersaoRecurso(versaoRecurso);
                    elemCreditados.getId().setFonteRecurso(fonteRecursoCredito);
                    elemCreditados.getId().setCaFixo(999);
                    elemCreditados.getId().setCaVariavel(0);
                    elemCreditados.setValor(valor);
                    emLocal.persist(elemCreditados);

                    ElemCredMes elemCredMes = new ElemCredMes();
                    elemCredMes.getId().setAno(anoAtual);
                    elemCredMes.getId().setCodigo(codigo);
                    elemCredMes.getId().setFicha(fichaCredito);
                    elemCredMes.getId().setVersaoRecurso(versaoRecurso);
                    elemCredMes.getId().setFonteRecurso(fonteRecursoCredito);
                    elemCredMes.getId().setCaFixo(999);
                    elemCredMes.getId().setCaVariavel(0);
                    elemCredMes.getId().setMes(mes);
                    elemCredMes.setValor(valor);
                    emLocal.persist(elemCredMes);
                }
                stmt2.close();
                rs2.close();


                // Ficha Anulada
                stmt2 = con.prepareStatement(
                        "SELECT " +
                                "    NRO_ORCAMENTO_DESPESA_FICHA_ANULADA, " +
                                "    COD_FONTE_RECURSO_ANULADA, " +
                                "    SUM(VLR_CREDITO_ADICIONAL_ANULADA) " +
                                "FROM " +
                                "    VW_CT_CREDITO_ADICIONAL " +
                                "WHERE " +
                                "    ANO_CREDITO_ADICIONAL = ? " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                                "AND NRO_CREDITO_ADICIONAL = ? " +
                                "GROUP BY NRO_ORCAMENTO_DESPESA_FICHA_ANULADA, COD_FONTE_RECURSO_ANULADA ");
                stmt2.setInt(1, anoSonner);
                stmt2.setInt(2, codigo);
                rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    fichaAnulada = rs2.getInt(1);
                    fonteRecursoAnulada = rs2.getInt(2);
                    valor = rs2.getBigDecimal(3);

                    ElemAnulados elemAnulados = new ElemAnulados();
                    elemAnulados.getId().setAno(anoAtual);
                    elemAnulados.getId().setCodigo(codigo);
                    elemAnulados.getId().setFicha(fichaAnulada);
                    elemAnulados.getId().setVersaoRecurso(versaoRecurso);
                    elemAnulados.getId().setFonteRecurso(fonteRecursoAnulada);
                    elemAnulados.getId().setCaFixo(999);
                    elemAnulados.getId().setCaVariavel(0);
                    elemAnulados.getId().setCaVariavel(0);
                    elemAnulados.setValor(valor);
                    emLocal.persist(elemAnulados);

                    ElemAnulMes elemAnulMes = new ElemAnulMes();
                    elemAnulMes.getId().setAno(anoAtual);
                    elemAnulMes.getId().setCodigo(codigo);
                    elemAnulMes.getId().setFicha(fichaAnulada);
                    elemAnulMes.getId().setVersaoRecurso(versaoRecurso);
                    elemAnulMes.getId().setFonteRecurso(fonteRecursoAnulada);
                    elemAnulMes.getId().setCaFixo(999);
                    elemAnulMes.getId().setCaVariavel(0);
                    elemAnulMes.getId().setMes(mes);
                    elemAnulMes.setValor(valor);
                    emLocal.persist(elemAnulMes);
                }
                stmt2.close();
                rs2.close();

                // DISTRIBUIÇÃO
                stmt2 = con.prepareStatement(
                        "SELECT " +
                                "    NRO_ORCAMENTO_DESPESA_FICHA_SUPLEMENTADA, " +
                                "    COD_FONTE_RECURSO_SUPLEMENTADA, " +
                                "    NRO_ORCAMENTO_DESPESA_FICHA_ANULADA, " +
                                "    COD_FONTE_RECURSO_ANULADA, " +
                                "    VLR_CREDITO_ADICIONAL_MOVIMENTO " +
                                "FROM " +
                                "    VW_CT_CREDITO_ADICIONAL " +
                                "WHERE " +
                                "    ANO_CREDITO_ADICIONAL = ? " +
                                "AND SEQ_CT_UNIDADE_GESTORA = 21 " +
                                "AND NRO_CREDITO_ADICIONAL = ? ");
                stmt2.setInt(1, anoSonner);
                stmt2.setInt(2, codigo);
                rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    fichaCredito = rs2.getInt(1);
                    fonteRecursoCredito = rs2.getInt(2);
                    fichaAnulada = rs2.getInt(3);
                    fonteRecursoAnulada = rs2.getInt(4);
                    valor = rs2.getBigDecimal(5);

                    ElemCredDistrib elemCredDistrib = new ElemCredDistrib();
                    elemCredDistrib.getId().setAno(anoAtual);
                    elemCredDistrib.getId().setCodigo(codigo);
                    elemCredDistrib.getId().setFichaCredito(fichaCredito);
                    elemCredDistrib.getId().setVersaoRecCredito(versaoRecurso);
                    elemCredDistrib.getId().setFonteRecursoCredito(fonteRecursoCredito);
                    elemCredDistrib.getId().setCaFixoCredito(999);
                    elemCredDistrib.getId().setCaVariavelCredito(0);
                    elemCredDistrib.getId().setFichaAnulacao(fichaAnulada);
                    elemCredDistrib.getId().setVersaoRecAnulacao(versaoRecurso);
                    elemCredDistrib.getId().setFonteRecursoAnulacao(fonteRecursoAnulada);
                    elemCredDistrib.getId().setCaFixoAnulacao(999);
                    elemCredDistrib.getId().setCaVariavelAnulacao(0);
                    elemCredDistrib.setValor(valor);
                    emLocal.persist(elemCredDistrib);
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
}
