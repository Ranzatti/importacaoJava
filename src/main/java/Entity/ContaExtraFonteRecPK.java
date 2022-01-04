package Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ContaExtraFonteRecPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private Integer contaExtra;
	private Integer versaoRecurso;
	private Integer fonteRecurso;

	public ContaExtraFonteRecPK() {
	}

	public ContaExtraFonteRecPK(Date ano, Integer contaExtra, Integer versaoRecurso, Integer fonteRecurso) {
		this.ano = ano;
		this.contaExtra = contaExtra;
		this.versaoRecurso = versaoRecurso;
		this.fonteRecurso = fonteRecurso;
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

	public Integer getVersaoRecurso() {
		return versaoRecurso;
	}

	public void setVersaoRecurso(Integer versaoRecurso) {
		this.versaoRecurso = versaoRecurso;
	}

	public Integer getFonteRecurso() {
		return fonteRecurso;
	}

	public void setFonteRecurso(Integer fonteRecurso) {
		this.fonteRecurso = fonteRecurso;
	}

}
