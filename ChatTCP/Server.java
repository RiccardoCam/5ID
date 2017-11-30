package chattcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luca.modolo
 */

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Server avviato");
        TreeMap<String, ClientThread> clients = new TreeMap<>();
        ServerSocket listener = new ServerSocket(9898);
        SQLHelper db = new SQLHelper();
        try {
            while (true) {
                ClientThread c;
                try {
                    c = new ClientThread(listener.accept(), clients, db); //se il login fallisce non si crea un nuovo client 
                    c.start();
                    clients.put(c.getIdCliente(), c);
                } catch (LoginException ex) {
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
    private final TreeMap<String, ClientThread> clients;// dove vengono salvati i vari clienti
    private String idCliente;
    private SQLHelper db;
    private static final int FIND_FRIEND = 1;
    private static final int CHAT = 2;
    private static final int DISCONNECTION = 3;
    private int stato = FIND_FRIEND;

    public ClientThread(Socket socket, TreeMap<String, ClientThread> clients, SQLHelper db) throws IOException, LoginException {
        this.socket = socket;
        this.clients = clients;
        this.db = db;
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        idCliente = in.readLine();

        String password = in.readLine();
        if (!db.esisteUtente(idCliente)) {
            db.creaUtente(idCliente, password);
            System.out.println("Sign up: " + idCliente);
        } else {
            if (db.passwordCorretta(idCliente, password)) {
                System.out.println("Utente già presente " + idCliente);
            } else {
                out.println("Errore");
                socket.close();
                throw new LoginException();
            }
        }
        out.println("Ok");
        System.out.println(idCliente);
        out.println(clients.toString());
    }

    @Override
    public String toString() {
        return idCliente + " è in linea.\n";
    }

    public String getIdCliente() {
        return idCliente;
    }
    private ClientThread friend = null;

    public ClientThread getFriend() {
        return friend;
    }

    public void setFriend(ClientThread ct) {
        this.friend = ct;
    }

    private synchronized void findFriend(String nomeUtente) throws IOException {
        friend = clients.get(nomeUtente);
        if (friend != null) {
            if (friend == this) {
                inviaMessaggio("Non puoi chattare con te stesso");
                friend = null;
            } else {
                if (friend.getFriend() == null) {
                    friend.setFriend(this);
                    out.println(friend.getIdCliente() + " è connesso con te");
                    friend.inviaMessaggio("Sei connesso con " + this.getIdCliente());
                    stato = CHAT;
                    friend.stato = CHAT;
                } else {
                    inviaMessaggio(friend.getIdCliente() + " non è disponibile");
                    friend = null;
                }
            }
        }
    }

    private synchronized void chat(String messaggio) throws IOException {
        if (messaggio == null) {
            return;
        }
        String msg = idCliente + ": " + messaggio;

        System.out.println(msg);
        friend.inviaMessaggio(msg);
    }

    private synchronized void disconnessione() throws IOException {
        if (friend != null) {
            friend.friend = null; //quello con cui sto chattando
            friend.inviaMessaggio("L'utente " + idCliente + " si è disconnesso");
            friend.stato = FIND_FRIEND;
        }
        clients.remove(idCliente);
        System.out.println(idCliente + " disconnesso");
        socket.close();
    }

    @Override
    public void run() {
        try {
            while (stato != DISCONNECTION) {
                String msg = in.readLine();
                if (msg == null || msg.equals(".")) {
                    stato = DISCONNECTION;
                }
                switch (stato) {
                    case FIND_FRIEND:
                        findFriend(msg);
                        break;
                    case CHAT:
                        chat(msg);
                        break;
                    case DISCONNECTION:
                        disconnessione();
                        break;
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void inviaMessaggio(String messaggio) {
        out.println(messaggio);
    }

}
