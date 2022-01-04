package Entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPRESTOSFONTEREC")
public class RestosFonteRec implements Serializable {

    @EmbeddedId
    private RestosFonteRecPK id;
    private Integer versaoRecurso;

    @Column(name = "RECURSO")
    private Integer fonteRecurso;
    private Integer caFixo;
    private Integer caVariavel;
    private BigDecimal valor;

    public RestosFonteRec() {
        this.id = new RestosFonteRecPK();
    }

    public RestosFonteRec(Date anoEmpenho, Integer empenho, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, BigDecimal valor) {
        this.id = new RestosFonteRecPK(anoEmpenho, empenho);
        this.versaoRecurso = versaoRecurso;
        this.fonteRecurso = fonteRecurso;
        this.caFixo = caFixo;
        this.caVariavel = caVariavel;
        this.valor = valor;
    }

    public RestosFonteRecPK getId() {
        return id;
    }

    public void setId(RestosFonteRecPK id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
}
