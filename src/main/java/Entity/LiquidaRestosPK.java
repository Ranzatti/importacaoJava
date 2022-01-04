package Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class LiquidaRestosPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date anoEmpenho;
	private Integer empenho;
	private Integer liquidacao;

	public LiquidaRestosPK() {
	}

	public LiquidaRestosPK(Date anoEmpenho, Integer empenho, Integer liquidacao) {
		this.anoEmpenho = anoEmpenho;
		this.empenho = empenho;
		this.liquidacao = liquidacao;
	}

	public Date getAnoEmpenho() {
		return anoEmpenho;
	}

	public void setAnoEmpenho(Date anoEmpenho) {
		this.anoEmpenho = anoEmpenho;
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
