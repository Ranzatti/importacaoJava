package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPITENSAUTPAGTO")
public class ItensAutPagto implements Serializable {

    @EmbeddedId
    private ItensAutPagtoPK id;

    public ItensAutPagto() {
        this.id = new ItensAutPagtoPK();
    }

    public ItensAutPagto(Date ano, Integer autorizacao, String tipoDoc, Integer documento, Integer parcela) {
        this.id = new ItensAutPagtoPK(ano, autorizacao, tipoDoc, documento, parcela);
    }

    public ItensAutPagtoPK getId() {
        return id;
    }

    public void setId(ItensAutPagtoPK id) {
        this.id = id;
    }

}
