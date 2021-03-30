import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TabelaRecordes implements Serializable {
    private transient List<TabelaRecordesListener> listeners = new ArrayList<>();

    private String nome;
    private long pontuacao; // tempo de jogo -> melhor pontuacao será a menor tempo de todos

    public TabelaRecordes(/*String nome, long pontuacao*/) {
        this.nome = "Anónimo";//nome;
        this.pontuacao = 9999;//pontuacao;
    }

    public String getNome() {
        return nome;
    }

    public long getPontuacao() {
        return pontuacao;
    }

    public void setRecorde(String nome, long pontuacao){
        if(pontuacao >= this.pontuacao) return;

        this.nome = nome;
        this.pontuacao = pontuacao;

        if(listeners == null) listeners = new ArrayList<>();

        for(var listener : listeners){
            listener.recordesAtualizados(this);
        }
    }

    public void addListener(TabelaRecordesListener listener){
        if(listeners == null) listeners = new ArrayList<>();

        this.listeners.add(listener);
    }
}
