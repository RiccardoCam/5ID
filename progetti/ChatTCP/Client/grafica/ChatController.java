/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.grafica;

import Client.Client;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 *
 * @author Sandro
 *
 */
public class ChatController implements Initializable {

    private Thread t;
    @FXML
    private Label nickname;
    @FXML
    private Button closeButton;
    @FXML
    private TextField daInviare;
    @FXML
    private ScrollPane chat;
    @FXML
    private TextArea txtArea;

    private void invia(String message) {
        Client.client.invia(message);
    }

    private String leggi() {
        return Client.client.leggi();
    }

    @FXML
    public void quit() throws InterruptedException, IOException {
        this.invia("finito");
        this.invia("Elimina");
        t.interrupt();
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    public void invio() {
        txtArea.appendText("ME:\n" + daInviare.getText() + "\n\n");

        invia(daInviare.getText());
        daInviare.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String dest = this.leggi();
        this.nickname.setText(dest);

        t = new Thread(() -> {
            while (true) {
                daInviare.setOnKeyPressed(ke -> {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        System.out.println("asdf");
                        invio();
                    }
                });
                String mexDest = this.leggi();
                if (!mexDest.isEmpty()) {

                    txtArea.appendText(mexDest + "\n");
                }
            }
        });

        t.start();

    }
}
