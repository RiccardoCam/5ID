/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Utente
 */
public class Cliente {
      private BufferedReader in;
    private PrintWriter out;
    private BufferedReader stdIn;

    public void connectToServer() throws IOException {

        String serverAddress = "127.0.0.1";
        String userInput;

        
        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, 9898);
        // in = new BufferedReader(new InputStreamReader(System.in));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println(in.readLine()); //welcome
        out.println(stdIn.readLine()); //name
        
        do {
            System.out.println(in.readLine());
            System.out.println(in.readLine());
            userInput = stdIn.readLine();
            out.println(userInput);
        } while (!userInput.equals("End"));
        //System.out.println(in.readLine());
    }

    public static void main(String[] args) throws IOException {
        Cliente cliente = new Cliente();
        cliente.connectToServer();
    }
}
