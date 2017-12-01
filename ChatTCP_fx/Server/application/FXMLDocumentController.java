/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;
import server.SocketThread;

/**
 *
 * @author Cristian
 */
public class FXMLDocumentController implements Initializable {

    public final int portNumber = 8080;
    private boolean serverRunning = false;
    private server.Server server = null;
    private SocketThread socket;

    @FXML
    private Button serverStartButton;

    @FXML
    private VBox containerMessaggi;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    public void startServer() {
        if (serverRunning) {
            server.close();
            serverRunning = false;
            serverStartButton.setText("Start server");
            writeMessage("Server stopped.");
        } else {
            server = new server.Server(portNumber);
            serverRunning = true;
            serverStartButton.setText("Server runnning...");
            writeMessage("The server is started.");
        new Thread(() -> {
            while (true) {
                if (serverRunning) {
                    writeMessage(server.getData());
                }
            }
        }).start();
        }
    }

    @FXML
    public void writeMessage(String message) {
        Platform.runLater(() -> {
            Label label = new Label("Server# " +message);
            label.setPrefWidth(Double.MAX_VALUE);
            label.setTextAlignment(TextAlignment.LEFT);
            label.setFont(Font.font("Courier New", FontPosture.ITALIC, 17));
            label.setPadding(new Insets(10, 0, 10, 0));
            label.setAlignment(Pos.TOP_LEFT);
            label.setTextFill(Color.GREEN);
            containerMessaggi.getChildren().add(label);
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }

}
