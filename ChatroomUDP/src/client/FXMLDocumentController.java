/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Alvise
 */
public class FXMLDocumentController implements Initializable {

    private Client client;

    @FXML
    private TextField messaggio = new TextField();

    @FXML
    private VBox Vbox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label username;

    @FXML
    public void inviaMessaggio() {
        String s = messaggio.getText();
        if (s.equals("") || s.equals(" ") || s.equals("  ")) {
            System.out.println("Inserisci un messaggio valido");
            return;
        }
        Platform.runLater(() -> {
            messaggio.clear();
            client.invia(username.getText(), s);
            scrollPane.setVvalue(1.0);
        });

    }

    public void riceviMessaggio(String messaggio) {
        if (messaggio == null)return;
        
        Platform.runLater(() -> {
            String[] app = messaggio.split(";");
            
            Label labelUsername = new Label("  "+app[0]+":");
            Label labelMessage = new Label("  "+app[1]);
            
            labelUsername.setWrapText(true);
            labelUsername.setTextFill(Color.RED);
            labelUsername.setPrefWidth(Double.MAX_VALUE);
            labelUsername.setFont(Font.font(null, FontWeight.BOLD, 20));
            labelUsername.setPadding(new Insets(10, 0, 0, 0)); //bordo inferiore settato a 0
            
            labelMessage.setWrapText(true);
            labelMessage.setPrefWidth(Double.MAX_VALUE);
            labelMessage.setFont(Font.font(null, FontWeight.BOLD, 20));
            labelMessage.setPadding(new Insets(-3, 0, 10, 0)); //bordo superiore settato a 1
            
            Vbox.getChildren().add(labelUsername);
            Vbox.getChildren().add(labelMessage);
            scrollPane.setVvalue(1.0);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client = new Client();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        username.setText(Client.user);

        new Thread(() -> {
            while (true) {
                riceviMessaggio(client.ricevi()); //client sempre in ascolto di messaggi provenienti dal server
            }
        }).start();
    }

    public void disconnetti() throws IOException, Exception {
        Platform.exit();
    }

}
