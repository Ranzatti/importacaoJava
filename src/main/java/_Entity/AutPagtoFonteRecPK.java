package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class AutPagtoFonteRecPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private Integer autorizacao;
    private Integer fichaBanco;
    private String tipoDoc;
    private Integer documento;
    private Integer parcela;
    private Integer sequencial;
    private Integer versaoRecurso;
    private Integer fonteRecurso;
    private Integer caFixo;
    private Integer caVariavel;

    public AutPagtoFonteRecPK() {
    }

    public AutPagtoFonteRecPK(Date ano, Integer autorizacao, Integer fichaBanco, String tipoDoc, Integer documento, Integer parcela, Integer sequencial, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel) {
        this.ano = ano;
        this.autorizacao = autorizacao;
        this.fichaBanco = fichaBanco;
        this.tipoDoc = tipoDoc;
        this.documento = documento;
        this.parcela = parcela;
        this.sequencial = sequencial;
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

    public Integer getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(Integer autorizacao) {
        this.autorizacao = autorizacao;
    }

    public Integer getFichaBanco() {
        return fichaBanco;
    }

    public void setFichaBanco(Integer fichaBanco) {
        this.fichaBanco = fichaBanco;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public Integer getDocumento() {
        return documento;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }

    public Integer getParcela() {
        return parcela;
    }

    public void setParcela(Integer parcela) {
        this.parcela = parcela;
    }

    public Integer getSequencial() {
        return sequencial;
    }

    public void setSequencial(Integer sequencial) {
        this.sequencial = sequencial;
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
