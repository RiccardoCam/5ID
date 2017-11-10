import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Client extends Application {

    //Costanti per la connessione
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8008;
    //Connessione al server
    private Socket connessione;
    private PrintWriter serverOutput;
    private BufferedReader serverInput;
    //Variabili di JavaFX
    private final ObservableList<String> listaProdottiDisponibili = FXCollections.observableArrayList();
    private final ObservableList<String> listaProdottiAcquistati = FXCollections.observableArrayList();
    private final SimpleStringProperty etichettaConto = new SimpleStringProperty("0,00€");

    public Client() throws IOException {
        while (!getConnessione()) {
            System.out.println("Server non disponibile");
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        //Prodotti disponibili
        ListView<String> prodottiDisponibili = new ListView();
        prodottiDisponibili.setItems(listaProdottiDisponibili);
        //Bottone aggiungi
        Button aggiungi = new Button(">>");
        aggiungi.setOnAction((ActionEvent event) -> {
            if (!prodottiDisponibili.getSelectionModel().getSelectedItems().isEmpty()) {
                listaProdottiAcquistati.addAll(prodottiDisponibili.getSelectionModel().getSelectedItems());
                try {
                    richiediConto();
                } catch (IOException ex) {
                    System.out.println("Server disconesso");
                }
            }
        });
        //Prodotti acquistati
        ListView<String> prodottiAcquistati = new ListView();
        prodottiAcquistati.setItems(listaProdottiAcquistati);
        //Conto
        Label conto = new Label();
        conto.textProperty().bind(etichettaConto);
        //Sconto
        Button sconto = new Button("Applica Sconto");
        sconto.setOnAction((ActionEvent event) -> {
            try {
                richiediSconto();
            } catch (IOException ex) {
            }
        });
        //Paga
        Button paga = new Button("Paga");
        paga.setOnAction((ActionEvent event) -> {
            try {
                richiediPagamento();
            } catch (IOException ex) {
            }
        });

        GridPane root = new GridPane();
        GridPane.setHalignment(aggiungi, HPos.CENTER);
        GridPane.setHalignment(conto, HPos.CENTER);
        GridPane.setHalignment(sconto, HPos.CENTER);
        GridPane.setHalignment(paga, HPos.CENTER);
        root.getColumnConstraints().addAll(new ColumnConstraints(125), new ColumnConstraints(100), new ColumnConstraints(125));
        root.add(prodottiDisponibili, 0, 0);
        root.add(aggiungi, 1, 0);
        root.add(prodottiAcquistati, 2, 0);
        root.add(sconto, 0, 1);
        root.add(conto, 1, 1);
        root.add(paga, 2, 1);

        Scene scene = new Scene(root, 350, 250);

        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        richiediLogin();
        richiediProdotti();
    }

    private boolean getConnessione() {
        try {
            connessione = new Socket(SERVER_ADDRESS, SERVER_PORT);
            serverOutput = new PrintWriter(connessione.getOutputStream(), true);
            serverInput = new BufferedReader(new InputStreamReader(connessione.getInputStream()));
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private String leggiMessaggio() throws IOException {
        return serverInput.readLine();
    }

    private void inviaMessaggio(String messaggio) {
        serverOutput.println(messaggio);
    }

    private void riceviProdotto(String prodotto) throws IOException {
        listaProdottiDisponibili.add(prodotto);
    }

    private void riceviConto(String conto) {
        etichettaConto.set(conto);
    }

    private void richiediLogin() {
        inviaMessaggio("#SET Utente");
        TextInputDialog login = new TextInputDialog();
        login.setTitle("Inserire Username");
        login.setHeaderText(null);
        login.setContentText("Username:");
        Optional<String> result = login.showAndWait();
        if (result.isPresent() && !result.get().isEmpty()) {
            inviaMessaggio(result.get());
        } else {
            inviaMessaggio("Guest");
        }

    }

    private void richiediProdotti() throws IOException {
        inviaMessaggio("#GET Prodotti");
        String serverLine;
        while (!(serverLine = leggiMessaggio()).equals("#END")) {
            riceviProdotto(serverLine);
        }
    }

    private void richiediConto() throws IOException {
        inviaMessaggio("#SET Prodotti");
        for (String prodotto : listaProdottiAcquistati) {
            inviaMessaggio(prodotto);
        }
        inviaMessaggio("#END");
        inviaMessaggio("#GET Conto");
        String conto = leggiMessaggio();
        riceviConto(conto);

    }

    private void richiediSconto() throws IOException {
        inviaMessaggio("#GET Sconto");
        String conto = leggiMessaggio();
        riceviConto(conto);
    }

    private void richiediPagamento() throws IOException {
        if (!listaProdottiAcquistati.isEmpty()) {
            inviaMessaggio("#GET Pagamento");
            String risposta = leggiMessaggio();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Pagamento eseguito!");
            alert.setHeaderText(null);
            alert.setContentText(risposta);
            alert.showAndWait();
            Platform.exit();
        }
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}
