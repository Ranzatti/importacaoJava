package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPITENSGUIA")
public class ItensGuiaReceita implements Serializable {

    @EmbeddedId
    private ItensGuiaReceitaPK id;
    private String receita;
    private BigDecimal valor;

    public ItensGuiaReceita() {
        this.id = new ItensGuiaReceitaPK();
    }

    public ItensGuiaReceita(Date ano, String tipo, Integer guia, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, String receita, BigDecimal valor) {
        this.id = new ItensGuiaReceitaPK(ano, tipo, guia, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel);
        this.receita = receita;
        this.valor = valor;
    }

    public ItensGuiaReceitaPK getId() {
        return id;
    }

    public void setId(ItensGuiaReceitaPK id) {
        this.id = id;
    }

    public String getReceita() {
        return receita;
    }

    public void setReceita(String receita) {
        this.receita = receita;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
