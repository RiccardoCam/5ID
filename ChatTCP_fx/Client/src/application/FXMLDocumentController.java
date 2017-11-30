/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import client.MessageInterface;
import client.UserStatusInterface;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

/**
 *
 * @author Cristian
 */
public class FXMLDocumentController implements Initializable {

    public static final String serverName = "localhost";
    public static final int serverPort = 8080;
    private String sender, msg, attivo = "";

    client.Client client = new client.Client(serverName, serverPort);

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label console;
    @FXML
    private Label selectReceiver;
    @FXML
    private Label loggedAs;

    @FXML
    private VBox containerMessaggi;

    @FXML
    private TextField inputName;
    @FXML
    private PasswordField inputPswd;
    @FXML
    private TextField message;

    @FXML
    private Button signIn;
    @FXML
    private Button signUp;
    @FXML
    private Button send;
    @FXML
    private Button logOut;

    @FXML
    private Pane pane;

    @FXML
    private ListView<String> listaDestinatari = new ListView<>();

    private void displayWindow(boolean display, boolean loggedIn) {
        pane.setVisible(display);
        signIn.setVisible(display);
        signUp.setVisible(display);
        containerMessaggi.setVisible(false);
        listaDestinatari.setVisible(false);
        loggedAs.setVisible(false);
        selectReceiver.setVisible(false);
        send.setVisible(false);
        message.setVisible(false);
        logOut.setVisible(false);
        scrollPane.setVisible(false);
        if (loggedIn) {
            logOut.setVisible(true);
            containerMessaggi.setVisible(true);
            listaDestinatari.setVisible(true);
            usersOnline();
            loggedAs.setVisible(true);
            selectReceiver.setVisible(true);
            send.setVisible(true);
            message.setVisible(true);
            scrollPane.setVisible(true);
        }
    }

    @FXML
    public void logOut() {
        try {
            client.logoff();
        } catch (IOException ex) {
            console.setText("Log out error");
        }
        displayWindow(false, true);
    }

    @FXML
    public void usersOnline() {
        new Thread(() -> {
            while (true) {
                listaDestinatari.setItems(client.getUsersOnline());
            }
        }).start();

    }

    @FXML
    public void writeChat() {
        StringTokenizer st = client.getChat();
        if (st != null) { //there are messages to write
            containerMessaggi.getChildren().clear();
            String app, username, msg;
            while (st.hasMoreTokens()) {
                username = st.nextToken();
                st.nextToken(); //consume a token
                msg = "";
                while (!(app = st.nextToken()).equals("§")) {
                    msg = msg + app + " ";
                }
                if (username.equals(client.getUsername())) { // a message sent
                    Label labelSender = new Label(username + ": " + msg);
                    labelSender.setPrefWidth(Double.MAX_VALUE);
                    labelSender.setTextAlignment(TextAlignment.RIGHT);
                    labelSender.setFont(Font.font("Calibri", FontPosture.ITALIC, 17));
                    labelSender.setPadding(new Insets(10, 0, 10, 0));
                    labelSender.setAlignment(Pos.BASELINE_RIGHT);
                    labelSender.setTextFill(Color.WHITE);
                    containerMessaggi.getChildren().add(labelSender);
                } else { // a message received
                    Label labelReceiver = new Label(" " + username + ": " + msg);
                    labelReceiver.setPrefWidth(Double.MAX_VALUE);
                    labelReceiver.setTextAlignment(TextAlignment.LEFT);
                    labelReceiver.setFont(Font.font("Calibri", FontPosture.ITALIC, 17));
                    labelReceiver.setPadding(new Insets(10, 0, 10, 0));
                    labelReceiver.setAlignment(Pos.BASELINE_LEFT);
                    labelReceiver.setTextFill(Color.RED);
                    containerMessaggi.getChildren().add(labelReceiver);
                }

            }
            scrollPane.setVvalue(1.0); //Autoscroll
        }

    }

    public void sendMessage() throws InterruptedException {
        String msg = message.getText();
        if (msg == null || msg.equals("")) {
            console.setText("Please insert a valid input");
        } else {
            String receiver = listaDestinatari.getSelectionModel().getSelectedItem();
            if (receiver != null) {
                client.sendMessage(receiver, msg);
                client.requestChat(client.getUsername(), receiver);
                Thread.sleep(200);
                writeChat();
                message.setText("");
            } else {
                console.setText("Please select a valid receiver.");
            }

        }
    }

    @FXML
    public void readMessage() {
        Label label = new Label(" " + sender + ": " + msg);
        label.setPrefWidth(Double.MAX_VALUE);
        label.setTextAlignment(TextAlignment.LEFT);
        label.setFont(Font.font("Calibri", FontPosture.ITALIC, 17));
        label.setPadding(new Insets(10, 0, 10, 0));
        label.setAlignment(Pos.BASELINE_LEFT);
        label.setTextFill(Color.RED);
        containerMessaggi.getChildren().add(label);
    }

    @FXML
    public void signInAction() {
        String username = inputName.getText();
        String password = inputPswd.getText();
        if (client.login(username, password)) {
            displayWindow(false, true);
            loggedAs.setText("Logged as: " + username);
            console.setText("Logged in succesfully!");
        } else {
            console.setText("Login error");
        }
    }

    @FXML
    public void signUpAction() {
        String username = inputName.getText();
        String password = inputPswd.getText();
        if (username.equals("") || password.length() < 4) {
            console.setText("Error sign up. Username mustn't be null and the password must contain 4 characters");
        } else {
            if (client.signUp(username, password)) {
                displayWindow(false, true);
                loggedAs.setText("Logged as: " + username);
                console.setText("Signed up succesfully!");
            } else {
                console.setText("Sign up error. Username already in use.");
            }
        }
    }

    public void getData(String sender, String msg) {
        this.sender = sender;
        this.msg = msg;
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                readMessage();
//            }
//        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaDestinatari.getItems().clear();
        displayWindow(false, false);
        if (!client.connect()) {
            console.setText("Connection failed. Server is not running");
        } else {
            console.setText("Connection OK!");
            displayWindow(true, false);
            client.addUserStatus(new UserStatusInterface() {

                @Override
                public void online(String myUsername) {
                    System.out.println("ONLINE: " + myUsername);
                }

                @Override
                public void offline(String myUsername) {
                    System.out.println("OFFLINE: " + myUsername);
                }
            });

            client.addMessage(new MessageInterface() {
                @Override
                public void message(String sender, String msg) {
                    getData(sender, msg);
                }
            });
        }
    }

}
