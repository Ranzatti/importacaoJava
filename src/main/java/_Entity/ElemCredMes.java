package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPELEMCREDMES")
public class ElemCredMes implements Serializable {

    @EmbeddedId
    private ElemCredMesPK id;
    private BigDecimal valor;

    public ElemCredMes() {
        this.id = new ElemCredMesPK();
    }

    public ElemCredMes(Date ano, Integer codigo, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, Integer mes, BigDecimal valor) {
        this.id = new ElemCredMesPK(ano, codigo, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel, mes);
        this.valor = valor;
    }

    public ElemCredMesPK getId() {
        return id;
    }

    public void setId(ElemCredMesPK id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
