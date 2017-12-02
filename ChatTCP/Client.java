package chattcp;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 *
 * @author Camillo
 */
public class Client extends Thread {

    private String idClient;
    private BufferedReader in;
    private BufferedReader stdIn;
    private PrintWriter out;
    private Socket socket;
    private String server;
    private int port;
    private final String serverAddress;
    private static boolean inChat;

    public Client(String serverAddress, int port) throws IOException {
        this.serverAddress = serverAddress;
        this.port = port;
        socket = new Socket(serverAddress, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Inserici nome utente");
        idClient = stdIn.readLine();
        out.println(idClient);
        String risp = in.readLine();
        while (!risp.equals("K")) {
            System.out.println(risp);
            System.out.println("Inserisci nome utente");
            out.println(stdIn.readLine());
            risp = in.readLine();
        }
        System.out.println("Inserisci password");
        String password = stdIn.readLine();
        out.println(password);
        System.out.println(in.readLine());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String s = in.readLine();
                if (s.equals("DISCONNESSO")) {
                    inChat = false;
                    break;
                }
                System.out.println(s);
            }
        } catch (IOException ex) {
            System.out.println("Errore in run");
        }
    }

    public static void main(String[] args) throws IOException {
        inChat = true;
        Scanner t = new Scanner(System.in);
        Client client = null;
        client = new Client("localhost", 9898);
        client.start();
        String s;
        while (inChat) {
            s = t.nextLine();
            client.out.println(s);
        }
    }

}
