package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class TransferenciaPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private Integer sequencial;

	public TransferenciaPK() {
	}

	public TransferenciaPK(Date ano, Integer sequencial) {
		this.ano = ano;
		this.sequencial = sequencial;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}

	public Integer getSequencial() {
		return sequencial;
	}

	public void setSequencial(Integer sequencial) {
		this.sequencial = sequencial;
	}

}
