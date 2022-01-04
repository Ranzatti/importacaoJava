package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPRECFONTERECURSO")
public class ReceitaFonteRecurso implements Serializable {

	@EmbeddedId
	private ReceitaFonteRecursoPK id;
	private BigDecimal orcado;

	public ReceitaFonteRecurso() {
		this.id = new ReceitaFonteRecursoPK();
	}

	public ReceitaFonteRecurso(Date ano, Integer fichaReceita, Integer versaoRecurso, Integer fonteRecurso, BigDecimal orcado) {
		this.id = new ReceitaFonteRecursoPK(ano, fichaReceita, versaoRecurso, fonteRecurso);
		this.orcado = orcado;
	}

	public ReceitaFonteRecursoPK getId() {
		return id;
	}

	public void setId(ReceitaFonteRecursoPK id) {
		this.id = id;
	}

	public BigDecimal getOrcado() {
		return orcado;
	}

	public void setOrcado(BigDecimal orcado) {
		this.orcado = orcado;
	}

}
