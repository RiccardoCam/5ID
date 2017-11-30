/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Alvise
 */
public class FXMLSignInController implements Initializable {

    protected String username, password;
    private Random r = new Random();

    @FXML
    private Label error, app;
    @FXML
    private TextField user;
    @FXML
    private TextField pswd;
    
    

    @FXML
    public boolean check() throws InterruptedException, SQLException, IOException {
        Arrays.fill(Client.receiveData, (byte)0); // try to remove
        Arrays.fill(Client.sendData, (byte)0); // try to remove
        System.out.println("chiamato");
        username = user.getText();
        password = pswd.getText();
        if (username.equals("") && password.equals("")) {
            user.clear();
            pswd.clear();
            error.setText("Inserire i dati");
            error.setVisible(true);
            return false;
        } else {
            if (username.equals("")) {
                user.clear();
                pswd.clear();
                error.setText("Username mancante");
                error.setVisible(true);
                return false;
            }
            if (password.equals("")) {
                user.clear();
                pswd.clear();
                error.setText("Password mancante");
                error.setVisible(true);
                return false;
            }
        }
        String ris = "SIGNIN"+username+";"+password+";";
        Client.sendData= ris.getBytes();
        Client.dp= new DatagramPacket(Client.sendData, Client.sendData.length, Client.address, 9898);
        Client.socket.send(Client.dp);
        

        System.out.println("mando " + username);

        Client.dp= new DatagramPacket(Client.receiveData, Client.receiveData.length);
        Client.socket.receive(Client.dp);
        ris = new String(Client.dp.getData());
        System.out.println("RICEVUTO DAL SERVER: " + ris);
        
        if (ris.matches("CHAT.+")) { 
            Client.user=username;
            System.out.println(username + " username");
            Client.r = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Client.scene = new Scene(Client.r);
            Client.s.setScene(Client.scene);
            Client.s.show();
           

            return true;

        } else {
            user.clear();
            pswd.clear();
            error.setVisible(true);
        }
        return false;

    }

    public void signUp() throws IOException, Exception {
        Client.r = FXMLLoader.load(getClass().getResource("FXMLSignUp.fxml"));
        Client.scene = new Scene(Client.r);
        Client.s.setScene(Client.scene);
        Client.s.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

}
