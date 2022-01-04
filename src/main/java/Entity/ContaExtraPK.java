package Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ContaExtraPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private Integer contaExtra;

	public ContaExtraPK() {
	}

	public ContaExtraPK(Date ano, Integer contaExtra) {
		this.ano = ano;
		this.contaExtra = contaExtra;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}

	public Integer getContaExtra() {
		return contaExtra;
	}

	public void setContaExtra(Integer contaExtra) {
		this.contaExtra = contaExtra;
	}

}
