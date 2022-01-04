package Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPELEMDESPRECURSO")
public class ElemDespRecurso implements Serializable {

    @EmbeddedId
    private ElemDespRecursoPK id;
    private BigDecimal orcado;

    public ElemDespRecurso(Date anoAtual, Integer ficha, Integer i, Integer fonteRecurso) {
        this.id = new ElemDespRecursoPK();
    }

    public ElemDespRecurso(Date ano, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, BigDecimal orcado) {
        this.id = new ElemDespRecursoPK(ano, ficha, versaoRecurso, fonteRecurso);
        this.orcado = orcado;
    }

    public ElemDespRecursoPK getId() {
        return id;
    }

    public void setId(ElemDespRecursoPK id) {
        this.id = id;
    }

    public BigDecimal getOrcado() {
        return orcado;
    }

    public void setOrcado(BigDecimal orcado) {
        this.orcado = orcado;
    }

}
