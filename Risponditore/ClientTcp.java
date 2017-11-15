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

    public void connectToServer() throws IOException {

        String serverAddress = "127.0.0.1";
        String userInput;

        Socket socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(in.readLine());
        while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("risposta accettata "+in.readLine());
                System.out.println(in.readLine());

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
