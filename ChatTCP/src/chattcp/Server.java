/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattcp;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.logging.*;

/**
 *
 * @author Alvise
 */
public class Server {

    private static SQLHelperUsers databaseUsers;
    private static SQLHelperMessages databaseMessages;
    private static ArrayList<PrintWriter> client;
    public static ArrayList<String> utentiConnessi;
    public static ArrayList<String> arrayMessaggi;
    public static ArrayList<Boolean> arrayBooleanMessaggi;
    public static ArrayList<Connection> utenti;
    public static String username;
    public static String password;
    public static String messaggio = "";
    public static int posUtente = 0;
    public static String nomeUtente, mittente, destinatario, mex, ultimoMessaggio;
    public static int contaUtenti = 0, contatoreMessaggi = 0;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        client = new ArrayList<>();
        utenti = new ArrayList<>();
        arrayMessaggi = new ArrayList<>();
        arrayBooleanMessaggi = new ArrayList<>();
        databaseUsers = new SQLHelperUsers();
        databaseMessages = new SQLHelperMessages();
        ServerSocket listener = new ServerSocket(9898);
        utentiConnessi = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        int idCliente = 0;

        try {
            while (true) {
                Connection c = new Connection(listener.accept(), idCliente);
                executor.submit(c);
                utenti.add(c);
                idCliente++;
                try {
                    ResultSet ris = databaseMessages.executeQuery("SELECT * FROM messages"); //prende il numero di messaggi gia salvati nel database
                    while (ris.next()) {
                        contatoreMessaggi = ris.getInt("id");
                        ultimoMessaggio = ris.getString("testo");
                    }
                    System.out.println("idMessaggio: " + contatoreMessaggi);
                } catch (SQLException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } finally {
            listener.close();
        }

    }

    public static class Connection implements Runnable {

        private Socket socket;
        private int numeroCliente;
        private PrintWriter out;
        private BufferedReader in;
        private String nomeUtente;

        public Connection(Socket socket, int idCliente) throws IOException {
            this.socket = socket;
            this.numeroCliente = idCliente;
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connessione con il client #" + numeroCliente + " all'indirizzo: " + socket.getInetAddress());
            client.add(out);
        }

        @Override
        public void run() {
            try {
                System.out.println("CONNECTION");
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (true) {
                    messaggio = in.readLine();
                    System.out.println(messaggio);

                    
                    if (messaggio.equals("SIGNIN")) {
                        System.out.println("SERVER CIAO");
                        username = in.readLine();
                        password = in.readLine();
                        if (verificaCredenziali(username, password, true)) {
                            System.out.println("SERVER VERIFICA CREDENZIALI PASSATA");
                            System.out.println(numeroCliente + " SERVER ncliente");
                            System.out.println(client.size() + " SERVER SIZE");
                            out.println("CHAT");
                            for (int i = 0; i < utentiConnessi.size(); i++) {
                                if (utentiConnessi.get(i) != null) {
                                    contaUtenti++;
                                    nomeUtente = username;
                                }
                            }
                            out.println(contaUtenti);
                            for (int i = 0; i < utentiConnessi.size(); i++) {
                                if (utentiConnessi.get(i) != null) out.println(utentiConnessi.get(i));
                            }
                            contaUtenti = 0;
                        } else out.println("ERRORE");
                        out.flush();

                        
                        
                    } else if (messaggio.equals("SIGNUP")) {
                        username = in.readLine();
                        password = in.readLine();

                        if (verificaCredenziali(username, password, false)) {
                            nomeUtente = username;
                            out.println("CHAT");

                            for (int i = 0; i < utentiConnessi.size(); i++) {
                                if (utentiConnessi.get(i) != null){
                                    contaUtenti++;
                                }
                            }
                            out.println(contaUtenti);
                            for (int i = 0; i < utentiConnessi.size(); i++) {
                                if (utentiConnessi.get(i) != null) out.println(utentiConnessi.get(i));
                            }
                            contaUtenti = 0;
                        } else out.println("ERRORE");
                        out.flush();

                        
                        
                    } else if (messaggio.equals("RICHIESTANUTENTI")) {
                        nomeUtente = in.readLine();
                        System.out.println("utente che invia richiesta:" + nomeUtente);
                        System.out.println("mando numero utenti");
                        for (int i = 0; i < utentiConnessi.size(); i++) {
                            if (utentiConnessi.get(i) == null) continue;
                            if (utentiConnessi.get(i).equals(nomeUtente)) {
                                posUtente = i;
                                break;
                            }
                        }
                        for (int i = 0; i < utentiConnessi.size(); i++) {
                            if (utentiConnessi.get(i) != null) contaUtenti++;
                        }
                        out.println(contaUtenti);
                        for (int i = 0; i < utentiConnessi.size(); i++) {
                            if (utentiConnessi.get(i) != null) out.println(utentiConnessi.get(i));
                        }
                        contaUtenti = 0;
                        out.flush();

                        
                        
                        
                    } else if (messaggio.equals("INVIAMESSAGGIO")) {
                        mex = in.readLine();
                        mittente = in.readLine();
                        destinatario = in.readLine();
                        addDbMessage(mex, mittente, destinatario);
                        System.out.println("messaggio ricevuto: " + mex);
                        out.flush();

                        
                        
                    } else if (messaggio.equals("VISUALIZZACHAT")) {
                        mittente = in.readLine();
                        destinatario = in.readLine();
                        recuperaChat(mittente, destinatario);
                        System.out.println("mittente "+ mittente + " destinatario "+ destinatario);
                        System.out.println("n messaggi: " + arrayMessaggi.size());
                        out.println(arrayMessaggi.size());
                        for (int i = 0; i < arrayMessaggi.size(); i++) {
                            out.println(arrayMessaggi.get(i));
                            System.out.println("messaggio " + arrayMessaggi.get(i));
                            out.println(arrayBooleanMessaggi.get(i));
                            System.out.println("boolean " + arrayBooleanMessaggi.get(i));
                        }
                        arrayMessaggi.clear();
                        arrayBooleanMessaggi.clear();
                        out.flush();
                    }
                }
            } catch (IOException e) {
                System.out.println("Errore con il cliente numero " + numeroCliente);
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
            } catch (SQLException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Errore con il cliente numero " + numeroCliente);
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
                }

                System.out.println("Connessione con il cliente numero " + numeroCliente + " chiusa");
                utentiConnessi.set(numeroCliente, null);
                System.out.println(Arrays.toString(utentiConnessi.toArray()));
            }
        }
    }

    public static boolean verificaCredenziali(String user, String pswd, boolean app) throws SQLException {
        if (app) {
            try {
                ResultSet ris = databaseUsers.executeQuery("SELECT * FROM users WHERE username = '" + user + "' AND password = '" + pswd + "'"); //verifica che nel database l'utente sia già registrato
                if (ris.next() && !utentiConnessi.contains(user)) { //il primo valore è antecedente a valori del resultset, perciò se questo valore è l'ultimo il risultato è vuoto
                    utentiConnessi.add(user);
                    System.out.println(Arrays.toString(utentiConnessi.toArray()));
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                ResultSet ris = databaseUsers.executeQuery("SELECT * FROM users WHERE username = '" + user + "'"); //verifica che nel database non sia già presente un username uguale
                if (!ris.next() && !utentiConnessi.contains(user)) {
                    addDbUser(user, pswd);
                    utentiConnessi.add(user);
                    System.out.println(Arrays.toString(utentiConnessi.toArray()));
                    return true;
                } else {
                    System.out.println("username già presente");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return false;
    }

    public static void addDbUser(String user, String pswd) throws SQLException {
        databaseUsers.executeUpdate("INSERT INTO users (username, password) VALUES ('" + user + "', '" + pswd + "')");
    }

    public static void addDbMessage(String messaggio, String mittente, String destinatario) throws SQLException {
        try {
            ResultSet ris = databaseMessages.executeQuery("SELECT * FROM messages"); //prende i messaggi inviati
            while (ris.next()) {
                contatoreMessaggi = ris.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        contatoreMessaggi++;
        databaseMessages.executeUpdate("INSERT INTO messages (id, testo, mittente, destinatario) VALUES ('" + contatoreMessaggi + "', '" + messaggio + "', '" + mittente + "', '" + destinatario + "')");

    }

    public static void recuperaChat(String mittente, String destinatario) {
        String app = "";
        try {
            ResultSet ris = databaseMessages.executeQuery("SELECT * FROM messages WHERE (mittente = '" + mittente + "' AND destinatario = '" + destinatario + "') OR (mittente = '" + destinatario + "' AND destinatario = '" + mittente + "')"); //prende i messaggi inviati e ricevuti
            System.out.println("entro in recupera chat");
            while (ris.next()) {
                System.out.println("ccccc");
                arrayMessaggi.add(ris.getString("testo"));
                app = ris.getString("mittente");
                if (app.equals(mittente)) {
                    arrayBooleanMessaggi.add(true); //true se il messaggio è del mittente
                } else {
                    arrayBooleanMessaggi.add(false); //false del destinatario
                }

            }
            System.out.println("MESSAGGI " + Arrays.toString(arrayMessaggi.toArray()));

        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
