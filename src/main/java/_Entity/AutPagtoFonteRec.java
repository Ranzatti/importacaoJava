package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPAUTPAGTOFONTREC")
public class AutPagtoFonteRec implements Serializable {

    @EmbeddedId
    private AutPagtoFonteRecPK id;
    private BigDecimal valor;

    public AutPagtoFonteRec() {
        this.id = new AutPagtoFonteRecPK();
    }

    public AutPagtoFonteRec(Date ano, Integer autorizacao, Integer fichaBanco, String tipoDoc, Integer documento, Integer parcela, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, Integer sequencial, BigDecimal valor) {
        this.id = new AutPagtoFonteRecPK(ano, autorizacao, fichaBanco, tipoDoc, documento, parcela, sequencial, versaoRecurso, fonteRecurso, caFixo, caVariavel);
        this.valor = valor;
    }

    public AutPagtoFonteRecPK getId() {
        return id;
    }

    public void setId(AutPagtoFonteRecPK id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
