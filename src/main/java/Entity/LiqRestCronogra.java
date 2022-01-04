package Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPLIQRESTCRONOGRA")
public class LiqRestCronogra implements Serializable {

    @EmbeddedId
    private LiqRestCronograPK id;

    public LiqRestCronogra() {
        this.id = new LiqRestCronograPK();
    }

    public LiqRestCronogra(Date anoEmpenho, int empenho, int liquidacao, int parcela, Date anoOP, int nroOP) {
        this.id = new LiqRestCronograPK(anoEmpenho, empenho, liquidacao, parcela, anoOP, nroOP);
    }

    public LiqRestCronograPK getId() {
        return id;
    }

    public void setId(LiqRestCronograPK id) {
        this.id = id;
    }
}
