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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author Sandro
 */
public class Server {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Server running..");
        ServerSocket listener = new ServerSocket(9898);
        ThreadPoolExecutor ex = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        try {
            while (true) {
                Task t = new Task(listener.accept());
                ex.execute(t);
                System.out.println("Clienti on: " + ex.getPoolSize());
            }
        } finally {
            listener.close();
        }

    }

    /**
     *
     */
    public static class Task implements Runnable {

        String ID;
        Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }

        /**
         *
         */
        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                out.println("You are now connected.");
                out.println("Write anything to start, Press Enter to quit");
                String input = in.readLine();
                if (input.matches("")) {
                    out.println("You have been disconnected");
                    System.out.println("Connection with the client lost");
                    socket.close();
                }
                if (!input.equals("")) {
                    Hotel h = new Hotel();
                    out.println("Hotel KIM, come ti chiami?");
                    String inputId = in.readLine();
                    if (!inputId.equals("")) {
                        this.ID = inputId;
                        out.println(ID + ": " + "Come posso aiutarti?prenotare,visitare,informazioni?");
                        System.out.println("connesso con: " + ID);
                    } else if (inputId.matches("")) {
                        out.println("You have been disconnected");
                        System.out.println("Connection with the client lost");
                        socket.close();
                    }

                    while (true) {
                        String input1 = in.readLine();
                        if (!input1.isEmpty()) {
                            System.out.println(ID + ": " + input1);

                            String domanda = h.getDomanda(input1);
                            if (!domanda.equals("end")) {
                                out.println(ID + ": " + domanda);
                            } else {
                                out.println(ID + ": " + "Ok, arrivederci!");
                                socket.close();
                                break;
                            }
                        } else {
                            out.println(ID + ": " + "You have been disconnected");
                            socket.close();
                            break;
                        }
                    }
                } else {

                    out.println(ID + ": " + "?????-Risposta non è valida.");
                    out.println(ID + ": " + "Connection Interrupted");
                    System.out.println(ID + ": " + "Connection with the client lost");
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println(ID + ": " + "Error handling client# " + "" + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(ID + ": " + "Couldn't close a socket, what's going on?");
                }
            }
        }

    }

}
