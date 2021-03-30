import java.util.Random;

public class CampoMinado {

    //region VARIÁVEIS
    public static final int VAZIO = 0; // ao seu redor não existem minas
    /* de 1 a 8 são o número de minas à volta */
    public static final int TAPADO = 9;
    public static final int DUVIDA = 10;
    public static final int MARCADO = 11;
    public static final int REBENTADO = 12;

    private boolean[][] minas;
    private int[][] estado;

    private int largura;
    private int altura;
    private int numMinas;

    private boolean jogoTerminado;
    private boolean jogadorDerrotado;

    private long instanteInicioJogo;
    private long duracaoJogo;
    //endregion


    public CampoMinado(int largura, int altura, int numMinas){
        this.largura = largura;
        this.altura = altura;
        this.numMinas = numMinas;
        //this.jogadorDerrotado = false; // como o valor padrão duma var tipo boolean é false não é necessário inicializá-la
        //this.jogoTerminado = false; // como o valor padrão duma var tipo boolean é false não é necessário inicializá-la
        this.minas = new boolean[largura][altura]; // valores começam a false
        this.estado = new int[largura][altura]; // valores começam a 0


        for(var x = 0; x < largura; ++x){
            for (var y = 0; y < altura; ++y){
                estado[x][y] = TAPADO;
            }
        }
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public int getNumMinas() {
        return numMinas;
    }

    public int getEstadoQuadricula(int x, int y){
        return estado[x][y];
    }

    public boolean hasMinas(int x, int y){
        return minas[x][y];
    }

    private boolean isPrimeiraJogada(){
        for(var x = 0; x < largura; ++x){
            for (var y=0; y < altura; ++y){
                var celula = estado[x][y];
                if(celula != TAPADO && celula != DUVIDA && celula != MARCADO) return false;
            }
        }
        return true;
    }

    private void colocarMinas(int exceptoX, int exceptoY){
        var gerador = new Random();

        //region VARIÁVEIS QUE GUARDAM AS COORDENADAS PSEUDO-ALEATÓRIAS DAS MINAS
        var x = 0;
        var y = 0;
        //endregion

        for(var i=0; i < numMinas; ++i){ // colocar o número necessário de minas
            do {
                x = gerador.nextInt(largura); // largura é o limite. Não é necessário fazer -1 pois ele considera que só pode gerar até ao limite e não o valor do limite
                y = gerador.nextInt(altura);
            } while(minas[x][y] || (x == exceptoX && y == exceptoY));
            //se x igual a exceptoX e y igual a exceptoY, o do/while irá continuar enquanto for igual e se a posição
            //já tiver uma mina também irá continuar até existir uma posição sem mina e depois colocar a mina
            // pois não se pode colocar uma mina numa posição já com uma mina

            minas[x][y] = true; // colocar mina
        }
    }

    private int contarMinasVizinhas(int x, int y){
        var numMinas=0; // numero de minas vizinhas

        for (int i = Math.max(0,x - 1); i < Math.min(largura, x + 2); ++i){ // <=> i <= Math.min(largura-1, x + 2)
            for (int j = Math.max(0,y - 1); j < Math.min(altura,y + 2); ++j){ // <=> j <= Math.min(altura-1, y + 2)
                //if(i == x && j == y) continue;
                // não queremos verificar a própria quadrícula mas já sabemos que esta não tem mina, logo em termos de performance seria pior e é inútil fazer tal verificação
                if(minas[i][j]) ++numMinas; // somar +1 à variável a cada mina nas quadrículas a redor da quadrícula revelada
            }

        }
        return numMinas;
    }

    private void revelarQuadriculasVizinhas(int x, int y){
        for (int i = Math.max(0,x - 1); i < Math.min(largura, x + 2); ++i){ // <=> i <= Math.min(largura-1, x + 2)
            for (int j = Math.max(0,y-1); j < Math.min(altura,y + 2); ++j){ // <=> j <= Math.min(altura-1, y + 2)
                //if(i == x && j == y) continue;
                // não é necessário verificar a própria quadrícula pois na função revelar quadrícula, essa verificação já é feita
                revelarQuadricula(i,j);
            }
        }
    }

    public void revelarQuadricula(int x, int y){
        if( jogoTerminado || estado[x][y] < TAPADO) return;
        if(isPrimeiraJogada()){
            colocarMinas(x,y); // após a primeira quadrícula revelada posicionar mina
            // (como a regra diz que a primeira quadricula revelada nunca pode ter uma mina, posicionamos as minas sempre depois da primeira quadricula revelada)
            instanteInicioJogo = System.currentTimeMillis(); // guardar o instante da primeira quadrícula revelada, ou seja, do início do jogo
        }


        if(minas[x][y]){
            jogadorDerrotado = true;
            jogoTerminado = true;
            estado[x][y] = REBENTADO;

            duracaoJogo = System.currentTimeMillis() - instanteInicioJogo;

            return;
        }

        estado[x][y] = contarMinasVizinhas(x,y);

        if (estado[x][y] == VAZIO){ // VAZIO = 0, ou seja, se ao seu redor não existir minas destapar todas as quadrículas vizinhas
            revelarQuadriculasVizinhas(x,y);
        }

        if(isVitoria()){
            jogoTerminado = true;
            //jogadorDerrotado = false // Não é necessário indicar isto porque o valor "false" é o valor padrão
            duracaoJogo = System.currentTimeMillis() - instanteInicioJogo;
        }

    }

    public void marcarComoSuspeito(int x,int y){
        if(estado[x][y] == TAPADO || estado[x][y] == MARCADO){
            estado[x][y] = DUVIDA;
        }
    }

    public void marcarComoTendoMina(int x,int y){
        if (estado[x][y] == TAPADO || estado[x][y] == DUVIDA){
            estado[x][y] = MARCADO;
        }
    }

    public void desmarcarQuadricula(int x,int y){
        if (estado[x][y] == MARCADO || estado[x][y] == DUVIDA){
            estado[x][y] = TAPADO;
        }
    }

    private boolean isVitoria(){
        for (var i = 0; i < largura; ++i){
            for (var j = 0; j < altura; ++j){
                if(!minas[i][j] && estado[i][j] >= TAPADO) return false;
                // verificar todas as quadrículas e se alguma não tiver uma mina e o estado for menor ou igual a TAPADO, ou seja,
                // não ter mina, ter minas ao redor ou estar tapado, logo ainda existem quadrículas por destapar.
                // Se ainda existem quadrículas por destapar o jogador ainda não ganhou
            }
        }
        return true; // se o ciclo for terminou e não foi devolvido "false" então todas as quadrículas sem minas estão destapadas e o jogador venceu!
    }

    public boolean isJogoTerminado() {
        return jogoTerminado;
    }

    public boolean isJogadorDerrotado() {
        return jogadorDerrotado;
    }

    public long getDuracaoJogoEmMilissegundos(){
        if(isPrimeiraJogada()) return 0;
        if(!jogoTerminado) return System.currentTimeMillis() - duracaoJogo; // se o jogo ainda não acabou, retornar a duração atual do jogo
        return duracaoJogo; // duração final do jogo (jogo terminado com vitória ou derrota)
    }

}
