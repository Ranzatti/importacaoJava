package _Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class EmpenhosPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;

    @Column(name = "NUMERO")
    private Integer empenho;

    public EmpenhosPK() {
    }

    public EmpenhosPK(Date ano, Integer numero) {
        this.ano = ano;
        this.empenho = numero;
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
}
