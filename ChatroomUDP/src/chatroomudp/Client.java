/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import javafx.application.Application;
import static javafx.application.Application.launch;
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

    public static Client client;
    public static String messaggio;

    public static String user = "";
    public static DatagramPacket dp;
    
    public static byte[] receiveData = new byte[1024];
    public static byte[] sendData = new byte[1024];
    public static MulticastSocket socket;
    public static InetAddress address;

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
        socket = new MulticastSocket(9898);
        address = InetAddress.getByName("224.0.0.1");
        socket.joinGroup(address);
        client = new Client();
        
        
        launch(args);
    }


}
