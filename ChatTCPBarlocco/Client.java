package chattcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author robertobarlocco
 */
public class Client {

	private BufferedReader in;
	private BufferedReader inSystem;
	private PrintWriter out;
	private Socket client;
	private int porta;
	private String indirizzoServer;
	private String username;
	private String password;

	public Client(String address, int port) {

		System.out.println("Connessione al server...");
		this.indirizzoServer = address;
		this.porta = port;

		try {
			this.client = new Socket(indirizzoServer, porta);
			System.out.println("Connessione riuscita " + client.getInetAddress() + "/" + client.getPort());

			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
			inSystem = new BufferedReader(new InputStreamReader(System.in));

			autenticazione();
			connesioneAltroClient();
			new msgDalServer().start();

		} catch (IOException ex) {

			System.out.println(ex.getMessage());
			System.out.println("Arrivederci");
			System.exit(0);

		}
	}

	private boolean autenticazione() throws IOException {
		try {

			System.out.println("Username :");
			this.username = inSystem.readLine();
			System.out.println("Password :");
			this.password = inSystem.readLine();

			out.println(username);
			out.println(password);

			if (in.readLine().equals("successo")) {
				System.out.println("Benvenuto " + username);
				return true;
			} else {
				System.out.println("Accesso fallito! Vuoi riprovare?");
				String s = inSystem.readLine().toLowerCase();
				if (s.equals("si")) {
					try {
						System.out.println("Username :");
						this.username = inSystem.readLine();
						System.out.println("Password :");
						this.password = inSystem.readLine();

						out.println(username);
						out.println(password);

						if (in.readLine().equals("successo")) {
							System.out.println("Benvenuto " + username);
							return true;
						}
					} catch (Exception e) {

					}
				}
				System.out.println("Devi registrarti?");
				String risposta = inSystem.readLine().toLowerCase();
				if (risposta.equals("si")) {
					out.println("si");
					while (true) {
						if (in.readLine().equals("ok")) {
							break;
						}
						System.out.println("username");
						out.println(inSystem.readLine());
						System.out.println("password");
						out.println(inSystem.readLine());

					}
					System.out.println("Benvenuto " + username);
				} else {
					out.println("no");
					System.out.println("Arrivederci");
					client.close();
					System.exit(0);
				}
			}

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return false;
	}

	private void connesioneAltroClient() {
		try {
			//mi dice i client connessi
			System.out.println("Ricerca utenti disponibili in corso; Attendere prego...");
			String connessi = in.readLine();
			if (connessi.equals("")) {
				System.out.println("Nessuno disponibile.");
			} else {
				System.out.println(connessi);
			}

			System.out.println(in.readLine());
			out.println(inSystem.readLine());

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			System.out.println("Arrivederci");
		}
	}

	public void inviaMsg(String message) {
		out.println(message);
		out.flush();
	}

	public static void main(String[] args) {
		Scanner t = new Scanner(System.in);
		Client client = new Client("localhost", 8080);
		while (true) {
			System.out.print("...");
			String message = t.nextLine();
			if (message.equals("fine")) {
				client.inviaMsg("fine");
				break;
			} else {
				client.inviaMsg(message);
			}
		}
		try {
			System.out.println("Arrivederci");
			client.client.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());

		}
	}

	class msgDalServer extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					String message = in.readLine();
					System.out.println(message);
					System.out.print("> ");
				} catch (IOException ex) {
					System.out.println(ex.getMessage());
					System.out.println("Arrivederci");
					System.exit(0);
				}
			}

		}
	}
}
