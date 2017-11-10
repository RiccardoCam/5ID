/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risponditoreclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Martorel
 */
public class RisponditoreClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("CONNESSIONE AL SERVER");
        String serverIp = "127.0.0.1";
        String input;
        Socket socket = new Socket(serverIp, 8080);
        System.out.println("CONNESSO");
        BufferedReader inServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader inLocale = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter outServer = new PrintWriter(socket.getOutputStream(),true);
        while (true) {
            String messaggioServer = inServer.readLine();
            System.out.println(messaggioServer);
            input = inLocale.readLine();
            outServer.println(input);
        }

    }

}
