package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPDOCPAGTO")
public class DocPagto implements Serializable {

    @EmbeddedId
    private DocPagtoPK id;

    @Temporal(TemporalType.DATE)
    private Date dataEmissao;
    private String descricaoAdicional;
    private Integer item;
    private BigDecimal valor;

    public DocPagto() {
        this.id = new DocPagtoPK();
    }

    public DocPagto(Date ano, String tipoDoc, Integer documento, Integer parcela, Integer tipoDocPagto, String documentoPagto, Date dataEmissao, String descricaoAdicional, Integer item, BigDecimal valor) {
        this.id = new DocPagtoPK(ano, tipoDoc, documento, parcela, tipoDocPagto, documentoPagto);
        this.dataEmissao = dataEmissao;
        this.descricaoAdicional = descricaoAdicional;
        this.item = item;
        this.valor = valor;
    }

    public DocPagtoPK getId() {
        return id;
    }

    public void setId(DocPagtoPK id) {
        this.id = id;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDescricaoAdicional() {
        return descricaoAdicional;
    }

    public void setDescricaoAdicional(String descricaoAdicional) {
        this.descricaoAdicional = descricaoAdicional;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
