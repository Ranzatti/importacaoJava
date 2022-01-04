package _Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ReceitasPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private Integer ficha;

	public ReceitasPK() {
	}

	public ReceitasPK(Date ano, Integer ficha) {
		this.ano = ano;
		this.ficha = ficha;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}

	public Integer getFicha() {
		return ficha;
	}

	public void setFicha(Integer ficha) {
		this.ficha = ficha;
	}

}
