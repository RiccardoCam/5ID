/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package responder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

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

        for (int i = 0; i < 2; i++) {
            System.out.println(in.readLine());
        }
        while ((userInput = stdIn.readLine()) != null) {
            out.println(userInput);
            String domanda = in.readLine();
            System.out.println(domanda);
            if (domanda.contains("Ok, arrivederci!")||domanda.contains("You have been disconnected")) {
                socket.close();
                break;
            }

        }

    }

    /**
     * Runs the client application.
     */
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.connectToServer();
    }
}
