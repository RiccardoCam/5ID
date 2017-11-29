/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattcp;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.YELLOW;

/**
 *
 * @author Alvise
 */
public class FXMLSignInController implements Initializable {

    protected String username, password;
    private Random r = new Random();

    @FXML
    private Label error, app;
    @FXML
    private TextField user;
    @FXML
    private TextField pswd;

    @FXML
    public boolean check() throws InterruptedException, SQLException, IOException {
        System.out.println("chiamato");
        username = user.getText();
        password = pswd.getText();
        if (username.equals("") && password.equals("")) {
            user.clear();
            pswd.clear();
            error.setText("Inserire i dati");
            error.setVisible(true);
            return false;
        } else {
            if (username.equals("")) {
                user.clear();
                pswd.clear();
                error.setText("Username mancante");
                error.setVisible(true);
                return false;
            }
            if (password.equals("")) {
                user.clear();
                pswd.clear();
                error.setText("Password mancante");
                error.setVisible(true);
                return false;
            }
        }
        Client.out.println("SIGNIN");
        Client.out.println(username);
        Client.out.println(password);
        Client.out.flush();

        System.out.println("mando " + username);

        if (Client.in.readLine().equals("CHAT")) {
            Client.user=username;
            System.out.println(username + " username");
            Client.r = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Client.scene = new Scene(Client.r);
            Client.s.setScene(Client.scene);
            Client.s.show();
           

            return true;

        } else {
            user.clear();
            pswd.clear();
            error.setVisible(true);
        }
        return false;

    }

    public void signUp() throws IOException, Exception {
        Client.r = FXMLLoader.load(getClass().getResource("FXMLSignUp.fxml"));
        Client.scene = new Scene(Client.r);
        Client.s.setScene(Client.scene);
        Client.s.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

}
