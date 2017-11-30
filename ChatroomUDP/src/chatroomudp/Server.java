/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomudp;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static ArrayList<String> ipClient;
    public static ArrayList<Integer> portClient;
    public static String username;
    public static String password;
    public static String messaggio = "";
    public static int posUtente = 0;
    public static String nomeUtente, mittente, destinatario, mex, ultimoMessaggio;
    public static int contaUtenti = 0, contatoreMessaggi = 0;
    public static byte[] buf;
    public static DatagramPacket dp;
    public static byte[] receiveData = new byte[1024];
    public static byte[] sendData = new byte[1024];
    public static MulticastSocket socket = null;
    public static InetAddress address;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        ipClient = new ArrayList<>();
        portClient= new ArrayList<>();
        client = new ArrayList<>();
        utenti = new ArrayList<>();
        arrayMessaggi = new ArrayList<>();
        arrayBooleanMessaggi = new ArrayList<>();
        databaseUsers = new SQLHelperUsers();
        databaseMessages = new SQLHelperMessages();
        utentiConnessi = new ArrayList<>();
        buf = new byte[1024];
        socket = new MulticastSocket(9898);
        address = InetAddress.getByName("224.0.0.1");


        while (true) {
            dp = new DatagramPacket(buf, buf.length);
            socket.receive(dp);
            String str = new String(dp.getData());
            System.out.println("RICEVUTO: " + str);
            System.out.println(str.length());
            connection(str);

        }
    }

    public static void connection(String messaggio) throws SQLException, IOException {
        Arrays.fill(receiveData, (byte) 0); // try to remove
        Arrays.fill(sendData, (byte) 0); // try to remove

        System.out.println("VERIFICO IL MESSAGGIO");

        if (messaggio.matches("SIGNIN.+")) {
            System.out.println("SERVER CIAO");
            messaggio = messaggio.substring("SIGNIN".length(), messaggio.length());
            String[] app = messaggio.split(";");
            username = app[0];
            password = app[1];

            if (verificaCredenziali(username, password, true)) {
                System.out.println("SERVER VERIFICA CREDENZIALI PASSATA");
                System.out.println(client.size() + " SERVER SIZE");
                inviaMessaggio("CHAT");
                
                
                for (int i = 0; i < utentiConnessi.size(); i++) {
                    if (utentiConnessi.get(i) != null) {
                        contaUtenti++;
                    }
                }
                String s=""+contaUtenti+";";
                for (int i = 0; i < utentiConnessi.size(); i++) {
                    if (utentiConnessi.get(i) != null) {
                        s+=utentiConnessi.get(i)+";";
                    }
                }
                System.out.println("s: "+s);
                inviaMessaggio(s);
                contaUtenti = 0;
            } else {
                inviaMessaggio("ERRORE");
            }

        } else if (messaggio.matches("SIGNUP.+")) {
            System.out.println("SERVER CIAO");
            messaggio = messaggio.substring("SIGNUP".length(), messaggio.length());
            String[] app = messaggio.split(";");
            username = app[0];
            password = app[1];

            if (verificaCredenziali(username, password, false)) {
                System.out.println("SERVER VERIFICA CREDENZIALI PASSATA");
                System.out.println(client.size() + " SERVER SIZE");
                inviaMessaggio("CHAT");

                for (int i = 0; i < utentiConnessi.size(); i++) {
                    if (utentiConnessi.get(i) != null) {
                        contaUtenti++;
                    }
                }
                String s=""+contaUtenti+";";
                for (int i = 0; i < utentiConnessi.size(); i++) {
                    if (utentiConnessi.get(i) != null) {
                        s+=utentiConnessi.get(i)+";";
                    }
                }
                inviaMessaggio(s);
                contaUtenti = 0;
            } else {
                inviaMessaggio("ERRORE");
            }


        } else if (messaggio.matches("INVIAMESSAGGIO.+")) {
            messaggio = messaggio.substring("INVIAMESSAGGIO".length(), messaggio.length());
            String[] app = messaggio.split(";");
            mex = app[0];
            mittente = app[1];
            addDbMessage(mex, mittente);
            System.out.println("messaggio ricevuto: " + mex + "; mittente: "+mittente);
            dp = new DatagramPacket(mex.getBytes(), mex.getBytes().length, address, 4443);
            //inviarlo a tutti i client connessi con multicastsocket

        } else if (messaggio.matches("VISUALIZZACHAT.+")) {
            
            mittente = messaggio.substring("VISUALIZZACHAT".length(), messaggio.length());
            
            recuperaChat(mittente);
            System.out.println("mittente " + mittente);
            System.out.println("n messaggi: " + arrayMessaggi.size());
            String s = ""+arrayMessaggi.size()+";";
            for (int i = 0; i < arrayMessaggi.size(); i++) {
                s+=arrayMessaggi.get(i)+";";
                System.out.println("messaggio " + arrayMessaggi.get(i));
                s+=arrayBooleanMessaggi.get(i)+";";
                System.out.println("boolean " + arrayBooleanMessaggi.get(i));
            }
            inviaMessaggio(s);
            arrayMessaggi.clear();
            arrayBooleanMessaggi.clear();
        }
        
    }

    public static boolean verificaCredenziali(String user, String pswd, boolean app) throws SQLException {
        if (app) {
            try {
                System.out.println(user);
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

    public static void addDbMessage(String messaggio, String mittente) throws SQLException {
        try {
            ResultSet ris = databaseMessages.executeQuery("SELECT * FROM messages"); //prende i messaggi inviati
            while (ris.next()) {
                contatoreMessaggi = ris.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        contatoreMessaggi++;
        databaseMessages.executeUpdate("INSERT INTO messages (id, testo, mittente) VALUES ('" + contatoreMessaggi + "', '" + messaggio + "', '" + mittente + "')");

    }

    public static void recuperaChat(String mittente) {
        String app = "";
        try {
            ResultSet ris = databaseMessages.executeQuery("SELECT * FROM messages"); //prende i messaggi inviati e ricevuti
            System.out.println("entro in recupera chat");
            while (ris.next()) {
                arrayMessaggi.add(ris.getString("testo"));
                app = ris.getString("mittente");
                if (app.equals(mittente)) {
                    arrayBooleanMessaggi.add(true); //true se il messaggio è del mittente
                } else {
                    arrayBooleanMessaggi.add(false); //false di qualcun'altro
                }

            }
            System.out.println("MESSAGGI " + Arrays.toString(arrayMessaggi.toArray()));

        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void inviaMessaggio(String messaggioDaInviare) throws IOException{
        Arrays.fill(sendData, (byte) 0); // try to remove
        sendData = messaggioDaInviare.getBytes();
        dp = new DatagramPacket(sendData, sendData.length, address, 9898);
        socket.send(dp);
    }
}
