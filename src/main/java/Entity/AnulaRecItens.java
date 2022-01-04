package Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPANULARECITENS")
public class AnulaRecItens implements Serializable {

    @EmbeddedId
    private AnulaRecItensPK id;
    private BigDecimal valor;

    public AnulaRecItens() {
        this.id = new AnulaRecItensPK();
    }

    public AnulaRecItens(Date ano, String tipo, Integer anulacao, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, BigDecimal valor) {
        this.id = new AnulaRecItensPK(ano, tipo, anulacao, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel);
        this.valor = valor;
    }

    public AnulaRecItensPK getId() {
        return id;
    }

    public void setId(AnulaRecItensPK id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
