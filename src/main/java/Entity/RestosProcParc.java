package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPRESTOSPROCPARC")
public class RestosProcParc implements Serializable {

    @EmbeddedId
    private RestosProcParcPK id;

    @Temporal(TemporalType.DATE)
    private Date vencimento;
    private BigDecimal valor;

    public RestosProcParc() {
        this.id = new RestosProcParcPK();
    }

    public RestosProcParc(Date ano, Integer empenho, Integer parcela, Date vencimento, BigDecimal valor) {
        this.id = new RestosProcParcPK(ano, empenho, parcela);
        this.vencimento = vencimento;
        this.valor = valor;
    }

    public RestosProcParcPK getId() {
        return id;
    }

    public void setId(RestosProcParcPK id) {
        this.id = id;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
