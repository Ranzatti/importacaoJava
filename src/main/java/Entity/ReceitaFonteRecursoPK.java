package Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ReceitaFonteRecursoPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private Integer fichaReceita;
	private Integer versaoRecurso;
	private Integer fonteRecurso;

	public ReceitaFonteRecursoPK() {
	}

	public ReceitaFonteRecursoPK(Date ano, Integer fichaReceita, Integer versaoRecurso, Integer fonteRecurso) {
		this.ano = ano;
		this.fichaReceita = fichaReceita;
		this.versaoRecurso = versaoRecurso;
		this.fonteRecurso = fonteRecurso;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}

	public Integer getFichaReceita() {
		return fichaReceita;
	}

	public void setFichaReceita(Integer fichaReceita) {
		this.fichaReceita = fichaReceita;
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
