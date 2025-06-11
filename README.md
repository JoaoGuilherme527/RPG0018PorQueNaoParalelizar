# Missão Prática - Nível 5

**João Guilherme Fernandes Borba - 2022 0851 5835**  
**POLO IPIRANGA - PORTO ALEGRE**  
**RPG0018 - Por que não paralelizar – Turma 9001**

---

## Como funcionam `Socket` e `ServerSocket`

- **`ServerSocket`** é tipo um porteiro: ele abre uma porta (4321 no nosso caso) e espera clientes baterem a porta.
- **`Socket`**, tanto do lado do servidor quanto do cliente, é o canal de comunicação criando um “túnel” entre os dois.
- Quando o cliente chama `new Socket("localhost", 4321)`, ele conecta nesse túnel. O servidor aceita com `serverSocket.accept()`.

---

## A importância das portas

- A **porta** é como uma sala dentro de uma casa (o IP/host).
- Sem especificar uma porta, o sistema não sabe pra onde enviar os dados.
- No nosso projeto, a porta ‘4321’ diz: “manda os dados pra porta certa no servidor”.

---

## Objetos e `ObjectInputStream` / `ObjectOutputStream`

- Essas classes permitem enviar objetos Java inteiros (como `Produto`, `Movimento`) pelos sockets.
- Mas só funciona se os objetos forem **serializáveis** (implementarem `Serializable`), porque aí o Java transforma eles em bytes e depois reconstrói no outro lado.

---

## JPA no cliente, sem acesso ao BD

- Usar entidade JPA no cliente **não dá acesso ao banco**. O cliente só usa as classes para ler os objetos vindos do servidor.
- Isso garante **isolamento**: apenas o servidor pode acessar e modificar o banco via JPA; o cliente só mostra o que recebe.

---

## Threads para tratamento assíncrono

- A `ClientThread` roda num flow separado (`Thread`), recebendo mensagens do servidor em background.
- Assim, o cliente pode continuar lendo comandos no console sem travar — super útil pra interface responsiva.

---

## `SwingUtilities.invokeLater`

- Esse método garante que todas as atualizações da **interface gráfica** (Swing) aconteçam na *Event Dispatch Thread*.
- É importante pra evitar bugs visuais ou travamentos, já que o Swing não é thread-safe.

---

## Como os objetos são enviados e recebidos

1. Cliente chama `out.writeObject(obj)` para mandar um objeto.
2. Ele passa pela rede como bytes.
3. Servidor lê com `in.readObject()` e reconstrói o objeto Java inteiro.
4. O mesmo processo serve para o retorno do servidor pro cliente.

---

## Assíncrono vs Síncrono com Socket

| Modo       | Como funciona                                      | Em que se diferencia                         |
|------------|----------------------------------------------------|-----------------------------------------------|
| **Síncrono** | Cliente manda e espera resposta, fica bloqueado. | Fácil de implementar, mas trava o app.       |
| **Assíncrono** | Usa thread separada pra ler do socket.         | UI continua fluida, cliente não bloqueia.    |

- No cliente **síncrono**, tudo roda na `main()`, então o programa fica preso até receber dados.
- No **assíncrono**, a `ClientThread` roda à parte, mantendo a interface ativa pra receber respostas sem travar.

---

## Conclusão

- `ServerSocket` e `Socket` formam a conexão cliente ↔ servidor.
- Portas são fundamentais pra direcionar os dados corretamente.
- `ObjectInputStream`/`ObjectOutputStream` fazem mágica pra enviar objetos serializados.
- Usar JPA nas entidades não dá acesso ao BD no cliente — é apenas conveniência de código.
- A thread T2 (ClientThread) e `invokeLater` garantem que a interface funcione numa boa.
- E o método assíncrono evita que o cliente trave ou congele.
