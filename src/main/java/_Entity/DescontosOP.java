package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPDESCONTOSOP")
public class DescontosOP implements Serializable {

    @EmbeddedId
    private DescontosOPPK id;

    public DescontosOP() {
        this.id = new DescontosOPPK();
    }

    public DescontosOP(Date ano, Integer op, Integer parcela, String tipoGuia, Integer guiaReceita) {
        this.id = new DescontosOPPK(ano, op, parcela, tipoGuia, guiaReceita);
    }

    public DescontosOPPK getId() {
        return id;
    }

    public void setId(DescontosOPPK id) {
        this.id = id;
    }
}
