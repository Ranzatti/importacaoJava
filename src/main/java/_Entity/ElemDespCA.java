package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPELEMDESPCA")
public class ElemDespCA implements Serializable {

    @EmbeddedId
    private ElemDespCAPK id;
    private BigDecimal orcado;

    public ElemDespCA() {
        this.id = new ElemDespCAPK();
    }

    public ElemDespCA(Date ano, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, BigDecimal orcado) {
        this.id = new ElemDespCAPK(ano, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel);
        this.orcado = orcado;
    }

    public ElemDespCAPK getId() {
        return id;
    }

    public void setId(ElemDespCAPK id) {
        this.id = id;
    }

    public BigDecimal getOrcado() {
        return orcado;
    }

    public void setOrcado(BigDecimal orcado) {
        this.orcado = orcado;
    }

}
