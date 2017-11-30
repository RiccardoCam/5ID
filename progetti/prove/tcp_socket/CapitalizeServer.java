package capitalizeserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CapitalizeServer {

    public static void main(String[] args) throws Exception {
        System.out.println("The capitalization server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                // crea il thread e lo lancia
                new Capitalizer(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Capitalizer extends Thread {
        private Socket socket;
        private int clientNumber;

        public Capitalizer(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            myLog("Client #"+clientNumber +":"+socket);
        }

        public void run() {
            try {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("Hello, you are client #" + clientNumber + ".");
                out.println("Press 'Enter' or '.' to quit\n");

                // Get messages from the client, line by line; 
                // return them capitalized
                while (true) {
                    String input = new String(in.readLine());
                    if (input.equals("") || input.equals(".")) {
                        out.println("Client #"+clientNumber+" closed.");
                        socket.close();
                        break;
                    }else {
                        myLog("-->recieved from Client #"+clientNumber +": "+input);
                        
                        out.println(input.toUpperCase());
                    }
                }
            } catch (IOException e) {
                myLog("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    // log("Couldn't close a socket, what's going on?");
                }
                myLog("Connection with client# " + clientNumber + " closed");
            }
        }
        
        private void myLog(String message) {
            System.out.println(message);
        }
    }
}