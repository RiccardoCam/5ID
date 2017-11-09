/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risponditore;

import grafo.Arco;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Filippo
 */
public class ClientTcp {

    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader stdIn;
    private Banca gestione;

    public void connectToServer() throws IOException {

        String serverAddress = "127.0.0.1";
        String userInput;
        gestione = new Banca();

        Socket socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        stdIn = new BufferedReader(new InputStreamReader(System.in));

        // Consume the initial welcoming messages from the server
        for (int i = 0; i < 3; i++) {
            System.out.println(in.readLine());
        }
        int nodoCorr = 0;

        boolean controllo = false;
        ArrayList<Arco<String>> adi = null;
        Connection conn = null;
        String url = "jdbc:sqlite:./users.db";
        while ((userInput = stdIn.readLine()) != null) {
            controllo=false;
            adi = gestione.getDomande().adiacenti(gestione.getDomande().nodi.get(nodoCorr));
            String[] app = null;
            if (nodoCorr == 5) {
                String schermo = userInput;
                String[] app1 = schermo.split(" ");
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
                                System.out.println("utente accettato, il suo saldo registrato è " + rs.getString("Saldo"));
                                nodoCorr = 7;//fine
                            }
                        }

                    }

                    if (!aiuto) {
                        System.out.println("nickname/ password sbagliate \n RIPROVA");
                        nodoCorr = gestione.getDomande().nodi.indexOf(gestione.getDomande().listaArchi.get(nodoCorr).get(3).dest);
                    }
                    break;
                } catch (Exception e) {

                }

            }
            boolean continua = true;
            if (nodoCorr == 6) {
                String schermo = userInput;
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
                            System.out.println("UTENTE GIA' PRESENTE");
                            nodoCorr = gestione.getDomande().nodi.indexOf(gestione.getDomande().listaArchi.get(nodoCorr).get(0).dest);
                            userInput = "NOT";
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
                        userInput = "OK";
                    } catch (Exception e) {
                    }
                }

            }
            for (Arco<String> x : adi) {
                for (int i = 0; i < x.informazioni.size(); i++) {
                    if (x.informazioni.get(i).equals(userInput.toUpperCase())) {
                        controllo = true;
                        nodoCorr = gestione.getDomande().nodi.indexOf(x.dest);
                        System.out.println(gestione.getDomande().nodi.get(nodoCorr).nodo + "\n");

                    }
                }

            }
            if (!controllo) {
                System.out.println("RISPOSTA NON VALIDA");
            }
            if (gestione.getDomande().nodi.get(nodoCorr).nodo.equals("FINE")) {
                System.out.println("FINE");
                socket.close();
                break;
            }
            out.println(userInput);
        }
    }

    /**
     * Runs the client application.
     */
    public static void main(String[] args) throws Exception {
        ClientTcp client = new ClientTcp();
        client.connectToServer();
    }
}
