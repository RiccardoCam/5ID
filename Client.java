package risponditore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private BufferedReader in;
    private BufferedReader inSystem;
    private PrintWriter out;

    protected String name;

    public void connect() throws IOException {
        System.out.println("Connecting to server...");
        String serverAddress = "127.0.0.1";
        String userInput = "";
        Socket s = new Socket(serverAddress, 8080);
        System.out.println("Client connected");
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
        inSystem = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Server says: " + in.readLine());
        this.name = inSystem.readLine();
        out.println("Ciao " + name);
        while (!userInput.equals("exit")) {
            String domanda = in.readLine();
            System.out.println("Server asks to "+name+": " + domanda);
            userInput = inSystem.readLine();
            
            out.println(userInput);
            

        }

    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect();
    }
}
