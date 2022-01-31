package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class AnulacaoLiquidaPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private int empenho;
	private int liquidacao;
	private int anulacao;

	public AnulacaoLiquidaPK() {
	}

	public AnulacaoLiquidaPK(Date ano, int empenho, int liquidacao, int anulacao) {
		this.ano = ano;
		this.empenho = empenho;
		this.liquidacao = liquidacao;
		this.anulacao = anulacao;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}

	public int getEmpenho() {
		return empenho;
	}

	public void setEmpenho(int empenho) {
		this.empenho = empenho;
	}

	public int getLiquidacao() {
		return liquidacao;
	}

	public void setLiquidacao(int liquidacao) {
		this.liquidacao = liquidacao;
	}


}
