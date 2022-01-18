package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class EmpenhoComplementoPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;

	private Integer empenho;
	private Integer complemento;

	public EmpenhoComplementoPK() {
	}

	public EmpenhoComplementoPK(Date ano, Integer empenho, Integer complemento) {
		this.ano = ano;
		this.empenho = empenho;
		this.complemento = complemento;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}

	public Integer getEmpenho() {
		return empenho;
	}

	public void setEmpenho(Integer empenho) {
		this.empenho = empenho;
	}

	public Integer getComplemento() {
		return complemento;
	}

	public void setComplemento(Integer complemento) {
		this.complemento = complemento;
	}
}
