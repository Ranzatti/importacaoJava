package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPANULARECEITAS")
public class AnulaReceita implements Serializable {

    @EmbeddedId
    private AnulaReceitaPK id;

    @Temporal(TemporalType.DATE)
    private Date dataAnulacao;
    private Integer lancamento;
    private String historico;
    private Integer dedutora;

    public AnulaReceita() {
        this.id = new AnulaReceitaPK();
    }

    public AnulaReceita(Date ano, String tipo, Integer anulacao, Date dataAnulacao, String historico, Integer dedutora) {
        this.id = new AnulaReceitaPK(ano, tipo, anulacao);
        this.dataAnulacao = dataAnulacao;
        this.lancamento = -1;
        this.historico = historico;
        this.dedutora = dedutora;
    }

    public AnulaReceitaPK getId() {
        return id;
    }

    public void setId(AnulaReceitaPK id) {
        this.id = id;
    }

    public Date getDataAnulacao() {
        return dataAnulacao;
    }

    public void setDataAnulacao(Date dataAnulacao) {
        this.dataAnulacao = dataAnulacao;
    }

    public Integer getLancamento() {
        return lancamento;
    }

    public void setLancamento(Integer lancamento) {
        this.lancamento = lancamento;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public Integer getDedutora() {
        return dedutora;
    }

    public void setDedutora(Integer dedutora) {
        this.dedutora = dedutora;
    }

}
