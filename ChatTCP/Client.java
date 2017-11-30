package chattcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luca.modolo
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
    
    public Client(String serverAddress, int port, String idClient, String password) throws IOException {
        this.idClient = idClient;
        this.serverAddress = serverAddress;
        this.port = port;
        socket = new Socket(serverAddress, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        out.println(idClient);
        out.println(password);
        if (!in.readLine().equals("Ok")) {
            socket.close();
            throw new IOException();
        }
        
    }
    
    public void mandaMessaggio(String messaggio) throws IOException {
        out.println(messaggio);
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                String msg = in.readLine();
                if(msg == null) {
                    break;
                }
                System.out.println(msg);
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) throws IOException {
        String messaggio;
        Scanner t = new Scanner(System.in);
        Client client = null;
        while (client == null) {
            try {
                System.out.println("Inserisci nome:");
                String nome = t.nextLine();
                System.out.println("Inserisci password:");
                String password = t.nextLine();
                client = new Client("localhost", 9898, nome, password);
            } catch (IOException ex) {
                //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Password errata");
            }
        }
        client.start();
        System.out.println("client connesso");
        while (true) {
            messaggio = t.nextLine();
            client.mandaMessaggio(messaggio);
            if (messaggio.equals(".")) {
                break;
            }
        }
    }
    
}
