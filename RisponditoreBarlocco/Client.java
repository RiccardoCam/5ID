package client;

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

        
        Socket socket = new Socket(serverAddress, 9898);//Creo la connessione e inizializzo lo stream 
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println(in.readLine()); 
        out.println(stdIn.readLine()); 
        
        do {
            System.out.println(in.readLine());
            System.out.println(in.readLine());
            userInput = stdIn.readLine();
            out.println(userInput);
        } while (!userInput.equals("End"));
        System.out.println(in.readLine());
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connectToServer();
    }
}