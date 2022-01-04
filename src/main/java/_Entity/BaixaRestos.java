package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPBAIXARESTOS")
public class BaixaRestos implements Serializable {

    @EmbeddedId
    private BaixaRestosPK id;

    @Temporal(TemporalType.DATE)
    private Date dataBaixa;
    private BigDecimal valorBaixa;

    @Column(name = "VALORNP")
    private BigDecimal valorNaoProcessado;

    @Column(name = "VALORP")
    private BigDecimal valorProcessado;

    public BaixaRestos() {
        this.id = new BaixaRestosPK();
    }

    public BaixaRestos(Date ano, Integer empenho, Date anoOP, Integer nroOP, Date dataBaixa, BigDecimal valorBaixa, BigDecimal valorNaoProcessado, BigDecimal valorProcessado) {
        this.id = new BaixaRestosPK(ano, empenho, anoOP, nroOP);
        this.dataBaixa = dataBaixa;
        this.valorBaixa = valorBaixa;
        this.valorNaoProcessado = valorNaoProcessado;
        this.valorProcessado = valorProcessado;
    }

    public BaixaRestosPK getId() {
        return id;
    }

    public void setId(BaixaRestosPK id) {
        this.id = id;
    }

    public Date getDataBaixa() {
        return dataBaixa;
    }

    public void setDataBaixa(Date dataBaixa) {
        this.dataBaixa = dataBaixa;
    }

    public BigDecimal getValorBaixa() {
        return valorBaixa;
    }

    public void setValorBaixa(BigDecimal valorBaixa) {
        this.valorBaixa = valorBaixa;
    }

    public BigDecimal getValorNaoProcessado() {
        return valorNaoProcessado;
    }

    public void setValorNaoProcessado(BigDecimal valorNaoProcessado) {
        this.valorNaoProcessado = valorNaoProcessado;
    }

    public BigDecimal getValorProcessado() {
        return valorProcessado;
    }

    public void setValorProcessado(BigDecimal valorProcessado) {
        this.valorProcessado = valorProcessado;
    }
}
