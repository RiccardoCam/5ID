/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risponditore;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Il Magnifico
 */
public class Client {

    private ArrayList<String> listaOutput;
    private int port = 1500;
    private Scanner scanner = new Scanner(System.in);
    private String serverAddress = "127.0.0.1";

    public void connectToServer() throws IOException, InterruptedException {
        listaOutput = new ArrayList<>();
        Socket socket = new Socket(serverAddress, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        while (true) {
            String s = in.readLine();
            listaOutput.add(s);
            System.out.println(s);
            if (s.equals("inserisci numero")) {
                int i = getInput();
                if (listaOutput.get(listaOutput.size() - 3).indexOf("No") >= 0 && i == 0) {
                    System.out.println("CONNESSIONE CHIUSA");
                    out.println(i);
                    socket.close();
                }
                out.println(i);
            }
        }
    }

    private int getInput() throws InterruptedException {
        int i;
        try {
            i = scanner.nextInt();
        } catch (InputMismatchException ex) {
            System.out.println("immessa stringa");
            System.out.println("Scelto 0 come deafault");
            i = 0;
        }
        return i;
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.connectToServer();
    }
}
