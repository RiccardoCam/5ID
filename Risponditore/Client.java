/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risponditorebar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author andrea zoccarato
 */
public class Client {
    
    private BufferedReader in;
    private BufferedReader inSystem;
    private PrintWriter out;
    private String nome;
    
    public static final String ERROR1="Risposta non valida";
    public static final String ERROR2="Bevanda non valida";
    public static final String ERROR3="Gusto non valido";
    
    public void connect() throws IOException{
        System.out.println("Connecting to server...");
        String serverAddress="127.0.0.1";
        String userInput;
        Socket s=new Socket(serverAddress,8080);
        System.out.println("Client connected");
        
        in=new BufferedReader(new InputStreamReader(s.getInputStream()));
        inSystem=new BufferedReader(new InputStreamReader(System.in));
        out=new PrintWriter(s.getOutputStream(),true);
        
        System.out.println("Client: "+in.readLine());
        this.nome=inSystem.readLine();
        out.println(nome);
        while (true) {
            String domanda=in.readLine();
            System.out.println(nome+": " + domanda);
            userInput=inSystem.readLine();
            if(userInput.equals("fine"))break;
            out.println(userInput); 
        }

    }
    

    public static void main(String[] args) throws IOException {
        Client client=new Client();
        client.connect();
    }
}
