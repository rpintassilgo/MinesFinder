import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JanelaDeJogo extends JFrame {

    private JPanel painelJogo;

    private CampoMinado campoMinado;
    private TabelaRecordes tabelaRecordes;

    private BotaoCampoMinado[][] botoes;


    public JanelaDeJogo(CampoMinado campoMinado, TabelaRecordes tabelaRecordes){
        this.campoMinado = campoMinado;
        this.tabelaRecordes = tabelaRecordes;

        var largura = campoMinado.getLargura();
        var altura = campoMinado.getAltura();

        this.botoes = new BotaoCampoMinado[largura][altura];
        painelJogo.setLayout(new GridLayout(altura, largura));

        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON3) return;

                var botao = (BotaoCampoMinado) e.getSource();

                var x = botao.getLinha();
                var y = botao.getColuna();

                var estadoQuadricula = campoMinado.getEstadoQuadricula(x,y);
                //System.out.println("Altura: "+y);

                if (estadoQuadricula == CampoMinado.TAPADO){
                    campoMinado.marcarComoTendoMina(x,y);
                } else if(estadoQuadricula == CampoMinado.MARCADO){
                    campoMinado.marcarComoSuspeito(x,y);
                } else if(estadoQuadricula == CampoMinado.DUVIDA){
                    campoMinado.desmarcarQuadricula(x,y);
                }
                atualizarEstadoBotoes();

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };


        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

                var botao = (BotaoCampoMinado) e.getSource();

                var x = botao.getLinha();
                var y = botao.getColuna();

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> botoes[x][--y < 0 ? altura - 1 : y].requestFocus();

                    case KeyEvent.VK_DOWN -> botoes[x][++y >= altura ? 0 : y ].requestFocus();
                    //case KeyEvent.VK_DOWN -> botoes[x][(y + 1) % altura].requestFocus();

                    case KeyEvent.VK_LEFT -> botoes[--x < 0 ? largura - 1 : x][y].requestFocus();

                    case KeyEvent.VK_RIGHT -> botoes[++x >= largura ? 0 : x][y].requestFocus();
                    //case KeyEvent.VK_RIGHT -> botoes[(x + 1) % largura][y].requestFocus();

                    case KeyEvent.VK_M -> {
                        var estadoQuadricula = campoMinado.getEstadoQuadricula(x, y);

                        if (estadoQuadricula == CampoMinado.TAPADO) {
                            campoMinado.marcarComoTendoMina(x, y);
                        } else if (estadoQuadricula == CampoMinado.MARCADO) {
                            campoMinado.marcarComoSuspeito(x, y);
                        } else if (estadoQuadricula == CampoMinado.DUVIDA) {
                            campoMinado.desmarcarQuadricula(x, y);
                        }
                        atualizarEstadoBotoes();
                    }
                }
                //Fim do switch
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };


        for (int coluna = 0; coluna < altura; ++coluna) {
            for (int linha = 0; linha < largura; ++linha) {
                botoes[linha][coluna] = new BotaoCampoMinado(linha, coluna);
                botoes[linha][coluna].addActionListener(this::btnCampoMinadoActionPerformed);
                botoes[linha][coluna].addMouseListener(mouseListener);
                botoes[linha][coluna].addKeyListener(keyListener);
                painelJogo.add(botoes[linha][coluna]);
            }
        }

        setContentPane(painelJogo); // Destrói esta janela, removendo-a completamente da memória.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();

        setVisible(true);
    };

    private void btnCampoMinadoActionPerformed(ActionEvent e){
        var botao = (BotaoCampoMinado) e.getSource();

        var x = botao.getLinha();
        var y= botao.getColuna();

        campoMinado.revelarQuadricula(x,y);

        atualizarEstadoBotoes();
        if(campoMinado.isJogoTerminado()) {
            if(campoMinado.isJogadorDerrotado()){
               JOptionPane.showMessageDialog(null, "Oh, rebentou uma mina","Perdeu!", JOptionPane.INFORMATION_MESSAGE);
            } else{
                JOptionPane.showMessageDialog(null, "Parabéns. Conseguiu descobrir todas as minas em " +
                        (campoMinado.getDuracaoJogoEmMilissegundos()/1000) + " segundos","Vitória", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);

                var pontuacao = campoMinado.getDuracaoJogoEmMilissegundos();

                if(pontuacao < tabelaRecordes.getPontuacao()){
                    var nome = JOptionPane.showInputDialog("Introduza o seu nome");

                    tabelaRecordes.setRecorde(nome, pontuacao);
                }
            }

        }
    }

    private void atualizarEstadoBotoes(){
        for (var x = 0; x < campoMinado.getLargura(); ++x){
            for (var y = 0; y < campoMinado.getAltura(); ++y){
                botoes[x][y].setEstado(campoMinado.getEstadoQuadricula(x,y)); // refresh à interface
                //fazemos refresh à interface toda, pois pode existir quadrículas vizinhas afetadas
            }
        }
    }


}
