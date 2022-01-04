package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPANULRECCONTABAN")
public class AnulaRecContaBanco implements Serializable {

	@EmbeddedId
	private AnulaRecContaBancoPK id;
	private BigDecimal valor;

	public AnulaRecContaBanco() {
		this.id = new AnulaRecContaBancoPK();
	}

	public AnulaRecContaBanco(Date ano, String tipo, Integer anulacao, Integer fichaReceita, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, Integer fichaBanco, Integer versaoRecursoBanco, Integer fonteRecursoBanco, Integer caFixoBanco, Integer caVariavelBanco, Integer item, BigDecimal valor) {
		this.id = new AnulaRecContaBancoPK(ano, tipo, anulacao, fichaReceita, versaoRecurso, fonteRecurso, caFixo, caVariavel, item, fichaBanco, versaoRecursoBanco, fonteRecursoBanco, caFixoBanco, caVariavelBanco);
		this.valor = valor;
	}

	public AnulaRecContaBancoPK getId() {
		return id;
	}

	public void setId(AnulaRecContaBancoPK id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
