/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import controller.exceptions.PreexistingEntityException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Movimento;
import model.Pessoa;
import model.Produto;
import model.Usuario;

/**
 *
 * @author borba
 */
public class CadastroThreadV2 extends Thread {

    private final UsuarioJpaController ctrlUsu;
    private final ProdutoJpaController ctrlProd;
    private final PessoaJpaController ctrlPessoa;
    private final MovimentoJpaController ctrlMov;
    private final Socket socket;

    public CadastroThreadV2(UsuarioJpaController ctrlUsu,
            ProdutoJpaController ctrlProd,
            PessoaJpaController ctrlPessoa,
            MovimentoJpaController ctrlMov,
            Socket socket) {
        this.ctrlUsu = ctrlUsu;
        this.ctrlProd = ctrlProd;
        this.ctrlPessoa = ctrlPessoa;
        this.ctrlMov = ctrlMov;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            String login = (String) in.readObject();
            String senha = (String) in.readObject();
            Usuario user = ctrlUsu.findUsuario(login, senha);
            if (user == null) {
                out.writeObject("Usuário ou senha inválidos.");
                out.flush();
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
                } else if ("E".equalsIgnoreCase(cmd) || "S".equalsIgnoreCase(cmd)) {
                    int idPessoa = (Integer) in.readObject();
                    int idProduto = (Integer) in.readObject();
                    String qtd = (String) in.readObject();
                    String valU = (String) in.readObject();

                    Pessoa p = ctrlPessoa.findPessoa(idPessoa);
                    Produto pd = ctrlProd.findProduto(idProduto);

                    Movimento m = new Movimento();
                    int nextId = ctrlMov.findMaxId() + 1;
                    m.setIDMovimento(nextId);
                    m.setTipo(cmd.charAt(0));
                    m.setIDUsuario(user);
                    m.setIDPessoa(p);
                    m.setIDProduto(pd);
                    m.setQuantidade(qtd);
                    m.setValorUnitario(valU);

                    ctrlMov.create(m);

                    int delta = Integer.parseInt(qtd) * (cmd.equalsIgnoreCase("E") ? +1 : -1);
                    pd.setQuantidade(pd.getQuantidade() + delta);
                    ctrlProd.edit(pd);

                    out.writeObject(cmd + " OK para Movimento ID " + nextId);
                    out.flush();
                }
            }

        } catch (IOException ex) {
            System.getLogger(CadastroThreadV2.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (ClassNotFoundException ex) {
            System.getLogger(CadastroThreadV2.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (PreexistingEntityException ex) {
            System.getLogger(CadastroThreadV2.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (Exception ex) {
            System.getLogger(CadastroThreadV2.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
