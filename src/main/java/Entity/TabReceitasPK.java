package Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class TabReceitasPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private String receita;

	public TabReceitasPK() {
	}

	public TabReceitasPK(Date ano, String receita) {
		this.ano = ano;
		this.receita = receita;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}

	public String getReceita() {
		return receita;
	}

	public void setReceita(String receita) {
		this.receita = receita;
	}

}
