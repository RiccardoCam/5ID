package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Client extends Application {

    private static final int HEADER_LENGHT = 1;
    private static final byte MESSAGE = 0;
    private static final int PORT = 8008;

    private MulticastSocket socket;
    private InetAddress address;
    private String username;
    private StringProperty chat;

    private void enter() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> System.exit(0));
        dialog.setTitle("Chatroom");
        dialog.setHeaderText("Inserire username e IP multicast");
        ButtonType entra = new ButtonType("Entra", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(entra);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 0, 20));

        TextField user = new TextField();
        TextField ip = new TextField("224.0.0.2");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(user, 1, 0);
        grid.add(new Label("IP Multicast:"), 0, 1);
        grid.add(ip, 1, 1);
        dialog.getDialogPane().setContent(grid);

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(entra);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            String u = user.getText().trim();
            String i = ip.getText().trim();
            if (!u.isEmpty() && !i.isEmpty()) {
                try {
                    socket = new MulticastSocket(PORT);
                    address = InetAddress.getByName(i);
                    socket.joinGroup(address);
                    if (u.length() <= 15) {
                        username = u;
                    } else {
                        dialog.setHeaderText("Username supera 15 caratteri");
                        ae.consume();
                    }
                    chat = new SimpleStringProperty("");
                    new Thread(new Receiver(this, socket)).start();
                    return;
                } catch (UnknownHostException e) {
                    dialog.setHeaderText("Indirizzo ip sconosciuto");
                } catch (IOException e) {
                    dialog.setHeaderText("L'indirizzo ip inserito non è multicast");
                }
                ae.consume();
            }
            ae.consume();
        });

        dialog.showAndWait();

        sendPacket(MESSAGE, username + " si è connesso.");
    }

    private void sendPacket(byte type, String content) {
        byte[] bytes = new byte[content.length() + 1];
        bytes[0] = type;
        System.arraycopy(content.getBytes(), 0, bytes, 1, bytes.length - 1);
        try {
            socket.send(new DatagramPacket(bytes, bytes.length, address, PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            message = "(" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + username + ") > " + message.trim();
            sendPacket(MESSAGE, message);
        }
    }

    private void receiveMessage(String message) {
        Platform.runLater(() -> chat.set(chat.get() + message + System.lineSeparator()));
    }

    private void exit() {
        sendPacket(MESSAGE, username + " si è disconesso.");
        socket.close();
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        enter();

        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(10);

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.textProperty().bind(chat);
        textArea.setWrapText(true);
        textArea.textProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> textArea.setScrollTop(Double.MAX_VALUE)));

        TextField textField = new TextField();

        Button button = new Button("Invia");
        button.setOnAction(event -> {
            sendMessage(textField.getText());
            textField.clear();
        });

        AnchorPane anchorPane = new AnchorPane(textField, button);
        AnchorPane.setLeftAnchor(textField, 0d);
        AnchorPane.setRightAnchor(textField, 0d);
        AnchorPane.setRightAnchor(button, 0d);

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        root.add(textArea, 0, 0);
        root.add(anchorPane, 0, 1);

        primaryStage.setTitle("Chatroom " + username);
        primaryStage.setOnCloseRequest(event -> exit());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class Receiver implements Runnable {

        private static final int BUFFER_LENGHT = 1024;

        private final MulticastSocket socket;
        private final Client client;

        Receiver(Client client, MulticastSocket socket) {
            this.socket = socket;
            this.client = client;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    byte[] buffer = new byte[BUFFER_LENGHT];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    int header = buffer[0];
                    if (header == MESSAGE) {
                        client.receiveMessage(new String(buffer, HEADER_LENGHT, packet.getLength()));
                    }
                }
            } catch (IOException ex) {
                System.out.println("Client chiuso");
            }
        }
    }
}
