package Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPRECCONTABANCO")
public class RecContaBanco implements Serializable {

	@EmbeddedId
	private RecContaBancoPK id;
	private BigDecimal valor;

	public RecContaBanco() {
		this.id = new RecContaBancoPK();
	}

	public RecContaBanco(Date ano, String tipo, Integer guia, Integer ficha, Integer versaoRecurso, Integer recurso, Integer caFixo, Integer caVariavel, Integer fichaBanco, Integer versaoRecursoBanco, Integer fonteRecursoBanco, Integer caFixoBanco, Integer caVariavelBanco, Integer item, BigDecimal valor) {
		this.id = new RecContaBancoPK(ano, tipo, guia, ficha, versaoRecurso, recurso, caFixo, caVariavel, item, fichaBanco, versaoRecursoBanco, fonteRecursoBanco, caFixoBanco, caVariavelBanco);
		this.valor = valor;
	}

	public RecContaBancoPK getId() {
		return id;
	}

	public void setId(RecContaBancoPK id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
