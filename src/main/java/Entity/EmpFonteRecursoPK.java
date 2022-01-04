package Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class EmpFonteRecursoPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private Integer empenho;

	public EmpFonteRecursoPK() {
	}

	public EmpFonteRecursoPK(Date ano, Integer empenho) {
		this.ano = ano;
		this.empenho = empenho;
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

}
