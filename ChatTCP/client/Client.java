package client;

import chat.Messaggio;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Optional;

public class Client extends Application {

    //Costanti per comunicazione con il server
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8008;

    //Comunicazione
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    //Login
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty utenteCorrente = new SimpleStringProperty("");
    private final StringProperty conversazioneCorrente = new SimpleStringProperty("");
    private final ObservableList<String> connessi = FXCollections.observableArrayList();
    private final ObservableList<String> conversazioni = FXCollections.observableArrayList();

    private DatabaseConversazioni databaseConversazioni;

    public Client() {
        while (!getConnessione()) {
            System.out.println("Server non disponibile");
        }
    }

    private boolean getConnessione() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private String login() {
        Dialog<String> dialog = new Dialog<>();
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> System.exit(0));
        dialog.setTitle("Login");
        dialog.setHeaderText("Inserire utente e password");

        ButtonType bottoneRegistrazione = new ButtonType("Registrati", ButtonBar.ButtonData.NEXT_FORWARD);
        ButtonType bottoneLogin = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(bottoneLogin, bottoneRegistrazione);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(bottoneLogin);
        loginButton.setDisable(true);
        Node registrazioneButton = dialog.getDialogPane().lookupButton(bottoneRegistrazione);
        registrazioneButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(username.getText().trim().isEmpty() || password.getText().trim().isEmpty());
            registrazioneButton.setDisable(username.getText().trim().isEmpty() || password.getText().trim().isEmpty());
        });

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(username.getText().trim().isEmpty() || password.getText().trim().isEmpty());
            registrazioneButton.setDisable(username.getText().trim().isEmpty() || password.getText().trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == bottoneLogin) {
                Messaggio login = Messaggio.creaMessaggioLogin(username.getText(), password.getText());
                inviaMessaggio(login);
                Messaggio risposta = riceviMessaggio();
                if (risposta != null) {
                    if (Boolean.parseBoolean(risposta.getIntestazione())) {
                        return username.getText();
                    } else {
                        dialog.setHeaderText(risposta.getContenuto());
                    }
                }
            }
            if (dialogButton == bottoneRegistrazione) {
                Messaggio register = Messaggio.creaMessaggioRegistrazione(username.getText(), password.getText());
                inviaMessaggio(register);
                Messaggio risposta = riceviMessaggio();
                if (risposta != null) {
                    if (Boolean.parseBoolean(risposta.getIntestazione())) {
                        dialog.setHeaderText(risposta.getContenuto());
                    } else {
                        dialog.setHeaderText(risposta.getContenuto());
                    }
                }
            }
            return null;
        });
        Optional<String> d = dialog.showAndWait();

        return d.orElse(null);
    }

    private Messaggio riceviMessaggio() {
        try {
            return (Messaggio) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    private void inviaMessaggio(Messaggio messaggio) {
        try {
            output.writeObject(messaggio);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void aggiornaContatto(String username, boolean stato) {
        Platform.runLater(() -> {
            if (stato) {
                if (!connessi.contains(username)) {
                    connessi.add(username);
                }
            } else {
                if (connessi.contains(username)) {
                    connessi.remove(username);
                }
            }
        });
    }

    void riceviConversazione(String mittente, String destinatario, String messaggio) {
        Platform.runLater(() -> {
            if (mittente.equals(username.get())) {
                databaseConversazioni.aggiungiAConversazione(destinatario, messaggio, true);
                if (utenteCorrente.get().equals(destinatario)) {
                    conversazioneCorrente.set(databaseConversazioni.getConversazione(destinatario));
                }
            } else {
                databaseConversazioni.aggiungiAConversazione(mittente, messaggio, false);
                if (utenteCorrente.get().equals(mittente)) {
                    conversazioneCorrente.set(databaseConversazioni.getConversazione(mittente));
                }
            }
        });
    }

    void apriConversazione(String utente) {
        utenteCorrente.set(utente);
        databaseConversazioni.creaNuovaConversazione(utente);
        conversazioneCorrente.set(databaseConversazioni.getConversazione(utente));
    }

    void aggiungiConversazione(String utente) {
        if (!conversazioni.contains(utente)) {
            conversazioni.add(utente);
        }
    }

    ObservableList<String> getConnessi() {
        return connessi;
    }

    ObservableList<String> getConversazioni() {
        return conversazioni;
    }

    StringProperty getConversazione() {
        return conversazioneCorrente;
    }

    StringProperty getUtenteCorrente() {
        return utenteCorrente;
    }

    void inviaConversazione(String destinatario, String messaggio) {
        inviaMessaggio(Messaggio.creaMessaggioConversazione(username.get(), destinatario, messaggio));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
        final Parent root = loader.load();
        final Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            try {
                databaseConversazioni.salva();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.exit(0);
        });
        username.set(login());
        databaseConversazioni = DatabaseConversazioni.getIstanza(username.get());
        conversazioni.addAll(databaseConversazioni.getConversazioni());
        stage.setTitle("Chat " + username.get());
        ((Chat) loader.getController()).initialize(this);
        new Thread(Ricevitore.getIstanza(this, socket, input)).start();
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
