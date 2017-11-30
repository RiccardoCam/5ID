/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Martorel
 */
public class ChatTCPClient {

    private final String SERVERADDRESS = "127.0.0.1";
    private final int SERVERPORT = 9999;

    private BufferedReader input;
    private PrintWriter output;
    private BufferedReader keyboard;
    private Socket clientSocket;

    private String username;
    private String password;

    
    //costruttore e inizializzazionbe della connessione con il server
    public ChatTCPClient() {
        System.out.println("Connessione al server...");
        try {
            this.clientSocket = new Socket(SERVERADDRESS, SERVERPORT);
            System.out.println("Connessione accettata dal server " + clientSocket.getInetAddress() + " con porta " + clientSocket.getPort());
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            keyboard = new BufferedReader(new InputStreamReader(System.in));
            login();
            connectionToAClient();
            new Thread(new Ascolta()).start();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("GoodBye");
            System.exit(0);
        }
    }

    //login nella chat 
    private boolean login() {
        try {
            System.out.println("Username");
            this.username = keyboard.readLine();
            System.out.println("Password");
            this.password = keyboard.readLine();
            output.println(username);
            String apppass = "";
            for (int i = 0; i < this.password.length(); i++) {
                apppass += "*";
            }
            output.println(apppass);
            if (input.readLine().equals("1")) {
                System.out.println("Benvenuto " + username);
                return true;
            } else {
                System.out.println("Login fallito");
                System.out.println("Vuoi registrati?");
                String risposta = keyboard.readLine().toLowerCase();
                if (risposta.equals("si")) {
                    output.println("si");
                    while (true) {
                        if (input.readLine().equals("ok")) {
                            break;
                        }
                        System.out.println("username");
                        output.println(keyboard.readLine());
                        System.out.println("password");
                        output.println(keyboard.readLine());
                    }
                    System.out.println("Sei stato registrato");
                } else {
                    output.println("no");
                    System.out.println("GoodBye");
                    clientSocket.close();
                    System.exit(0);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    //connessione con un client
    private void connectionToAClient() {
        try {
            //ricevo client connessi
            System.out.println("Ricerca client connessi in corso. Attendere...");
            String connessi = input.readLine();
            if (connessi.equals("")) {
                System.out.println("Nessun client disponibile.");
            } else {
                System.out.println(connessi);
            }
            System.out.println(input.readLine());
            output.println(keyboard.readLine());

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }
    }

    //metodo che invia il messaggio
    public void sendMessage(String message) {
        output.println(message);
        output.flush();
    }

    
    public static void main(String[] args) {
        Scanner t = new Scanner(System.in);
        ChatTCPClient client = new ChatTCPClient();
        System.out.println("Ora puoi chattare, digitae --end-- per concludere la counicazione");
        while (true) {
            String message = t.nextLine();
            if (message.equals("--end--")) {
                client.sendMessage("--end--");
                break;
            } else {
                client.sendMessage(message);
            }
        }
        try {
            client.clientSocket.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        }
    }

    //thread che ascolta i messaggi dal server
    class Ascolta implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    String message = input.readLine();
                    System.out.println(message);
                    System.out.print(">");
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("GoodBye");
                    System.exit(0);
                }
            }

        }
    }
}
