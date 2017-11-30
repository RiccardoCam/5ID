/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Sandro
 */
public class Client extends Application {

    static Socket socket;
    static PrintWriter out;
    static BufferedReader in;
    public static Parent root;
    public static Scene scene;
    public static Stage s;

    public void ConnectToClient() {
        try {
            String serverAddress = "localhost";
            socket = new Socket(serverAddress, 9898);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void start(Stage stage) throws Exception {
        s = stage;
        root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));

        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }
     public void invia(String messaggio){
        out.println(messaggio);
        out.flush();
    }

    public String ricevi(){
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
    public static void main(String[] args) throws IOException {
        Client c = new Client();
        c.ConnectToClient();
        launch(args);
    }

}
