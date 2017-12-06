package chattcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.util.Pair;

/**
 *
 * @author robertobarlocco
 */
public class Server {

	public static final int DISPONIBILE = 1;
	public static final int OCCUPATO = 2;

	private ServerSocket server;
	private ArrayList<Pair<ClientThread, Integer>> clientConnessi;
	private int port;
	private boolean isFermo;
	private DatabaseConnection db;
	private int nConnessi;

	public Server(int port) throws ClassNotFoundException {
		this.port = port;
		try {
			this.server = new ServerSocket(port);
			this.clientConnessi = new ArrayList<>();
			this.isFermo = false;
			this.nConnessi = 0;
			db = new DatabaseConnection();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void start() {
		System.out.println("Connessione al server...");
		ExecutorService threadPool = Executors.newFixedThreadPool(10);//10 ovvero numero massimo di thread 1 server e 9 client
		System.out.println("Porta del server connesso--> " + port);
		while (!isFermo) {
			try {
				int app = nConnessi;
				Socket client = server.accept();
				System.out.println("Utente " + (app+1) + " connesso in questo momento!");
				ClientThread ct = new ClientThread(client, app);
				this.clientConnessi.add(new Pair<>(ct, OCCUPATO));
				threadPool.execute(ct);
				nConnessi = clientConnessi.size();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
				isFermo = true;
			}
		}
	}

	public synchronized boolean occupaClient(String name) {
		int pos = getPos(name);
		ClientThread ct = clientConnessi.get(pos).getKey();
		int isBlocked = clientConnessi.get(pos).getValue();
		if (ct.getUsername().equals(name) && isBlocked == DISPONIBILE) {
			clientConnessi.remove(pos);
			clientConnessi.add(new Pair<>(ct, OCCUPATO));
			return true;

		}
		return false;
	}

	public int getPos(String name) {
		for (int i = 0; i < clientConnessi.size(); i++) {
			ClientThread ct = clientConnessi.get(i).getKey();
			if (ct.getUsername().equals(name)) {
				return i;
			}

		}
		return -1;
	}

	private String getClientConnessi(int id) {
		String conn = "";
		for (int i = 0; i < clientConnessi.size(); i++) {
			ClientThread ct = clientConnessi.get(i).getKey();
			int isBlocked = clientConnessi.get(i).getValue();
			if (isBlocked == DISPONIBILE && id != i) {
				conn += ct.getUsername() + " <> ";
			}
		}

		return conn;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		int port = 8080;
		Server s = new Server(port);
		s.start();
	}

	class ClientThread implements Runnable {

		private final Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		private String username;
		private boolean sconnesso;
		private final int id;

		public ClientThread(Socket socket, int id) {
			this.socket = socket;
			this.username = "";
			this.id = id;
			this.sconnesso = false;
		}

		public String getUsername() {
			return username;
		}

		@Override
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				autenticazione();
				if (!sconnesso) {
					connessione();
				} else {
					socket.close();
				}
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}

		private void autenticazione() {
			try {
				this.username = in.readLine();
				String password = in.readLine();
				if (db.login(username, password)) {
                                        System.out.println("L'utente "+username+" e' entrato con successo nella stanza!");
					out.println("successo");
				} else {
					out.println("fallito");
					if (socket != null && in.readLine().equals("si")) {
						out.println("fallito");
						username = in.readLine();
						password = in.readLine();
						if (db.exist(username)) {
							while (db.exist(username)) {
								username = in.readLine();
								password = in.readLine();
								out.println("fallito");
							}
							db.addUser(username, password);
							out.println("successo");
						} else {
							db.addUser(username, password);
							out.println("successo");
						}
					} else {
						System.out.println("Utente" + id + " rimosso");
						this.sconnesso = true;
					}

				}
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}

		private void connessione() throws IOException {
			try {
				String connessi = getClientConnessi(id);
				out.println(connessi);

				out.println("Digitare nome utente con cui si decide di chattare o premere 1 per attendere.");
				out.flush();

					String user = in.readLine();
					if (user.equals("1")) {
						clientConnessi.remove(id);
						clientConnessi.add(id, new Pair<>(this, DISPONIBILE));
					} else if (db.exist(user)) {

						Socket s1 = clientConnessi.get(getPos(user)).getKey().socket;
						BufferedReader input = new BufferedReader(new InputStreamReader(s1.getInputStream()));
						PrintWriter output = new PrintWriter(s1.getOutputStream(), true);

						output.println(username + " vuole chattare con te. Accetti?");
						if (input.readLine().toLowerCase().equals("si")) {
							occupaClient(user);
							new Thread(new Chat(username, user)).start();
							new Thread(new Chat(user, username)).start();
						}
				}

			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}

		public void writeMessage(String message) {
			out.println(message);
		}

		class Chat implements Runnable {

			private final String user1;
			private final String user2;

			public Chat(String user1, String user2) {
				this.user1 = user1;
				this.user2 = user2;
			}

			@Override
			public void run() {
				Socket s1 = clientConnessi.get(getPos(user1)).getKey().socket;
				Socket s2 = clientConnessi.get(getPos(user2)).getKey().socket;
				try {
					BufferedReader input = new BufferedReader(new InputStreamReader(s1.getInputStream()));
					PrintWriter output = new PrintWriter(s2.getOutputStream(), true);

					while (true) {
						String messaggio = user1 + ":" + input.readLine();
						if (!messaggio.equals("fine")) {
							output.println(messaggio);
						} else {
                                                    s2.close();
                                                    s1.close();
							
						}
					}
				} catch (IOException ex) {
					ex.getMessage();
				}

			}

		}

	}

}
