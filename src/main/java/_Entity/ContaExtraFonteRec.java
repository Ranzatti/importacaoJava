package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPCONTAEXFONTEREC")
public class ContaExtraFonteRec implements Serializable {

	@EmbeddedId
	private ContaExtraFonteRecPK id;
	private String tipoSaldo;
	private BigDecimal saldo;

	public ContaExtraFonteRec() {
		this.id = new ContaExtraFonteRecPK();
	}

	public ContaExtraFonteRec(Date ano, Integer contaExtra, Integer versaoRecurso, Integer fonteRecurso, String tipoSaldo, BigDecimal saldo) {
		this.id = new ContaExtraFonteRecPK(ano, contaExtra,versaoRecurso, fonteRecurso);
		this.tipoSaldo = tipoSaldo;
		this.saldo = saldo;
	}

	public ContaExtraFonteRecPK getId() {
		return id;
	}

	public void setId(ContaExtraFonteRecPK id) {
		this.id = id;
	}

	public String getTipoSaldo() {
		return tipoSaldo;
	}

	public void setTipoSaldo(String tipoSaldo) {
		this.tipoSaldo = tipoSaldo;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
}
