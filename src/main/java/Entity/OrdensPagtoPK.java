package Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class OrdensPagtoPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private Integer numero;

    public OrdensPagtoPK() {
    }

    public OrdensPagtoPK(Date ano, Integer numero) {
        this.ano = ano;
        this.numero = numero;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

}
