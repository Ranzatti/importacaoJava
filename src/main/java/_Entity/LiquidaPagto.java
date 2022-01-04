package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPLIQUIDAPAGTO")
public class LiquidaPagto implements Serializable {

    @EmbeddedId
    private LiquidaPagtoPK id;

    public LiquidaPagto() {
        this.id = new LiquidaPagtoPK();
    }

    public LiquidaPagto(Date ano, Integer empenho, Integer liquidacao, Integer pagamento) {
        this.id = new LiquidaPagtoPK(ano, empenho, liquidacao, pagamento);
    }

    public LiquidaPagtoPK getId() {
        return id;
    }

    public void setId(LiquidaPagtoPK id) {
        this.id = id;
    }
}
