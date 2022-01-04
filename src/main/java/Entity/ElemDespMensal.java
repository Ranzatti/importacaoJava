package Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPELEMDESPMENSAL")
public class ElemDespMensal implements Serializable {

    @EmbeddedId
    private ElemDespMensalPK id;
    private BigDecimal orcado;

    public ElemDespMensal() {
        this.id = new ElemDespMensalPK();
    }

    public ElemDespMensal(Date ano, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, Integer mes, BigDecimal orcado) {
        this.id = new ElemDespMensalPK(ano, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel, mes);
        this.orcado = orcado;
    }

    public ElemDespMensalPK getId() {
        return id;
    }

    public void setId(ElemDespMensalPK id) {
        this.id = id;
    }

    public BigDecimal getOrcado() {
        return orcado;
    }

    public void setOrcado(BigDecimal orcado) {
        this.orcado = orcado;
    }

}
