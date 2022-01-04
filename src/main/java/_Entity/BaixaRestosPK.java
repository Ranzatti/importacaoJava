package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class BaixaRestosPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private Integer empenho;

    @Temporal(TemporalType.DATE)
    private Date anoOP;
    private Integer nroOP;

    public BaixaRestosPK() {
    }

    public BaixaRestosPK(Date ano, Integer empenho, Date anoOP, Integer nroOP) {
        this.ano = ano;
        this.empenho = empenho;
        this.anoOP = anoOP;
        this.nroOP = nroOP;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public Integer getEmpenho() {
        return empenho;
    }

    public void setEmpenho(Integer empenho) {
        this.empenho = empenho;
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
