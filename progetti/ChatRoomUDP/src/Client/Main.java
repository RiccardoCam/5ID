package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Sandro
 */
public class Main extends Application {

    @Override
    public void start(Stage Stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("grafica/InterfacciaXML.fxml"));
        Stage.setTitle("Chatroom");
        Stage.setScene(new Scene(root, 400, 540));
        Stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
