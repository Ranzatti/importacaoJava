package Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table( name = "CBPCONTASFONTEREC")
public class ContasFonteRecurso implements Serializable {

	@EmbeddedId
	private ContasFonteRecursoPK id;
	private BigDecimal saldoInicial;

	public ContasFonteRecurso() {
		this.id = new ContasFonteRecursoPK();
	}

	public ContasFonteRecurso(Integer ficha, Integer versaoRecurso, Integer fonteRecurso, BigDecimal saldoInicial) {
		this.id = new ContasFonteRecursoPK(ficha, versaoRecurso, fonteRecurso);
		this.saldoInicial = saldoInicial;
	}

	public ContasFonteRecursoPK getId() {
		return id;
	}

	public void setId(ContasFonteRecursoPK id) {
		this.id = id;
	}

	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

}
