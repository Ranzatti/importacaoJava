package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPTRANSFERENCIA")
public class Transferencia implements Serializable {

	@EmbeddedId
	private TransferenciaPK id;

	public Transferencia() {this.id = new TransferenciaPK();
	}

	public Transferencia(Date ano, Integer sequencial) {
		this.id = new TransferenciaPK(ano, sequencial);
	}

	public TransferenciaPK getId() {
		return id;
	}

	public void setId(TransferenciaPK id) {
		this.id = id;
	}
}
