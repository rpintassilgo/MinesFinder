import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinesFinder extends JFrame {

    private JPanel painelPrincipal;

    private JLabel lblNomeJogadorFacil;
    private JLabel lblPontuacaoFacil;

    private JLabel lblNomeJogadorMedio;
    private JLabel lblPontuacaoMedio;

    private JLabel lblNomeJogadorDificil;
    private JLabel lblPontuacaoDificil;

    private JButton btnJogoFacil;
    private JButton btnJogoMedio;
    private JButton btnJogoDificil;

    private JButton btnSair;

    private TabelaRecordes tabelaRecordesFacil;
    private TabelaRecordes tabelaRecordesMedio;
    private TabelaRecordes tabelaRecordesDificil;

    private String caminhoFicheiroRecordes = System.getProperty("user.home")+ File.separator + "minesfinder.recordes";

    public MinesFinder(String title) {
        super(title);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(painelPrincipal);

        pack(); // atualizar as dimensões (expandir a janela para o tamanho do painel)

        tabelaRecordesFacil = new TabelaRecordes();
        tabelaRecordesMedio = new TabelaRecordes();
        tabelaRecordesDificil = new TabelaRecordes();

        lerRecordesDoDisco();

        atualizarRecordesFacil(tabelaRecordesFacil,false);
        atualizarRecordesMedio(tabelaRecordesMedio,false);
        atualizarRecordesDificil(tabelaRecordesDificil,false);

        tabelaRecordesFacil.addListener( (recordes) -> this.atualizarRecordesFacil(recordes,true)); // checkar mais tarde para entender melhor esta syntax!
        tabelaRecordesMedio.addListener( (recordes) -> this.atualizarRecordesMedio(recordes,true));
        tabelaRecordesDificil.addListener( (recordes) -> this.atualizarRecordesDificil(recordes,true));

        // Ações btn
        btnJogoFacil.addActionListener(this::btnJogoFacilActionPerformed);
        btnJogoMedio.addActionListener(this::btnJogoMedioActionPerformed);
        btnJogoDificil.addActionListener(this::btnJogoDificilActionPerformed);
        btnSair.addActionListener(this::btnSairActionPerformed);
    }

    // Métodos btn
    private void btnJogoFacilActionPerformed(ActionEvent e){
        // instanciar uma nova janela de jogo
        new JanelaDeJogo(new CampoMinado(9,9,10),tabelaRecordesFacil ); // minas: 10

    }

    private void btnJogoMedioActionPerformed(ActionEvent e){
        // instanciar uma nova janela de jogo
        new JanelaDeJogo(new CampoMinado(16,16,40),tabelaRecordesMedio); // minas: 40
    }

    private void btnJogoDificilActionPerformed(ActionEvent e){
        // instanciar uma nova janela de jogo
        new JanelaDeJogo(new CampoMinado(16,30,90),tabelaRecordesDificil); // minas: 90
    }

    private void btnSairActionPerformed(ActionEvent e){
        System.exit(0);
    }

    // Métodos Atualizar Pontuação
    public void atualizarRecordesFacil(TabelaRecordes recordes, boolean saveOnDisk){
        lblNomeJogadorFacil.setText(recordes.getNome());
        lblPontuacaoFacil.setText(Long.toString(recordes.getPontuacao()));

        if(saveOnDisk) guardarRecordesDisco();
    }
    public void atualizarRecordesMedio(TabelaRecordes recordes, boolean saveOnDisk){
        lblNomeJogadorMedio.setText(recordes.getNome());
        lblPontuacaoMedio.setText(Long.toString(recordes.getPontuacao()));

        if(saveOnDisk) guardarRecordesDisco();
    }
    public void atualizarRecordesDificil(TabelaRecordes recordes, boolean saveOnDisk){
        lblNomeJogadorDificil.setText(recordes.getNome());
        lblPontuacaoDificil.setText(Long.toString(recordes.getPontuacao()));

        if(saveOnDisk) guardarRecordesDisco();
    }

    // Método main
    public static void main(String[] args) {
        new MinesFinder("Mines Finder").setVisible(true);
    }

    private void guardarRecordesDisco() { //checkar esta funcao mais tarde
        ObjectOutputStream oos = null;
        try {
            File f =new File(caminhoFicheiroRecordes);

            oos = new ObjectOutputStream(new FileOutputStream(f)); oos.writeObject(tabelaRecordesFacil);
            oos.writeObject(tabelaRecordesMedio);
            oos.writeObject(tabelaRecordesDificil);

            oos.close();
        } catch (IOException ex) { Logger.getLogger(MinesFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void lerRecordesDoDisco() { //checkar esta funcao mais tarde
        ObjectInputStream ois = null;
        File f = new File(caminhoFicheiroRecordes);

        if (!f.canRead()) return;;

        try {
            ois = new ObjectInputStream(new FileInputStream(f));

            tabelaRecordesFacil=(TabelaRecordes) ois.readObject();
            tabelaRecordesMedio=(TabelaRecordes) ois.readObject();
            tabelaRecordesDificil=(TabelaRecordes) ois.readObject();

            ois.close();

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MinesFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}