package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class AnulacaoEmpenhoPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private Integer empenho;
	private Integer anulacao;

	public AnulacaoEmpenhoPK() {
	}

	public AnulacaoEmpenhoPK(Date ano, Integer empenho, Integer anulacao) {
		this.ano = ano;
		this.empenho = empenho;
		this.anulacao = anulacao;
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

	public Integer getAnulacao() {
		return anulacao;
	}

	public void setAnulacao(Integer anulacao) {
		this.anulacao = anulacao;
	}

}
