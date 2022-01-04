package Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ElemDespesaPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private Integer ficha;

	public ElemDespesaPK() {
	}

	public ElemDespesaPK(Date ano, Integer ficha) {
		this.ano = ano;
		this.ficha = ficha;
	}

	public Integer getFicha() {
		return ficha;
	}

	public void setFicha(Integer ficha) {
		this.ficha = ficha;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}
}
