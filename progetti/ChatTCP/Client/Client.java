/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Sandro
 */
public class Client {

    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    public static Parent root;
    public static Scene scene;
    public static Stage s;
    public static Client client = new Client();

    public Client() {
        try {
            String serverAddress = "localhost";
            socket = new Socket(serverAddress, 9898);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void invia(String messaggio) {
        out.println(messaggio);
    }

    public String leggi() {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return "";
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
}
