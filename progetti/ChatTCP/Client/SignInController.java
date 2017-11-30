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
public class SignInController implements Initializable {

    @FXML
    private Label messaggio;
    @FXML
    private Button closeButton;
    @FXML
    public TextField username, password;

    private void println(String message) {
        Client.out.println(message);

    }

    @FXML
    public void quit() {
        println("hopremutosignin");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void signIn() throws IOException {
        System.out.println("Premuto SignIn");
        println("signIn");
        println(username.getText());
        println(password.getText());
        while (true) {
            String input = Client.in.readLine();
            if (input.equals("go")) {
                Client.root = FXMLLoader.load(getClass().getResource("Seleziona.fxml"));
                Client.scene = new Scene(Client.root);
                Client.s.setScene(Client.scene);
                Client.s.show();

                break;

            }
            if (input.equals("no")) {
                messaggio.setText("Username e/o Password invalidi");
                break;
            }
            break;
        }

    }

    public void signUp() throws IOException {
        Client.root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Client.scene = new Scene(Client.root);
        Client.s.setScene(Client.scene);
        Client.s.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
