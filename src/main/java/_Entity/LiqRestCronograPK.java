package _Entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class LiqRestCronograPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date anoEmpenho;
    private Integer empenho;
    private Integer liquidacao;
    private Integer parcela;

    @Temporal(TemporalType.DATE)
    private Date anoOP;

    @Column(name = "OP")
    private Integer nroOP;

    public LiqRestCronograPK() {
    }

    public LiqRestCronograPK(Date anoEmpenho, Integer empenho, Integer liquidacao, Integer parcela, Date anoOP, Integer nroOP) {
        this.anoEmpenho = anoEmpenho;
        this.empenho = empenho;
        this.liquidacao = liquidacao;
        this.parcela = parcela;
        this.anoOP = anoOP;
        this.nroOP = nroOP;
    }

    public Date getAnoEmpenho() {
        return anoEmpenho;
    }

    public void setAnoEmpenho(Date anoEmpenho) {
        this.anoEmpenho = anoEmpenho;
    }

    public Integer getEmpenho() {
        return empenho;
    }

    public void setEmpenho(Integer empenho) {
        this.empenho = empenho;
    }

    public Integer getLiquidacao() {
        return liquidacao;
    }

    public void setLiquidacao(Integer liquidacao) {
        this.liquidacao = liquidacao;
    }

    public Integer getParcela() {
        return parcela;
    }

    public void setParcela(Integer parcela) {
        this.parcela = parcela;
    }

    public Date getAnoOP() {
        return anoOP;
    }

    public void setAnoOP(Date anoOP) {
        this.anoOP = anoOP;
    }

    public Integer getNroOP() {
        return nroOP;
    }

    public void setNroOP(Integer nroOP) {
        this.nroOP = nroOP;
    }
}
