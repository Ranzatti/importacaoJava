package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPDESCONTOSTEMP")
public class DescontosTemp implements Serializable {

    @EmbeddedId
    private DescontosTempPK id;
    private Integer contribuinte;
    private BigDecimal valor;
    private String tributo;
    private String contribTributo;

    public DescontosTemp() {
        this.id = new DescontosTempPK();
    }

    public DescontosTemp(Date ano, String tipoDoc, Integer documento, Integer parcela, String tipoDesc, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, Integer contribuinte, String tributo, String contribTributo, BigDecimal valor) {
        this.id = new DescontosTempPK(ano, tipoDoc, documento, parcela, tipoDesc, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel);
        this.contribuinte = contribuinte;
        this.tributo = tributo;
        this.contribTributo = contribTributo;
        this.valor = valor;
    }

    public DescontosTempPK getId() {
        return id;
    }

    public void setId(DescontosTempPK id) {
        this.id = id;
    }

    public Integer getContribuinte() {
        return contribuinte;
    }

    public void setContribuinte(Integer contribuinte) {
        this.contribuinte = contribuinte;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTributo() {
        return tributo;
    }

    public void setTributo(String tributo) {
        this.tributo = tributo;
    }

    public String getContribTributo() {
        return contribTributo;
    }

    public void setContribTributo(String contribTributo) {
        this.contribTributo = contribTributo;
    }
}
