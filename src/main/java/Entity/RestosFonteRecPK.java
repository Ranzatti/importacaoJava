package Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class RestosFonteRecPK implements Serializable {

    @Temporal(TemporalType.DATE)
    @Column(name = "ANO")
    private Date anoEmpenho;

    private Integer empenho;

    public RestosFonteRecPK() {
    }

    public RestosFonteRecPK(Date anoEmpnho, Integer empenho) {
        this.anoEmpenho = anoEmpnho;
        this.empenho = empenho;
    }

    public Date getAno() {
        return anoEmpenho;
    }

    public void setAno(Date ano) {
        this.anoEmpenho = ano;
    }

    public Integer getEmpenho() {
        return empenho;
    }

    public void setEmpenho(Integer empenho) {
        this.empenho = empenho;
    }

}
