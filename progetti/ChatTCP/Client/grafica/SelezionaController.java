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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author Sandro
 *
 */
public class SelezionaController implements Initializable {

    @FXML
    private Label nickname;
    @FXML
    public ComboBox disponibili;
    @FXML
    private Button closeButton;

    @FXML
    public void quit() throws InterruptedException, IOException {
        this.invia("Elimina");

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    private void invia(String message) {
        Client.client.invia(message);
    }

    private String leggi() {
        return Client.client.leggi();
    }

    public void seleziona() throws IOException {
        this.invia("vogliomessaggiare");
        System.out.println("Premuto Conferma");
        String mioNickname = this.nickname.getText();
        String destNickname = this.disponibili.getValue().toString();
        //--
        System.out.println("mio nick: " + mioNickname);
        System.out.println("nick da contattare: " + destNickname);
        //--
        this.invia(mioNickname);
        this.invia(destNickname);
        Client.root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Client.scene = new Scene(Client.root);
        Client.s.setScene(Client.scene);
        Client.s.show();
    }

    public void aggiorna() throws IOException {
        this.invia("Aggiorna");
        System.out.println("Premuto Aggiorna");
        String input = this.leggi();
        if (input.equals("go")) {
            while (true) {
                if (!this.leggi().equals("finito")) {
                    String insert = this.leggi();
                    if (!insert.isEmpty()) {
                        System.out.println("il nome che ti sta passando è: " + insert);
                        ObservableList a = disponibili.getItems();
                        if (!a.contains(insert)) {
                            disponibili.getItems().add(insert);
                        }

                    }

                } else {
                    break;
                }

            }

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disponibili.setVisibleRowCount(5);
        nickname.setText(this.leggi());

    }

}
