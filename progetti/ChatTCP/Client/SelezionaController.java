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
        println("Elimina");

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    private void println(String message) {
        Client.out.println(message);

    }

    public void seleziona() throws IOException {
        println("vogliomessaggiare");
        System.out.println("Premuto Conferma");
        String mioNickname = this.nickname.getText();
        String destNickname = this.disponibili.getValue().toString();
        //--
        System.out.println("mio nick: " + mioNickname);
        System.out.println("nick da contattare: " + destNickname);
        //--
        println(mioNickname);
        println(destNickname);
        Client.root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Client.scene = new Scene(Client.root);
        Client.s.setScene(Client.scene);
        Client.s.show();
    }

    public void aggiorna() throws IOException {
        println("Aggiorna");
        System.out.println("Premuto Aggiorna");
        String input = Client.in.readLine();
        if (input.equals("go")) {
            while (true) {
                if (!Client.in.readLine().equals("finito")) {
                    String insert = Client.in.readLine();
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
        try {
            nickname.setText(Client.in.readLine());
        } catch (IOException ex) {
            Logger.getLogger(SelezionaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
