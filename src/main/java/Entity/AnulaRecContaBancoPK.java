package Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class AnulaRecContaBancoPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private String tipo;
    private Integer anulacao;
    private Integer fichaBanco;
    private Integer fichaReceita;
    private Integer item;
    private Integer versaoRecurso;
    private Integer fonteRecurso;
    private Integer caFixo;
    private Integer caVariavel;
    private Integer versaoRecursoBanco;
    private Integer fonteRecursoBanco;
    private Integer caFixoBanco;
    private Integer caVariavelBanco;

    public AnulaRecContaBancoPK() {
    }

    public AnulaRecContaBancoPK(Date ano, String tipo, Integer anulacao, Integer fichaReceita, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, Integer item, Integer fichaBanco, Integer versaoRecursoBanco, Integer fonteRecursoBanco, Integer caFixoBanco, Integer caVariavelBanco) {
        this.ano = ano;
        this.tipo = tipo;
        this.anulacao = anulacao;
        this.fichaBanco = fichaBanco;
        this.fichaReceita = fichaReceita;
        this.item = item;
        this.versaoRecurso = versaoRecurso;
        this.fonteRecurso = fonteRecurso;
        this.caFixo = caFixo;
        this.caVariavel = caVariavel;
        this.versaoRecursoBanco = versaoRecursoBanco;
        this.fonteRecursoBanco = fonteRecursoBanco;
        this.caFixoBanco = caFixoBanco;
        this.caVariavelBanco = caVariavelBanco;
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

    public Integer getAnulacao() {
        return anulacao;
    }

    public void setAnulacao(Integer anulacao) {
        this.anulacao = anulacao;
    }

    public Integer getFichaBanco() {
        return fichaBanco;
    }

    public void setFichaBanco(Integer fichaBanco) {
        this.fichaBanco = fichaBanco;
    }

    public Integer getFichaReceita() {
        return fichaReceita;
    }

    public void setFichaReceita(Integer fichaReceita) {
        this.fichaReceita = fichaReceita;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
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

    public Integer getVersaoRecursoBanco() {
        return versaoRecursoBanco;
    }

    public void setVersaoRecursoBanco(Integer versaoRecursoBanco) {
        this.versaoRecursoBanco = versaoRecursoBanco;
    }

    public Integer getFonteRecursoBanco() {
        return fonteRecursoBanco;
    }

    public void setFonteRecursoBanco(Integer fonteRecursoBanco) {
        this.fonteRecursoBanco = fonteRecursoBanco;
    }

    public Integer getCaFixoBanco() {
        return caFixoBanco;
    }

    public void setCaFixoBanco(Integer caFixoBanco) {
        this.caFixoBanco = caFixoBanco;
    }

    public Integer getCaVariavelBanco() {
        return caVariavelBanco;
    }

    public void setCaVariavelBanco(Integer caVariavelBanco) {
        this.caVariavelBanco = caVariavelBanco;
    }

}
