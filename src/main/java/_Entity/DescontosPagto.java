package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPDESCONTOSPAGTO")
public class DescontosPagto implements Serializable {

	@EmbeddedId
	private DescontosPagtoPK id;

	public DescontosPagto() {
		this.id = new DescontosPagtoPK();
	}

	public DescontosPagto(Date ano, Integer empenho, Integer parcela, String tipoGuia, Integer guiaReceita) {
		this.id = new DescontosPagtoPK(ano, empenho, parcela, tipoGuia, guiaReceita);
	}

	public DescontosPagtoPK getId() {
		return id;
	}

	public void setId(DescontosPagtoPK id) {
		this.id = id;
	}
}
