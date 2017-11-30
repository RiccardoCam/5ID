/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientchat;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
    private TextField utenteDest;
    @FXML
    private TextField err;

    protected static PrintWriter writer;
    protected static String user;
    protected ArrayList<String> users = new ArrayList();

    protected static BufferedReader reader;
    protected Stage prevStage;

    public FXMLDocumentController(PrintWriter writer, BufferedReader reader, String nomeUtente) {
        this.writer = writer;
        this.reader = reader;
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
                while ((stream = reader.readLine()) != null) {
                    System.out.println("ecccccco    "+stream);
                    if(stream.equals("erroreUtente")){
                        err.setVisible(true);
                        err.setText("Utente non trovato");
                        err.setEditable(false);
                        utenteDest.setText("");
                    }
                    data = stream.split(":");
                    System.out.println("nomeUtente:: " + this.nomeUtente);
                    System.out.println("ecco:" + stream);
                    if (data[2].equals("Chat")) {
                        System.out.println("chat \n" + this.nomeUtente + "\n" + data[3]);
                        if (this.nomeUtente.equals(data[3])||this.nomeUtente.equals(data[1])) {
                            area.appendText(data[1] + ": " + data[0] + "\n");
                            area.positionCaret(area.getLength());
                        }
                    }  else if (data[2].equals("Disconnect")) {
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
            writer.println(bye);
            writer.flush();
        } catch (Exception e) {

        }
        try {
            area.appendText("Disconnected.\n");
        } catch (Exception ex) {
        }
        isConn = false;
    }

    @FXML
    private void b_sendActionPerformed(ActionEvent event) {
        String nothing = "";
        if ((areaMess.getText()).equals(nothing)) {
            areaMess.setText("");
            areaMess.requestFocus();
        } else {
            
            try {
                err.setText("");
                writer.println(user + ":" + areaMess.getText() + ":" + "Chat" + ":" + utenteDest.getText());
                writer.flush(); // flushes the buffer

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
