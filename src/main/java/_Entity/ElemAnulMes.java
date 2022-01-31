package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPELEMANULMES")
public class ElemAnulMes implements Serializable {

	@EmbeddedId
	private ElemAnulMesPK id;
	private BigDecimal valor;

	public ElemAnulMes() {this.id = new ElemAnulMesPK();
	}

	public ElemAnulMes(Date ano, Integer codigo, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, Integer mes, BigDecimal valor) {
		this.id = new ElemAnulMesPK(ano, codigo, ficha, versaoRecurso, fonteRecurso, caFixo, caVariavel, mes);
		this.valor = valor;
	}

	public ElemAnulMesPK getId() {
		return id;
	}

	public void setId(ElemAnulMesPK id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
