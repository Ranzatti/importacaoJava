package _Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ItensAutPagtoPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private Integer autorizacao;
    private String tipoDoc;
    private Integer documento;
    private Integer parcela;

    public ItensAutPagtoPK() {
    }

    public ItensAutPagtoPK(Date ano, Integer autorizacao, String tipoDoc, Integer documento, Integer parcela) {
        this.ano = ano;
        this.autorizacao = autorizacao;
        this.tipoDoc = tipoDoc;
        this.documento = documento;
        this.parcela = parcela;
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

}
