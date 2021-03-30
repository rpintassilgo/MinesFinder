import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class BotaoCampoMinado extends JButton {

    //region VARIÁVEIS
    private int estado;
    private int linha;
    private int coluna;

    private Color currentColor;
    //endregion

    public BotaoCampoMinado(int linha, int coluna) {
        this.estado = CampoMinado.TAPADO;

        this.linha = linha;
        this.coluna = coluna;



        var focusListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                setBackground(Color.GREEN);
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBackground(currentColor);
            }
        };

        addFocusListener(focusListener);

    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setEstado(int estado){
        this.estado = estado;

        switch (estado) {
            case CampoMinado.VAZIO -> {
                setText("");
                currentColor = Color.LIGHT_GRAY;
            }
            case CampoMinado.TAPADO -> {
                setText("");
                currentColor = null;
            }
            case CampoMinado.DUVIDA -> {
                setText("?");
                currentColor = Color.YELLOW;
            }
            case CampoMinado.MARCADO -> {
                setText("!");
                currentColor = Color.RED;
            }
            case CampoMinado.REBENTADO -> {
                setText("*");
                currentColor = Color.ORANGE;
            }
            default -> {
                setText(String.valueOf(estado));
                currentColor = Color.LIGHT_GRAY;
            }
        }

        setBackground(currentColor);
    }
}
