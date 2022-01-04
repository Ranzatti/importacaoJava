package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "CBPCONTASCA")
public class ContasCA implements Serializable {

    @EmbeddedId
    private ContasCAPK id;
    private BigDecimal saldoInicial;

    public ContasCA() {
        this.id = new ContasCAPK();
    }

    public ContasCA(Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, BigDecimal saldoInicial) {
        this.id = new ContasCAPK(ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel);
        this.saldoInicial = saldoInicial;
    }

    public ContasCAPK getId() {
        return id;
    }

    public void setId(ContasCAPK id) {
        this.id = id;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

}
