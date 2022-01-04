package Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class EntradasCaixaPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date data;
	private Integer entrada;

	public EntradasCaixaPK() {
	}

	public EntradasCaixaPK(Date data, Integer entrada) {
		this.data = data;
		this.entrada = entrada;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getEntrada() {
		return entrada;
	}

	public void setEntrada(Integer entrada) {
		this.entrada = entrada;
	}

}
