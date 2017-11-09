package risponditore;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author Filippo
 */
public class ServerTcp {

    private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private static Banca gestione;

    public static void executeTask(Task task) {

        executor.execute(task);

    }

    public static void main(String[] args) throws Exception {
        System.out.println("SERVER AVVIATO.");
        int clientN = 0;
        ServerSocket socklist = new ServerSocket(9898);
        try {
            while (true) {
                Task task = new Task(socklist.accept(), clientN++);
                executeTask(task);
            }
        } finally {
            endServer();
        }
    }

    private static class Task implements Runnable {

        private Socket socket;
        private int clientN;

        public Task(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientN = clientNumber;
            gestione = new Banca();
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //the line read
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                System.out.println("NUOVA CONNESSIONE CON IL CLINET NUMERO #" + clientN + " ,socket: " + socket);// Send a welcome message.
                out.println("CIAO; TU SEI IL CLIENT NUMERO  #" + clientN + ".");// send the clientnumber to client  
                out.println(gestione.getDomande().nodi.get(0).nodo + "\n");
                while (true) {
                    String input = in.readLine();
                    if (input == null || input.equals(".")) {
                        out.println("CLIENT NUMERO #" + clientN + "SI E? DISCONNESSO");
                        socket.close();
                        break;
                    }
                    out.println(input.toUpperCase());
                }
            } catch (IOException e) {
                System.out.println("ERRORE# " + clientN + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                System.out.println("CONNESSIONE CON # " + clientN + " CHIUSA!!");
            }
        }
    }

    public static void endServer() {
        executor.shutdown();
    }
}
