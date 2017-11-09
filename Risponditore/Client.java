package ristorante;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Luca
 */
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
        while (true) {
            System.out.println("" + in.readLine());
            userInput = stdIn.readLine();
            out.println(userInput);
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.connectToServer();
    }
}
