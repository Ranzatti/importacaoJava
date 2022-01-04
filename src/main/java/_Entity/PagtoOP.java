package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPPAGTOOPS")
public class PagtoOP implements Serializable {

    @EmbeddedId
    private PagtoOPPK id;

    @Temporal(TemporalType.DATE)
    private Date dataPagto;

    private BigDecimal valorPagto;

    public PagtoOP() {
        this.id = new PagtoOPPK();
    }

    public PagtoOP(Date ano, Integer numero, Date dataPagto, BigDecimal valorPagto) {
        this.id = new PagtoOPPK(ano, numero);
        this.dataPagto = dataPagto;
        this.valorPagto = valorPagto;
    }

    public PagtoOPPK getId() {
        return id;
    }

    public void setId(PagtoOPPK id) {
        this.id = id;
    }

    public Date getDataPagto() {
        return dataPagto;
    }

    public void setDataPagto(Date dataPagto) {
        this.dataPagto = dataPagto;
    }

    public BigDecimal getValorPagto() {
        return valorPagto;
    }

    public void setValorPagto(BigDecimal valorPagto) {
        this.valorPagto = valorPagto;
    }

}
