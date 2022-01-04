package Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class LiquidacaoEmpenhoPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private Integer empenho;
	private Integer liquidacao;

	public LiquidacaoEmpenhoPK() {
	}

	public LiquidacaoEmpenhoPK(Date ano, Integer empenho, Integer liquidacao) {
		this.ano = ano;
		this.empenho = empenho;
		this.liquidacao = liquidacao;
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

	public Integer getLiquidacao() {
		return liquidacao;
	}

	public void setLiquidacao(Integer liquidacao) {
		this.liquidacao = liquidacao;
	}
}
