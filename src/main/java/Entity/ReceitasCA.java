package Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPRECEITASCA")
public class ReceitasCA implements Serializable {

    @EmbeddedId
    private ReceitasCAPK id;
    private BigDecimal orcado;

    public ReceitasCA() {
        this.id = new ReceitasCAPK();
    }

    public ReceitasCA(Date ano, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, BigDecimal orcado) {
        this.id = new ReceitasCAPK(ano, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel);
        this.orcado = orcado;
    }

    public ReceitasCAPK getId() {
        return id;
    }

    public void setId(ReceitasCAPK id) {
        this.id = id;
    }

    public BigDecimal getOrcado() {
        return orcado;
    }

    public void setOrcado(BigDecimal orcado) {
        this.orcado = orcado;
    }

}
