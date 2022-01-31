package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPELEMANULADOS")
public class ElemAnulados implements Serializable {

    @EmbeddedId
    private ElemAnuladosPK id;
    private BigDecimal valor;

    public ElemAnulados() {
        this.id = new ElemAnuladosPK();
    }

    public ElemAnulados(Date ano, Integer codigo, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, BigDecimal valor) {
        this.id = new ElemAnuladosPK(ano, codigo, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel);
        this.valor = valor;
    }

    public ElemAnuladosPK getId() {
        return id;
    }

    public void setId(ElemAnuladosPK id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
