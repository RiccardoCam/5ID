package client;

import java.io.*;
import java.net.*;

public class Client {

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
    
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connectToServer();
    }

}