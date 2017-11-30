package Client;

import Client.grafica.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.IOException;
import java.util.Optional;


public class Main extends Application {

    public static Client client = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("");
        dialog.setHeaderText("Inserisci l'indirizzo del server");

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 30, 10, 10));

        TextField ipServer = new TextField();
        TextField portaServer = new TextField();


        portaServer.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                portaServer.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        grid.add(new Label("Ip server:"), 0, 0);
        grid.add(ipServer, 1, 0);
        grid.add(new Label("Porta:"), 0, 1);
        grid.add(portaServer, 1, 1);

        Node bottoneInvia = dialog.getDialogPane().lookupButton(loginButtonType);
        bottoneInvia.setDisable(true);

        ipServer.textProperty().addListener((observable, oldValue, newValue) -> {
            bottoneInvia.setDisable(newValue.trim().isEmpty() || portaServer.getText().trim().isEmpty());
        });

        portaServer.textProperty().addListener((observable, oldValue, newValue) -> {
            bottoneInvia.setDisable(newValue.trim().isEmpty() || ipServer.getText().trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(ipServer::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(ipServer.getText(), portaServer.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(esito -> {
            try {
                client = new Client(esito.getKey(),Integer.parseInt(esito.getValue()));
                client.start();
            } catch (Exception e) {
                System.err.println("Impossibile contattare il server: "+ e.getMessage());

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Impossibile contattare il server");
                alert.setContentText("Verifica i dati immessi e riprova");

                alert.showAndWait();
                return;
            }
            Parent root = null;
            LoginController.setCurrentStage(primaryStage);
            try {
                root = FXMLLoader.load(getClass().getResource("grafica/login.fxml"));
            } catch (IOException e) {e.printStackTrace();

            }
            primaryStage.setTitle("Client");
            primaryStage.setScene(new Scene(root,380, 260));
            primaryStage.show();
        });

    }

    public static void main(String ... args) {
        launch(args);
    }
}
