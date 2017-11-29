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
public class FXMLSignUpController implements Initializable {

    protected String username, password, passwordConfirm;
//	private PrintWriter out = Client.client.getOut();
//	private BufferedReader in = Client.client.getIn();
    private static final Color[] colors = {RED, GREEN, YELLOW, BLUE};
    //error.setTextFill(colors[r.nextInt(colors.length-1)]);
    private Random r = new Random();

    @FXML
    private Label error;
    @FXML
    private TextField user;
    @FXML
    private TextField pswd, pswdConfirm;

    public boolean check() throws InterruptedException, SQLException, IOException {
        username = user.getText();
        password = pswd.getText();
        passwordConfirm = pswdConfirm.getText();

        if ((username.equals("") && (password.equals("")) && (passwordConfirm.equals("")))) {
            user.clear();
            pswd.clear();
            pswdConfirm.clear();
            error.setText("Inserire i dati");
            error.setVisible(true);
            return false;

        } else {
            if (username.equals("")) {
                user.clear();
                pswd.clear();
                pswdConfirm.clear();
                error.setText("Username mancante");
                error.setVisible(true);
                return false;
            }
            if (password.equals("") || passwordConfirm.equals("")) {
                user.clear();
                pswd.clear();
                pswdConfirm.clear();
                error.setText("Password mancante");
                error.setVisible(true);
                return false;
            }
            if (!password.equals(passwordConfirm)) {
                user.clear();
                pswd.clear();
                pswdConfirm.clear();
                error.setText("Password diverse");
                error.setVisible(true);
                return false;
            }
        }

        Client.out.println("SIGNUP");
        Client.out.println(username);
        Client.out.println(password);
        Client.out.flush();

        if (Client.in.readLine().equals("CHAT")) {
            Client.user = username;
            Client.r = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Client.scene = new Scene(Client.r);
            Client.s.setScene(Client.scene);
            Client.s.show();
            return true;
        } else {
            user.clear();
            pswd.clear();
            pswdConfirm.clear();
            error.setVisible(true);
        }

        return false;

    }

    public void signIn() throws IOException, Exception {
        Client.r = FXMLLoader.load(getClass().getResource("FXMLSignIn.fxml"));
        Client.scene = new Scene(Client.r);
        Client.s.setScene(Client.scene);
        Client.s.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
