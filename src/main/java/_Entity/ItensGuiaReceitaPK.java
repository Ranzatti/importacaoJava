package _Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ItensGuiaReceitaPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private String tipo;
    private Integer guia;
    private Integer ficha;
    private Integer versaoRecurso;

    @Column(name = "RECURSO")
    private Integer fonteRecurso;
    private Integer caFixo;
    private Integer caVariavel;

    public ItensGuiaReceitaPK() {
    }

    public ItensGuiaReceitaPK(Date ano, String tipo, Integer guia, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel) {
        this.ano = ano;
        this.tipo = tipo;
        this.guia = guia;
        this.ficha = ficha;
        this.versaoRecurso = versaoRecurso;
        this.fonteRecurso = fonteRecurso;
        this.caFixo = caFixo;
        this.caVariavel = caVariavel;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getGuia() {
        return guia;
    }

    public void setGuia(Integer guia) {
        this.guia = guia;
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
