package Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class DocPagtoPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private String tipoDoc;
    private Integer documento;
    private Integer parcela;
    private Integer tipoDocPagto;
    private String documentoPagto;

    public DocPagtoPK() {
    }

    public DocPagtoPK(Date ano, String tipoDoc, Integer documento, Integer parcela, Integer tipoDocPagto, String documentoPagto) {
        this.ano = ano;
        this.tipoDoc = tipoDoc;
        this.documento = documento;
        this.parcela = parcela;
        this.tipoDocPagto = tipoDocPagto;
        this.documentoPagto = documentoPagto;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
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

    public Integer getTipoDocPagto() {
        return tipoDocPagto;
    }

    public void setTipoDocPagto(Integer tipoDocPagto) {
        this.tipoDocPagto = tipoDocPagto;
    }

    public String getDocumentoPagto() {
        return documentoPagto;
    }

    public void setDocumentoPagto(String documentoPagto) {
        this.documentoPagto = documentoPagto;
    }
}
