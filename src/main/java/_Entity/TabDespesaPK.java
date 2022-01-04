package _Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class TabDespesaPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private String despesa;

	public TabDespesaPK() {
	}

	public TabDespesaPK(Date ano, String despesa) {
		this.ano = ano;
		this.despesa = despesa;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}

	public String getDespesa() {
		return despesa;
	}

	public void setDespesa(String despesa) {
		this.despesa = despesa;
	}

}
