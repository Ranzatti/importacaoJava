package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPPAGAMENTOS")
public class Pagamentos implements Serializable {

    @EmbeddedId
    private PagamentosPK id;

    @Temporal(TemporalType.DATE)
    private Date dataPagamento;

    @Temporal(TemporalType.DATE)
    private Date dataSubEmpenho;

    @Temporal(TemporalType.DATE)
    private Date vencimento;

    private String historico;
    private BigDecimal valorParcela;
    private BigDecimal valorPagamento;
    private BigDecimal desconto;
    private Integer seqImportacao;

    public Pagamentos() {
        this.id = new PagamentosPK();
    }

    public Pagamentos(Date ano, Integer empenho, Integer pagamento, Date dataSubEmpenho, Date vencimento, String historico, BigDecimal valorParcela, Date dataPagamento, BigDecimal valorPagamento, BigDecimal desconto, Integer seqImportacao) {
        this.id = new PagamentosPK(ano, empenho, pagamento);
        this.dataPagamento = dataPagamento;
        this.dataSubEmpenho = dataSubEmpenho;
        this.vencimento = vencimento;
        this.historico = historico;
        this.valorParcela = valorParcela;
        this.valorPagamento = valorPagamento;
        this.desconto = desconto;
        this.seqImportacao = seqImportacao;
    }

    public PagamentosPK getId() {
        return id;
    }

    public void setId(PagamentosPK id) {
        this.id = id;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public BigDecimal getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(BigDecimal valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Date getDataSubEmpenho() {
        return dataSubEmpenho;
    }

    public void setDataSubEmpenho(Date dataSubEmpenho) {
        this.dataSubEmpenho = dataSubEmpenho;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public Integer getSeqImportacao() {
        return seqImportacao;
    }

    public void setSeqImportacao(Integer seqImportacao) {
        this.seqImportacao = seqImportacao;
    }
}
