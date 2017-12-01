/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Alvise
 */
public class Main extends Application {

    public static Parent r;
    public static Scene scene;
    public static Stage s;
    
    @Override
    public void start(Stage stage) throws Exception {
        s = stage;
        r = FXMLLoader.load(getClass().getResource("FXMLSignIn.fxml"));
        Scene scene = new Scene(r);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}