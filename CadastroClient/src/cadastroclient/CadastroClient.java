/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Produto;

/**
 *
 * @author borba
 */
public class CadastroClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        try (Socket socket = new Socket("localhost", 4321); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.print("Login: ");
            String login = reader.readLine();
            System.out.print("Senha: ");
            String senha = reader.readLine();

            out.writeObject(login);
            out.writeObject(senha);
            out.flush();

            out.writeObject("L");
            out.flush();

            @SuppressWarnings("unchecked")
            List<Produto> produtos = (List<Produto>) in.readObject();
            for (Produto p : produtos) {
                System.out.println(p.getIDProduto() + " - " + p.getNome()
                        + " - estoque: " + p.getQuantidade());
            }
        }
    }

}
