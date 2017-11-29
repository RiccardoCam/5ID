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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Alvise
 */
public class Client extends Application {

    public static Parent r;
    public static Scene scene;
    public static Stage s;
    public static Socket socket;

    public static BufferedReader in, stdIn;
    public static PrintWriter out;

    public static Client client;
    public static String messaggio;
    
    public static String user="";
    
    
    @Override
    public void start(Stage stage) throws Exception {
        s = stage;
        r = FXMLLoader.load(getClass().getResource("FXMLSignIn.fxml"));
        Scene scene = new Scene(r);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        client = new Client();
        client.connectToServer();
        launch(args);
    }


    public void connectToServer() throws IOException {

        String serverAddress = "127.0.0.1";
        socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }

}
