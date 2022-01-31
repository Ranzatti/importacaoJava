package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPELEMDISTCREDANU")
public class ElemCredDistrib implements Serializable {

	@EmbeddedId
	private ElemCredDistribPK id;
	private BigDecimal valor;

	public ElemCredDistrib() {this.id = new ElemCredDistribPK();
	}

	public ElemCredDistrib(Date ano, Integer codigo, Integer fichaCredito, Integer versaoRecCredito, Integer fonteRecursoCredito, Integer caFixoCredito, Integer caVariavelCredito,
						   Integer fichaAnulacao, Integer versaoRecAnulacao, Integer fonteRecursoAnulacao, Integer caFixoAnulacao, Integer caVariavelAnulacao, BigDecimal valor) {
		this.id = new ElemCredDistribPK(ano, codigo, fichaCredito, versaoRecCredito, fonteRecursoCredito, caFixoCredito, caVariavelCredito, fichaAnulacao, versaoRecAnulacao, fonteRecursoAnulacao, caFixoAnulacao, caVariavelAnulacao);
		this.valor = valor;
	}

	public ElemCredDistribPK getId() {
		return id;
	}

	public void setId(ElemCredDistribPK id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
