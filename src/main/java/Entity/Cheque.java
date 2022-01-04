package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPCHEQUE")
public class Cheque implements Serializable {

    @EmbeddedId
    private ChequePK id;
    private Integer banco;
    private String agencia;
    private String conta;

    @Temporal(TemporalType.DATE)
    private Date anoLancto;
    private Integer lancamento;
    private Integer anulacaoReceita;
    private Integer transferencia;
    private Integer versaoRecurso;
    private Integer fonteRecurso;
    private Integer caFixo;
    private Integer caVariavel;
    private String finalidade;
    private Integer autPagto;

    @Temporal(TemporalType.DATE)
    private Date dataEmissao;

    @Temporal(TemporalType.DATE)
    private Date dataBaixa;
    private BigDecimal valor;

    public Cheque() {
        this.id = new ChequePK();
    }

    public Cheque(Integer fichaConta, Integer banco, String agencia, String conta, Integer numero, Date data, String historico, Date anoLancto, Integer anulacaoReceita, Integer transferencia, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, String finalidade, Integer autPagto, Date dataEmissao, Date dataBaixa, BigDecimal valor) {
        this.id = new ChequePK(fichaConta, numero, data, historico);
        this.banco = banco;
        this.agencia = agencia;
        this.conta = conta;
        this.anoLancto = anoLancto;
        this.lancamento = -1;
        this.anulacaoReceita = anulacaoReceita;
        this.transferencia = transferencia;
        this.versaoRecurso = versaoRecurso;
        this.fonteRecurso = fonteRecurso;
        this.caFixo = caFixo;
        this.caVariavel = caVariavel;
        this.finalidade = finalidade;
        this.autPagto = autPagto;
        this.dataEmissao = dataEmissao;
        this.dataBaixa = dataBaixa;
        this.valor = valor;
    }

    public ChequePK getId() {
        return id;
    }

    public void setId(ChequePK id) {
        this.id = id;
    }

    public Integer getBanco() {
        return banco;
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Date getAnoLancto() {
        return anoLancto;
    }

    public void setAnoLancto(Date anoLancto) {
        this.anoLancto = anoLancto;
    }

    public Integer getLancamento() {
        return lancamento;
    }

    public void setLancamento(Integer lancamento) {
        this.lancamento = lancamento;
    }

    public Integer getAnulacaoReceita() {
        return anulacaoReceita;
    }

    public void setAnulacaoReceita(Integer anulacaoReceita) {
        this.anulacaoReceita = anulacaoReceita;
    }

    public Integer getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Integer transferencia) {
        this.transferencia = transferencia;
    }

    public Integer getVersaoRecurso() {
        return versaoRecurso;
    }

    public void setVersaoRecurso(Integer versaoRecurso) {
        this.versaoRecurso = versaoRecurso;
    }

    public Integer getFonteRecurso() {
        return fonteRecurso;
    }

    public void setFonteRecurso(Integer fonteRecurso) {
        this.fonteRecurso = fonteRecurso;
    }

    public Integer getCaFixo() {
        return caFixo;
    }

    public void setCaFixo(Integer caFixo) {
        this.caFixo = caFixo;
    }

    public Integer getCaVariavel() {
        return caVariavel;
    }

    public void setCaVariavel(Integer caVariavel) {
        this.caVariavel = caVariavel;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

    public Integer getAutPagto() {
        return autPagto;
    }

    public void setAutPagto(Integer autPagto) {
        this.autPagto = autPagto;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Date getDataBaixa() {
        return dataBaixa;
    }

    public void setDataBaixa(Date dataBaixa) {
        this.dataBaixa = dataBaixa;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
