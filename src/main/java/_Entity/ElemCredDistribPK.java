package _Entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ElemCredDistribPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private Integer codigo;
    private Integer fichaCredito;
    private Integer versaoRecCredito;

    @Column(name = "FONTERECCREDITO")
    private Integer fonteRecursoCredito;
    private Integer caFixoCredito;
    private Integer caVariavelCredito;
    private Integer fichaAnulacao;
    private Integer versaoRecAnulacao;

    @Column(name = "FONTERECANULACAO")
    private Integer fonteRecursoAnulacao;
    private Integer caFixoAnulacao;
    private Integer caVariavelAnulacao;

    public ElemCredDistribPK() {
    }

    public ElemCredDistribPK(Date ano, Integer codigo, Integer fichaCredito, Integer versaoRecCredito, Integer fonteRecursoCredito, Integer caFixoCredito, Integer caVariavelCredito,
                             Integer fichaAnulacao, Integer versaoRecAnulacao, Integer fonteRecursoAnulacao, Integer caFixoAnulacao, Integer caVariavelAnulacao) {
        this.ano = ano;
        this.codigo = codigo;
        this.fichaCredito = fichaCredito;
        this.versaoRecCredito = versaoRecCredito;
        this.fonteRecursoCredito = fonteRecursoCredito;
        this.caFixoCredito = caFixoCredito;
        this.caVariavelCredito = caVariavelCredito;
        this.fichaAnulacao = fichaAnulacao;
        this.versaoRecAnulacao = versaoRecAnulacao;
        this.fonteRecursoAnulacao = fonteRecursoAnulacao;
        this.caFixoAnulacao = caFixoAnulacao;
        this.caVariavelAnulacao = caVariavelAnulacao;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getFichaCredito() {
        return fichaCredito;
    }

    public void setFichaCredito(Integer fichaCredito) {
        this.fichaCredito = fichaCredito;
    }

    public Integer getVersaoRecCredito() {
        return versaoRecCredito;
    }

    public void setVersaoRecCredito(Integer versaoRecCredito) {
        this.versaoRecCredito = versaoRecCredito;
    }

    public Integer getFonteRecursoCredito() {
        return fonteRecursoCredito;
    }

    public void setFonteRecursoCredito(Integer fonteRecursoCredito) {
        this.fonteRecursoCredito = fonteRecursoCredito;
    }

    public Integer getCaFixoCredito() {
        return caFixoCredito;
    }

    public void setCaFixoCredito(Integer caFixoCredito) {
        this.caFixoCredito = caFixoCredito;
    }

    public Integer getCaVariavelCredito() {
        return caVariavelCredito;
    }

    public void setCaVariavelCredito(Integer caVariavelCredito) {
        this.caVariavelCredito = caVariavelCredito;
    }

    public Integer getFichaAnulacao() {
        return fichaAnulacao;
    }

    public void setFichaAnulacao(Integer fichaAnulacao) {
        this.fichaAnulacao = fichaAnulacao;
    }

    public Integer getVersaoRecAnulacao() {
        return versaoRecAnulacao;
    }

    public void setVersaoRecAnulacao(Integer versaoRecAnulacao) {
        this.versaoRecAnulacao = versaoRecAnulacao;
    }

    public Integer getFonteRecursoAnulacao() {
        return fonteRecursoAnulacao;
    }

    public void setFonteRecursoAnulacao(Integer fonteRecursoAnulacao) {
        this.fonteRecursoAnulacao = fonteRecursoAnulacao;
    }

    public Integer getCaFixoAnulacao() {
        return caFixoAnulacao;
    }

    public void setCaFixoAnulacao(Integer caFixoAnulacao) {
        this.caFixoAnulacao = caFixoAnulacao;
    }

    public Integer getCaVariavelAnulacao() {
        return caVariavelAnulacao;
    }

    public void setCaVariavelAnulacao(Integer caVariavelAnulacao) {
        this.caVariavelAnulacao = caVariavelAnulacao;
    }
}
