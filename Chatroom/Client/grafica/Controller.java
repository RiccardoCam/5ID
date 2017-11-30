package Client.grafica;


import Client.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Client client;

    @FXML
    private TextArea txtMessaggio;

    @FXML
    private Button bottoneInvia;

    @FXML
    private VBox containerMessaggi;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField txtUsername;

    @FXML
    public void invia(){
        final String messaggio = txtMessaggio.getText();
        if(messaggio.trim().isEmpty())return;
        Platform.runLater(() -> {
            client.invia(txtUsername.getText(),messaggio);
            Label label = new Label("\""+messaggio+"\"");
            label.setPrefWidth(Double.MAX_VALUE);
            label.setTextAlignment(TextAlignment.RIGHT);
            label.setFont(Font.font("Verdana", FontPosture.ITALIC, 17));
            label.setPadding(new Insets(10,0,10,0));
            label.setAlignment(Pos.BASELINE_RIGHT);
            label.setTextFill(Color.WHITE);
            containerMessaggi.getChildren().add(label);
            scrollPane.setVvalue(1.0); //Autoscroll
            txtMessaggio.setText("");
        });
    }

    public void ricevi(final String m){
        if(m == null){
            return;
        }
        Platform.runLater(() -> {
            Label label = new Label(m);
            label.setWrapText(true);
            label.setPrefWidth(Double.MAX_VALUE);
            label.setTextFill(Color.WHITE);
            label.setFont(Font.font("System", FontWeight.BOLD, 17));
            label.setPadding(new Insets(10,0,10,0));
            label.setStyle("-fx-line-spacing: 0.3em;");
            containerMessaggi.getChildren().add(label);
            scrollPane.setVvalue(1.0); //Autoscroll
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client = new Client( 4446);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Impossibile contattare il server");
            System.exit(1);
        }
        new Thread(() -> {
            for(;;)
                ricevi(client.ricevi());
        }).start();
        txtMessaggio.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                invia();
            }
        });
    }

}
