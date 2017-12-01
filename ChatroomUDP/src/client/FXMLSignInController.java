/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    public void check() throws InterruptedException, SQLException, IOException {
        System.out.println("chiamato");
        username = user.getText();
        if (username.equals("")) {
            user.clear();
            new Thread(() -> {
                error.setVisible(true);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLSignInController.class.getName()).log(Level.SEVERE, null, ex);
                }
                error.setVisible(false);

            }).start();

        } else {

            Client.user = username;
            Main.r = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Main.scene = new Scene(Main.r);
            Main.s.setScene(Main.scene);
            Main.s.show();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

}
