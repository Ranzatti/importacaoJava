package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class SaidasCaixaPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date data;
	private Integer saida;

	public SaidasCaixaPK() {
	}

	public SaidasCaixaPK(Date data, Integer saida) {
		this.data = data;
		this.saida = saida;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getSaida() {
		return saida;
	}

	public void setSaida(Integer saida) {
		this.saida = saida;
	}
}
