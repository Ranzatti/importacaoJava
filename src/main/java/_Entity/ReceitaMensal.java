package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPRECEITASMENSAL")
public class ReceitaMensal implements Serializable {

    @EmbeddedId
    private ReceitaMensalPK id;
    private BigDecimal orcado;

    public ReceitaMensal() {
        this.id = new ReceitaMensalPK();
    }

    public ReceitaMensal(Date ano, Integer fichaReceita, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, Integer mes, BigDecimal orcado) {
        this.id = new ReceitaMensalPK(ano, fichaReceita, versaoRecurso, fonteRecurso, caFixo, caVariavel, mes);
        this.orcado = orcado;
    }

    public ReceitaMensalPK getId() {
        return id;
    }

    public void setId(ReceitaMensalPK id) {
        this.id = id;
    }

    public BigDecimal getOrcado() {
        return orcado;
    }

    public void setOrcado(BigDecimal orcado) {
        this.orcado = orcado;
    }

}
