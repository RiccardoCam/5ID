package chattcp;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author Camillo
 */
public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Server avviato");
        TreeMap<String, ClientThread> listaClients = new TreeMap<>();
        ServerSocket listener = new ServerSocket(9898);
        SQLHelper db = new SQLHelper();
        try {
            while (true) {
                ClientThread c;
                try {
                    c = new ClientThread(listener.accept(), listaClients, db);
                    c.start();
                    listaClients.put(c.idCliente, c);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } finally {
            listener.close();
        }
    }
}

class ClientThread extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final TreeMap<String, ClientThread> listaClients;
    protected String idCliente;
    private SQLHelper db;
    private static final int RICERCA = 1;
    private static final int CHAT = 2;
    private static final int DISCONNECTION = 3;
    private int stato = RICERCA;

    public ClientThread(Socket socket, TreeMap<String, ClientThread> listaClients, SQLHelper db) throws IOException {
        this.socket = socket;
        this.listaClients = listaClients;
        this.db = db;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        idCliente = in.readLine();
        while(listaClients.containsKey(idCliente)){
               out.println("Utente già loggato, cambiare nome");
               idCliente = in.readLine();
        }
        out.println("K");
        String password = in.readLine();
        if (!db.esisteUtente(idCliente)) {
            db.creaUtente(idCliente, password);
            System.out.println("Sign up: " + idCliente);
            out.println("utente registrato");
        } else {
            if (db.passwordCorretta(idCliente, password)) {
                out.println("Benvenuto " + idCliente);
            } else {
                out.println("Password errata");
                out.println("DISCONNESSO");
                socket.close();
                
            }
        }
        out.println("Ok");
        System.out.println(idCliente);
        out.println(listaClients.toString());
    }

    @Override
    public String toString() {
        return idCliente + " è in linea.\n";
    }

    private ClientThread clientInChat = null;

    private synchronized void findFriend(String nomeUtente) throws IOException {
        clientInChat = listaClients.get(nomeUtente);
        if (clientInChat != null) {
            if (clientInChat == this) {
                out.println("Non puoi chattare con te stesso");
                clientInChat = null;
            } else {
                if (clientInChat.clientInChat == null) {
                    clientInChat.clientInChat = this;
                    out.println(clientInChat.idCliente + " è connesso con te");
                    clientInChat.out.println("Sei connesso con " + this.idCliente);
                    stato = CHAT;
                    clientInChat.stato = CHAT;
                } else {
                    out.println(clientInChat.idCliente + " non è disponibile");
                    out.println(listaClients.toString());
                    clientInChat = null;
                }
            }
        }
    }

    private synchronized void disconnessione() throws IOException {
        if (clientInChat != null) {  //è connesso con qualcuno;
            clientInChat.clientInChat = null;
            clientInChat.stato = RICERCA;
            clientInChat.out.println("L'utente " + idCliente + " si è disconnesso");
            clientInChat.out.println(listaClients.toString());
            clientInChat = null;
            out.println(listaClients.toString());
            stato = RICERCA;
            System.out.println(stato);
        }
    }

    @Override
    public void run() {
        try {
            while (stato != DISCONNECTION) {
                String s = in.readLine();
                if (s.equals(".")) {
                    stato = DISCONNECTION;
                }
                switch (stato) {
                    case RICERCA:
                        findFriend(s);
                        break;
                    case CHAT:
                        clientInChat.out.println(idCliente + ": " + s);
                        break;
                    case DISCONNECTION:
                        disconnessione();
                        break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Errore in run");
        } finally {
            try {
                listaClients.remove(idCliente);
                System.out.println(idCliente + " disconnesso");
                out.println("DISCONNESSO");
                socket.close();
            } catch (IOException ex) {
                System.out.println("Errore in chiusura socket");
            }
        }
    }
}
