package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class CreditoDotacaoPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private Integer codigo;

    public CreditoDotacaoPK() {
    }

    public CreditoDotacaoPK(Date ano, Integer codigo) {
        this.ano = ano;
        this.codigo = codigo;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
