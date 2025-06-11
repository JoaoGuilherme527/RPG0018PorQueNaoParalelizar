package cadastroclientv2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import javax.swing.*;
import model.Produto;

public class ClientThread extends Thread {

    private final ObjectInputStream in;
    private final JTextArea textArea;

    public ClientThread(ObjectInputStream in, JTextArea textArea) {
        this.in = in;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();
                SwingUtilities.invokeLater(() -> {
                    if (obj instanceof String str) {
                        textArea.append(str + "\n");
                    } else if (obj instanceof List<?>) {
                        for (Object o : (List<?>) obj) {
                            if (o instanceof Produto p) {
                                textArea.append(
                                        p.getIDProduto() + " - "
                                        + p.getNome() + " - Estoque: "
                                        + p.getQuantidade() + "\n"
                                );
                            }
                        }
                    }
                    textArea.setCaretPosition(textArea.getDocument().getLength()); // autoscroll
                });
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> textArea.append("Conexão encerrada pelo servidor.\n"));
        }
    }
}
