package Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "CBPAGENCIAS")
public class Agencia implements Serializable {

    @EmbeddedId
    private AgenciaPK id;
    private String nome;

    public Agencia() {
        this.id = new AgenciaPK();
    }

    public Agencia(Integer banco, String codigo, String nome) {
        this.id = new AgenciaPK(banco, codigo);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
