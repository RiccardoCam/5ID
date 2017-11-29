/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ristorante;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Alvise
 */
public class Client {

    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader stdIn;

    public void connectToServer() throws IOException {

        String serverAddress = "127.0.0.1";
        String inputTastiera;
        String domandaCorr, ultimaDomanda, nomeCliente;
        Socket socket = new Socket(serverAddress, 8080);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        stdIn = new BufferedReader(new InputStreamReader(System.in));

        try {
            ultimaDomanda = in.readLine();
            System.out.println(in.readLine());//stampa i messaggi del server
            inputTastiera = stdIn.readLine();//legge da tastiera la risposta del cliente
            out.println(inputTastiera);//invia al server la risposta del cliente
            nomeCliente = in.readLine();
            System.out.println(in.readLine());

            while ((inputTastiera = stdIn.readLine()) != null) {
                out.println(inputTastiera);
                domandaCorr = in.readLine();
                System.out.println(domandaCorr);
                if (domandaCorr.equals(nomeCliente + " " + ultimaDomanda)) {
                    System.out.println(in.readLine());
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
