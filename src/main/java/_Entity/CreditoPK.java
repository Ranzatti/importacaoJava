package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class CreditoPK implements Serializable {

	private Integer fichaConta;

	@Temporal(TemporalType.DATE)
	private Date data;
	private Integer numero;

	public CreditoPK() {
	}

	public CreditoPK(Integer fichaConta, Date data, Integer numero) {
		this.fichaConta = fichaConta;
		this.data = data;
		this.numero = numero;
	}

	public Integer getFichaConta() {
		return fichaConta;
	}

	public void setFichaConta(Integer fichaConta) {
		this.fichaConta = fichaConta;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
}
