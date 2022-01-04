package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPORDENSPAGTO")
public class OrdensPagto implements Serializable {

    @EmbeddedId
    private OrdensPagtoPK id;
    private Integer ficha;
    private Integer fornecedor;

    @Column(name = "DATAOP")
    @Temporal(TemporalType.DATE)
    private Date data;

    private String historico;
    private BigDecimal valorOP;

    @Temporal(TemporalType.DATE)
    private Date vencimento;
    private String status;
    private BigDecimal desconto;

    @Temporal(TemporalType.DATE)
    private Date anoRestos;
    private Integer empenhoRestos;
    private Integer parcelaRestos;

    @Column(name = "VALORP")
    private BigDecimal valorProcessado;

    @Column(name = "VALORNP")
    private BigDecimal valorNaoProcessado;

    @Temporal(TemporalType.DATE)
    private Date anoLancto;
    private Integer lancamento;

    public OrdensPagto() {
        this.id = new OrdensPagtoPK();
    }

    public OrdensPagto(Date ano, Integer numero, Integer ficha, Integer fornecedor, Date data, String historico, BigDecimal valorOP, Date vencimento, BigDecimal desconto, Date anoRestos, Integer empenhoRestos, Integer parcelaRestos,
                       BigDecimal valorProcessado, BigDecimal valorNaoProcessado) {
        this.id = new OrdensPagtoPK(ano, numero);
        this.ficha = ficha;
        this.fornecedor = fornecedor;
        this.data = data;
        this.historico = historico;
        this.valorOP = valorOP;
        this.vencimento = vencimento;
        this.status = "A";
        this.desconto = desconto;
        this.anoRestos = anoRestos;
        this.empenhoRestos = empenhoRestos;
        this.parcelaRestos = parcelaRestos;
        this.valorProcessado = valorProcessado;
        this.valorNaoProcessado = valorNaoProcessado;
        this.anoLancto = null;
        this.lancamento = -1;
    }

    public OrdensPagtoPK getId() {
        return id;
    }

    public void setId(OrdensPagtoPK id) {
        this.id = id;
    }

    public Integer getFicha() {
        return ficha;
    }

    public void setFicha(Integer ficha) {
        this.ficha = ficha;
    }

    public Integer getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Integer fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public BigDecimal getValorOP() {
        return valorOP;
    }

    public void setValorOP(BigDecimal valorOP) {
        this.valorOP = valorOP;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public Date getAnoRestos() {
        return anoRestos;
    }

    public void setAnoRestos(Date anoRestos) {
        this.anoRestos = anoRestos;
    }

    public Integer getEmpenhoRestos() {
        return empenhoRestos;
    }

    public void setEmpenhoRestos(Integer empenhoRestos) {
        this.empenhoRestos = empenhoRestos;
    }

    public Integer getParcelaRestos() {
        return parcelaRestos;
    }

    public void setParcelaRestos(Integer parcelaRestos) {
        this.parcelaRestos = parcelaRestos;
    }

    public BigDecimal getValorProcessado() {
        return valorProcessado;
    }

    public void setValorProcessado(BigDecimal valorProcessado) {
        this.valorProcessado = valorProcessado;
    }

    public BigDecimal getValorNaoProcessado() {
        return valorNaoProcessado;
    }

    public void setValorNaoProcessado(BigDecimal valorNaoProcessado) {
        this.valorNaoProcessado = valorNaoProcessado;
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
}
