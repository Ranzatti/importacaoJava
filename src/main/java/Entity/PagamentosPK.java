package Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class PagamentosPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private Integer empenho;
	private Integer pagamento;

	public PagamentosPK() {
	}

	public PagamentosPK(Date ano, Integer empenho, Integer pagamento) {
		this.ano = ano;
		this.empenho = empenho;
		this.pagamento = pagamento;
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

	public Integer getPagamento() {
		return pagamento;
	}

	public void setPagamento(Integer pagamento) {
		this.pagamento = pagamento;
	}

}
