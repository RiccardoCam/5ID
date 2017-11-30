/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattcp;

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
    //ScrollPane non utilizzata
    @FXML
    private ScrollPane chat;
    @FXML
    private TextArea txtArea;

    private void println(String message) {
        Client.out.println(message);

    }

    @FXML
    public void quit() throws InterruptedException, IOException {
        println("finito");
        println("Elimina");
        t.interrupt();
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    public void invio() {
        txtArea.appendText("ME:\n" + daInviare.getText() + "\n\n");
        
        println(daInviare.getText());
        daInviare.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String dest = Client.in.readLine();
            this.nickname.setText(dest);
        } catch (IOException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }

        t = new Thread(() -> {
            while (true) {
                daInviare.setOnKeyPressed(ke -> {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        System.out.println("asdf");
                        invio();
                    }
                });
                try {
                    String mexDest = Client.in.readLine();
                    if (!mexDest.isEmpty()) {

                        txtArea.appendText(mexDest + "\n");
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        t.start();

    }
}
             
