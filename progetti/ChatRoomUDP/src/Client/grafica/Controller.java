package Client.grafica;

import Client.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Sandro
 */
public class Controller implements Initializable {

    private Client client;

    @FXML
    private TextArea txtMex;

    @FXML
    private VBox Vbox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField nickname;

    @FXML
    public void inviaMex() {
        String messaggio = txtMex.getText();
        //ricevi messaggio e poi
        //.trim rimuove spazi bianchi
        if (messaggio.trim().isEmpty()) {
            return;
        }
        Platform.runLater(() -> {
            client.inviaMex(nickname.getText(), messaggio);
            scrollPane.setVvalue(1.0); 
            txtMex.setText("");
        });
    }

    public void riceviMex(String mex) {
        if (mex == null) {
            return;
        }
        Platform.runLater(() -> {
            Label label = new Label(mex);
            label.setWrapText(true);
            label.setPrefWidth(Double.MAX_VALUE);
            label.setTextFill(Color.WHITE);
            label.setFont(Font.font(null, FontWeight.BOLD, 15));
            label.setPadding(new Insets(10, 0, 10, 0));
            Vbox.getChildren().add(label);
            scrollPane.setVvalue(1.0); 
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client = new Client(4446);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Server non disponibile?");
            System.exit(1);
        }
        new Thread(() -> {
            while (true) {
                riceviMex(client.riceviMex());
            }
        }).start();
        txtMex.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                inviaMex();
            }
        });
    }

}
