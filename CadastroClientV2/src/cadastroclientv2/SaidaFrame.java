/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclientv2;

import javax.swing.*;

/**
 *
 * @author borba
 */
public class SaidaFrame extends JDialog {

    public final JTextArea texto = new JTextArea();

    public SaidaFrame() {
        setTitle("Mensagens do Servidor");
        setBounds(100, 100, 400, 300);
        setModal(false);
        texto.setEditable(false);
        add(new JScrollPane(texto));
    }
}
