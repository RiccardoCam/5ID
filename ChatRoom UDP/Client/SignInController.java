/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientchat;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Filippo
 */
public class SignInController implements Initializable {

    @FXML
    private Button conferma;
    @FXML
    private Button signUp;
    @FXML
    private TextField utente;
    @FXML
    private PasswordField pass;
    @FXML
    private Label Erro;

    private DatagramSocket socket;
    private byte[] buffer;

    protected String address = "localhost";
    int p = 2222;
    public Stage prevStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buffer = new byte[1024];
        try {
            socket = new DatagramSocket();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

    @FXML
    private void b_Accedi() throws IOException {
        try {
            byte[] buffer = ("login:" + utente.getText() + ":" + pass.getText()).getBytes();
            InetAddress dest = InetAddress.getByName(address);
            DatagramPacket message = new DatagramPacket(buffer, buffer.length, dest, p);
            socket.send(message);
            System.out.println("ciao");
            String nmUt = utente.getText();
            String stream = "";
            byte[] buffer1 = new byte[1024];
            DatagramPacket messageRic = new DatagramPacket(buffer1, buffer1.length);
            socket.receive(messageRic);
            String received = new String(messageRic.getData(), 0, messageRic.getLength());
            if (received.equals("Errore")) {
                Erro.setText("Credenziali inesistenti o utente già connesso");
            } else if (received.equals("entrato")) {
                Stage stage = new Stage();
                FXMLDocumentController next = new FXMLDocumentController(socket, nmUt);
                FXMLLoader myLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
                Pane myPane = (Pane) myLoader.load();
                next = (FXMLDocumentController) myLoader.getController();
                next.setPrevStage(stage);
                Scene myScene = new Scene(myPane);
                stage.setScene(myScene);
                stage.sizeToScene();
                prevStage.close();
                stage.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void b_SignUp() throws IOException {
        try {
            byte[] buffer = ("registrazione:" + utente.getText() + ":" + pass.getText()).getBytes();
            InetAddress dest = InetAddress.getByName(address);
            DatagramPacket message = new DatagramPacket(buffer, buffer.length, dest, p);
            socket.send(message);
            String stream = "";
            byte[] buffer1 = new byte[1024];
            DatagramPacket messageRic = new DatagramPacket(buffer1, buffer1.length);
            socket.receive(messageRic);
            String received = new String(messageRic.getData(), 0, messageRic.getLength());
            if (received.equals("Errore")) {
                Erro.setText("nome utente già presente");
            } else if (received.equals("registrato")) {
                utente.setText("");
                pass.setText("");
            }
        } catch (IOException ex) {
        }
    }

}
