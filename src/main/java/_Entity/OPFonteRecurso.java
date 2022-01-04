package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPOPFONTERECURSO")
public class OPFonteRecurso implements Serializable {

    @EmbeddedId
    private OPFonteRecursoPK id;
    private Integer versaoRecurso;
    private Integer fonteRecurso;
    private Integer caFixo;
    private Integer caVariavel;
    private BigDecimal valor;

    public OPFonteRecurso() {
        this.id = new OPFonteRecursoPK();
    }

    public OPFonteRecurso(Date ano, Integer numero, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, BigDecimal valor) {
        this.id = new OPFonteRecursoPK(ano, numero);
        this.versaoRecurso = versaoRecurso;
        this.fonteRecurso = fonteRecurso;
        this.caFixo = caFixo;
        this.caVariavel = caVariavel;
        this.valor = valor;
    }

    public OPFonteRecursoPK getId() {
        return id;
    }

    public void setId(OPFonteRecursoPK id) {
        this.id = id;
    }

    public Integer getVersaoRecurso() {
        return versaoRecurso;
    }

    public void setVersaoRecurso(Integer versaoRecurso) {
        this.versaoRecurso = versaoRecurso;
    }

    public Integer getFonteRecurso() {
        return fonteRecurso;
    }

    public void setFonteRecurso(Integer fonteRecurso) {
        this.fonteRecurso = fonteRecurso;
    }

    public Integer getCaFixo() {
        return caFixo;
    }

    public void setCaFixo(Integer caFixo) {
        this.caFixo = caFixo;
    }

    public Integer getCaVariavel() {
        return caVariavel;
    }

    public void setCaVariavel(Integer caVariavel) {
        this.caVariavel = caVariavel;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
