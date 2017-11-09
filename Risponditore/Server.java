package ristorante;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca
 */
public class Server extends Thread {

    public static void main(String[] args) throws IOException {
        Server svr = new Server(9898);
        svr.start();
    }

    ServerSocket listener;
    int porta;

    public Server(int porta) throws IOException {
        this.porta = porta;
        listener = new ServerSocket(porta);
    }

    @Override
    public void run() {
        System.out.println("Server avviato");
        while (true) {
            try {
                new Ristorante(listener.accept()).start();
                System.out.println("ristorante creato");
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
