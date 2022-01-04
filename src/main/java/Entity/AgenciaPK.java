package Entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AgenciaPK implements Serializable {

    private Integer banco;
    private String codigo;

	public AgenciaPK() {
	}

	public AgenciaPK(Integer banco, String codigo) {
		this.banco = banco;
		this.codigo = codigo;
	}

	public Integer getBanco() {
        return banco;
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
