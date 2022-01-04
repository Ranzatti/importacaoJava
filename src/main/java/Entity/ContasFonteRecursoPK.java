package Entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ContasFonteRecursoPK implements Serializable {

	private Integer ficha;
	private Integer versaoRecurso;
	private Integer fonteRecurso;

	public ContasFonteRecursoPK() {
	}

	public ContasFonteRecursoPK(Integer ficha, Integer versaoRecurso, Integer fonteRecurso) {
		this.ficha = ficha;
		this.versaoRecurso = versaoRecurso;
		this.fonteRecurso = fonteRecurso;
	}

	public Integer getFicha() {
		return ficha;
	}

	public void setFicha(Integer ficha) {
		this.ficha = ficha;
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
