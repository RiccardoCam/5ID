/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientchat;

import java.net.*;
import java.net.URL;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 *
 * @author Filippo
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField nUt;
    @FXML
    private TextArea area;
    Boolean isConn = false;
    @FXML
    private TextField areaMess;

    @FXML
    private Label err;
    String address = "localhost";

    protected static String user;
    protected ArrayList<String> users = new ArrayList();
    protected static DatagramSocket soc;
    protected Stage prevStage;

    public FXMLDocumentController(DatagramSocket sock, String nomeUtente) {
        this.soc = sock;
        user = nomeUtente;
    }

    public FXMLDocumentController() {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nUt.setText(user);
        nUt.setEditable(false);
        Thread IncomingReader = new Thread(new IncomingReader(this.user));
        IncomingReader.start();
    }

    public void userAdd(String data) {
        users.add(data);
    }

    public void userRemove(String data) {
        area.appendText(data + " is now offline.\n");
    }
    
    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public class IncomingReader implements Runnable {

        public String nomeUtente;

        public IncomingReader(String nomeUtente) {
            this.nomeUtente = nomeUtente;
        }

        @Override
        public void run() {
            String[] data;
            String stream;
            try {
                while (true) {
                byte[] buffer1 = new byte[1024];
                DatagramPacket messageRic = new DatagramPacket(buffer1, buffer1.length);
                soc.receive(messageRic);
                String received = new String(messageRic.getData(), 0, messageRic.getLength());
                    data = received.split(":");
                    if (data[2].equals("Chat")) {
                        System.out.println("chat \n" + this.nomeUtente + "\n");
                            area.appendText(data[1] + ": " + data[0] + "\n");
                            area.positionCaret(area.getLength());
                    } else if (data[2].equals("Disconnect")) {
                        userRemove(data[0]);
                    }
                }
            } catch (Exception ex) {
            }
        }
    }

    @FXML
    private void b_disconnectActionPerformed(ActionEvent event) {
        String bye = (user + ": :Disconnect");
        try {
            byte[] buffer = bye.getBytes();
            InetAddress dest = InetAddress.getByName(address);
            DatagramPacket message = new DatagramPacket(buffer, buffer.length, dest, 2222);
            soc.send(message);
        } catch (Exception e) {

        }
        try {
            area.appendText("Disconnected.\n");
            soc.close();
        } catch (Exception ex) {
        }
        isConn = false;
        System.exit(0);
    }

    @FXML
    private void b_sendActionPerformed(ActionEvent event) {
        String nothing = "";
        if ((areaMess.getText()).equals(nothing)) {
            areaMess.setText("");
            areaMess.requestFocus();
        } else {
            try {
                byte[] buffer = (user + ":" + areaMess.getText() + ":" + "Chat").getBytes();
                InetAddress dest = InetAddress.getByName(address);
                DatagramPacket message = new DatagramPacket(buffer, buffer.length,dest, 2222);
                soc.send(message);
            } catch (Exception ex) {
                area.appendText("errore mess non inviato  \n");
            }
            areaMess.setText("");
            areaMess.requestFocus();
        }

        areaMess.setText("");
        areaMess.requestFocus();
    }

}
