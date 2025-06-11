/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import controller.exceptions.PreexistingEntityException;
import java.io.*;
import java.net.Socket;
import java.util.List;
import model.Produto;
import model.Usuario;

/**
 *
 * @author borba
 */

public class CadastroThread extends Thread {

    private final UsuarioJpaController ctrlUsu;
    private final ProdutoJpaController ctrlProd;
    private final Socket socket;

    public CadastroThread(UsuarioJpaController ctrlUsu,
            ProdutoJpaController ctrlProd,
            Socket socket) {
        this.ctrlUsu = ctrlUsu;
        this.ctrlProd = ctrlProd;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            String login = (String) in.readObject();
            String senha = (String) in.readObject();
            Usuario user = ctrlUsu.findUsuario(login, senha);
            if (user == null) {
                socket.close();
                return;
            }

            while (true) {
                String cmd = (String) in.readObject();
                if ("X".equalsIgnoreCase(cmd)) {
                    socket.close();
                    break;
                }
                if ("L".equalsIgnoreCase(cmd)) {
                    List<Produto> lista = ctrlProd.findProdutoEntities();
                    out.writeObject(lista);
                    out.flush();
                }
            }

        } catch (IOException ex) {
            System.getLogger(CadastroThread.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (ClassNotFoundException ex) {
            System.getLogger(CadastroThread.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
