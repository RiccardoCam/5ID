package risponditore;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import grafo.Arco;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
        boolean controllo = false;
        ArrayList<Arco<String>> adi = null;
        Connection conn = null;
        String url = "jdbc:sqlite:./users.db";
        int nodoCorr = 0;

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
                out.println(gestione.getDomande().nodi.get(0).nodo + "\n");
                while (true) {
                    String input = in.readLine();
                    controllo = false;
                    adi = gestione.getDomande().adiacenti(gestione.getDomande().nodi.get(nodoCorr));
                    String[] app = null;
                    if (nodoCorr == 5) {
                        String schermo = input;
                        String[] app1 = schermo.split(" ");
                        System.out.println("nome "+app1[0]);
                        System.out.println("pass "+app1[1]);
                        Statement st = null;
                        ResultSet rs = null;
                        try {
                            conn = DriverManager.getConnection(url);
                            st = conn.createStatement();
                            rs = st.executeQuery("SELECT * FROM utenti");
                            boolean aiuto = false;
                            while (rs.next()) {
                                if (rs.getString("Nickname").equals(app1[0])) {
                                    if (rs.getString("Password").equals(app1[1])) {
                                        aiuto = true;
                                        
                                        out.println("utente accettato, il suo saldo registrato è " + rs.getString("Saldo"));
                                        nodoCorr = 7;//fine
                                    }
                                }

                            }

                            if (!aiuto) {
                                out.println("nickname/ password sbagliate \n RIPROVA");
                                nodoCorr = gestione.getDomande().nodi.indexOf(gestione.getDomande().listaArchi.get(nodoCorr).get(3).dest);
                            }
                            break;
                        } catch (Exception e) {

                        }

                    }
                    boolean continua = true;
                    if (nodoCorr == 6) {
                        String schermo = input;
                        app = schermo.split(" ");
                        Statement st = null;
                        ResultSet rs = null;
                        try {
                            conn = DriverManager.getConnection(url);
                            st = conn.createStatement();
                            rs = st.executeQuery("SELECT * FROM utenti");
                            boolean aiuto = false;
                            while (rs.next()) {
                                if (rs.getString("Nickname").equals(app[0])) {
                                    continua = false;
                                    out.println("UTENTE GIA' PRESENTE");
                                    nodoCorr = gestione.getDomande().nodi.indexOf(gestione.getDomande().listaArchi.get(nodoCorr).get(0).dest);
                                    input = "NOT";
                                }

                            }
                        } catch (Exception e) {
                        }
                        if (continua) {
                            PreparedStatement st1 = null;
                            try {
                                conn = DriverManager.getConnection(url);
                                String sql = "INSERT INTO utenti(Nickname,Password,saldo) VALUES('" + app[0] + "','" + app[1] + "','" + app[2] + "')";
                                st1 = conn.prepareStatement(sql);
                                st1.execute();
                                System.out.println("inserito");
                                input = "OK";
                            } catch (Exception e) {
                            }
                        }

                    }
                    for (Arco<String> x : adi) {
                        for (int i = 0; i < x.informazioni.size(); i++) {
                            if (x.informazioni.get(i).equals(input.toUpperCase())) {
                                controllo = true;
                                nodoCorr = gestione.getDomande().nodi.indexOf(x.dest);
                                System.out.println("inviato "+gestione.getDomande().nodi.get(nodoCorr).nodo);
                                out.println(gestione.getDomande().nodi.get(nodoCorr).nodo + "\n");

                            }
                        }

                    }
                    if (!controllo) {
                        out.println("RISPOSTA NON VALIDA");
                    }
                    if (gestione.getDomande().nodi.get(nodoCorr).nodo.equals("FINE")) {
                        out.println("FINE");
                        socket.close();
                        break;
                    }
                    if (input == null || input.equals(".")) {
                        out.println("CLIENT NUMERO #" + clientN + "SI E' DISCONNESSO");
                        socket.close();
                        break;
                    }
                    
                    System.out.println("nodoCorr="+ nodoCorr);
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
