package cadastroserver;

import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import controller.exceptions.PreexistingEntityException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author borba
 */
public class CadastroServer {

    public static void main(String[] args)
            throws IOException, ClassNotFoundException, PreexistingEntityException {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");

        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);
        ProdutoJpaController ctrlProd = new ProdutoJpaController(emf);
        PessoaJpaController ctrlPessoa = new PessoaJpaController(emf);
        MovimentoJpaController ctrlMov = new MovimentoJpaController(emf);

        try (ServerSocket serverSocket = new ServerSocket(4321)) {
            System.out.println("Servidor aguardando na porta 4321...");
            while (true) {
                Socket socket = serverSocket.accept();
                new CadastroThreadV2(
                        ctrlUsu,
                        ctrlProd,
                        ctrlPessoa,
                        ctrlMov,
                        socket
                ).start();
                System.out.println("Thread nova iniciada.");
            }
        }
    }
}
