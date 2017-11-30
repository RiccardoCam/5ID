/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattcp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Sandro
 *
 */
public class SignUpController implements Initializable {

    @FXML
    private Label messaggio;
    @FXML
    private Button closeButton;
    @FXML
    private TextField username, password1, password2;

    private void println(String message) {
        Client.out.println(message);

    }

    @FXML
    public void quit() {

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    public void signIn() throws IOException {
        Client.root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        Client.scene = new Scene(Client.root);
        Client.s.setScene(Client.scene);
        Client.s.show();
    }

    public void signUp() throws IOException {
        if (!password1.getText().isEmpty() && !password2.getText().isEmpty() && !username.getText().isEmpty()) {
            Client.out.println("confermaregistrazione");
            Client.out.println(username.getText());
            while (true) {
                String input = Client.in.readLine();
                if (input.equals("go")) {
                    if (password1.getText().equals(password2.getText())) {
                        Client.out.println("invioregistrazione");
                        Client.out.println(username.getText());
                        Client.out.println(password1.getText());
                        Client.root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
                        Client.scene = new Scene(Client.root);
                        Client.s.setScene(Client.scene);
                        Client.s.show();
                        break;
                    } else {
                        messaggio.setText("i password non corrispondono");
                        break;
                    }
                }
                if (input.equals("no")) {
                    messaggio.setText("Questo username è già stato utilizzato");
                    break;
                }
                break;
            }

        } else {
            messaggio.setText("Devi completare tutti i campi");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
