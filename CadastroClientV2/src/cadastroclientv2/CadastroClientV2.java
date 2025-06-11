/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroclientv2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author borba
 */
public class CadastroClientV2 {

    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 4321); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Login: ");
            String login = reader.readLine();
            System.out.print("Senha: ");
            String senha = reader.readLine();
            out.writeObject(login);
            out.writeObject(senha);
            out.flush();

            SaidaFrame frame = new SaidaFrame();
            frame.setVisible(true);

            new ClientThread(in, frame.texto).start();

            String cmd;
            do {
                System.out.print("Comando (L=lista, E=entrada, S=saída, X=sair): ");
                cmd = reader.readLine().trim().toUpperCase();

                out.writeObject(cmd);
                out.flush();

                if ("E".equals(cmd) || "S".equals(cmd)) {
                    System.out.print("ID Pessoa: ");
                    int idPessoa = Integer.parseInt(reader.readLine());
                    out.writeObject(idPessoa);

                    System.out.print("ID Produto: ");
                    int idProduto = Integer.parseInt(reader.readLine());
                    out.writeObject(idProduto);

                    System.out.print("Quantidade: ");
                    String quantidade = reader.readLine();
                    out.writeObject(quantidade);

                    System.out.print("Valor Unitário: ");
                    String valorUnitario = reader.readLine();
                    out.writeObject(valorUnitario);

                    out.flush();
                }

            } while (!"X".equals(cmd));

            socket.close();
            System.exit(0);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro de conexão: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro de formato numérico: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
