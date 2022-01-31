package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPELEMCREDITADOS")
public class ElemCreditados implements Serializable {

    @EmbeddedId
    private ElemCreditadosPK id;
    private BigDecimal valor;

    public ElemCreditados() {this.id = new ElemCreditadosPK();
    }

    public ElemCreditados(Date ano, Integer codigo, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, BigDecimal valor) {
        this.id = new ElemCreditadosPK(ano, codigo, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel);
        this.valor = valor;
    }

    public ElemCreditadosPK getId() {
        return id;
    }

    public void setId(ElemCreditadosPK id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
