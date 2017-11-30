/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientchat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Filippo
 */
public class ClientChat extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
        Pane myPane = (Pane) myLoader.load();
        SignInController controller1;
        controller1 = (SignInController) myLoader.getController();
        controller1.setPrevStage(stage);
        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
