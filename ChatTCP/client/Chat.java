package client;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

public class Chat {

    private Client client;

    @FXML
    private ListView<String> listaConnessi, listaConversazioni;
    @FXML
    private WebView vistaConversazione;
    @FXML
    private TextField messaggio;
    @FXML
    private Label utenteCorrente;

    @FXML
    private void aggiungiConversazione() {
        String utente = listaConnessi.getSelectionModel().getSelectedItem();
        if (utente != null) {
            client.aggiungiConversazione(utente);
        }
    }

    @FXML
    private void apriConversazione() {
        String utente = listaConversazioni.getSelectionModel().getSelectedItem();
        if (utente != null) {
            client.apriConversazione(utente);
        }
    }

    @FXML
    private void inviaConversazione() {
        String destinatario = listaConversazioni.getSelectionModel().getSelectedItem();
        String conversazione = messaggio.getText().trim();
        if (destinatario != null && !conversazione.isEmpty()) {
            client.inviaConversazione(destinatario, conversazione);
            messaggio.clear();
        }
    }

    public void initialize(Client client) {
        this.client = client;
        listaConnessi.setItems(client.getConnessi());
        listaConversazioni.setItems(client.getConversazioni());

        vistaConversazione.getEngine().setUserStyleSheetLocation(getClass().getResource("style.css").toString());
        vistaConversazione.getEngine().getLoadWorker().stateProperty().addListener( (o, oldVal, newVal) -> {
            if (newVal == Worker.State.SUCCEEDED) {
                vistaConversazione.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight);");
            }
        });
        client.getConversazione().addListener((observable, oldValue, newValue) -> vistaConversazione.getEngine().loadContent(newValue));

        client.getUtenteCorrente().addListener((observable, oldValue, newValue) -> utenteCorrente.setText("Conversazione con " + newValue));
    }
}
