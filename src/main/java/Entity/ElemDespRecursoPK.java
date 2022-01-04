package Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ElemDespRecursoPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private Integer ficha;
    private Integer versaoRecurso;

    @Column(name = "RECURSO")
    private Integer fonteRecurso;

    public ElemDespRecursoPK() {
    }

    public ElemDespRecursoPK(Date ano, Integer ficha, Integer versaoRecurso, Integer fonteRecurso) {
        this.ano = ano;
        this.ficha = ficha;
        this.versaoRecurso = versaoRecurso;
        this.fonteRecurso = fonteRecurso;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public Integer getFicha() {
        return ficha;
    }

    public void setFicha(Integer ficha) {
        this.ficha = ficha;
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
}
