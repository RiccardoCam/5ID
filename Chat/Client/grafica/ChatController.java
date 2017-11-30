package Client.grafica;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import utility.Pacchetto;

import java.net.URL;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static Client.Main.client;

public class ChatController implements Initializable {

    private String destinatarioAttivo;

    private enum StatoMessaggio{
        INVIATO,
        RICEVUTO
    }

    @FXML
    private Label lblUser;
    @FXML
    private Label lblDestinatario;
    @FXML
    private VBox containerContatti;
    @FXML
    private AnchorPane containerChat;
    @FXML
    private TextArea txtMessaggio;
    @FXML
    private VBox containerMessaggi;

    private Map<String,List<Pair<String,StatoMessaggio>>> conversazioni;

    @FXML
    private void inviaMessaggio(){
        String testoMessaggio = txtMessaggio.getText().trim();
        if(testoMessaggio.isEmpty())return;
        Pacchetto.Parametri parametriMessaggio = new Pacchetto.Parametri();
        parametriMessaggio.put("mittente",client.getUsername());
        parametriMessaggio.put("destinatario",destinatarioAttivo);
        parametriMessaggio.put("testo",testoMessaggio);
        aggiungiLabel(testoMessaggio,StatoMessaggio.INVIATO);
        txtMessaggio.setText("");
        conversazioni.get(destinatarioAttivo).add(new Pair<>(testoMessaggio,StatoMessaggio.INVIATO));
        client.inviaPacchetto(new Pacchetto(Pacchetto.Tipo.MESSAGGIO,parametriMessaggio));
    }

    private void riceviMessaggio(String messaggio, String mittente){
        conversazioni.get(mittente).add(new Pair<>(messaggio,StatoMessaggio.RICEVUTO));
        if(mittente.equals(destinatarioAttivo))
            aggiungiLabel(messaggio,StatoMessaggio.RICEVUTO);
    }

    private void aggiungiLabel(String testoMessaggio,StatoMessaggio statoMessaggio){
        Platform.runLater(()->{
            Label label = new Label(testoMessaggio);
            label.setPrefWidth(Double.MAX_VALUE);
            label.setTextAlignment((statoMessaggio == StatoMessaggio.INVIATO)?TextAlignment.RIGHT:TextAlignment.LEFT);
            label.setFont(Font.font("Verdana", FontPosture.ITALIC, 17));
            label.setPadding(new Insets(10,0,10,0));
            if(statoMessaggio == StatoMessaggio.INVIATO)
                label.setAlignment(Pos.BASELINE_RIGHT);
            label.setTextFill(Color.WHITE);
            containerMessaggi.getChildren().add(label);
        });
    }

    @FXML
    private void logout(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Vuoi eseguire il logout?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            try {
                Preferences preferences = Preferences.userNodeForPackage(getClass());
                preferences.clear();
                client.inviaPacchetto(new Pacchetto(Pacchetto.Tipo.FINE));
                System.exit(0);
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDestinatarioAttivo(String destinatarioAttivo){
        this.destinatarioAttivo = destinatarioAttivo;
        if(destinatarioAttivo == null){
            containerChat.setVisible(false);
            return;
        }
        lblDestinatario.setText(destinatarioAttivo);
        //Ricarico i messaggi precedenti
        containerMessaggi.getChildren().clear();
        for(Pair<String,StatoMessaggio> x: conversazioni.get(destinatarioAttivo)){
            aggiungiLabel(x.getKey(),x.getValue());
        }
        containerChat.setVisible(true);
    }


    private void aggiornaListaContatti(String listaContatti){
        try {
            JSONArray contatti = new JSONArray(listaContatti);
            Platform.runLater(()->{
                containerContatti.getChildren().clear();
                boolean fill = false;
                boolean destinatarioPresente = (destinatarioAttivo == null);
                for(int i=0;i<contatti.length();i++){
                    try {
                        if(!contatti.getString(i).equals(client.getUsername())) {
                            final String nomeContatto = contatti.getString(i);
                            destinatarioPresente = destinatarioPresente || nomeContatto.equals(destinatarioAttivo);
                            if(!conversazioni.containsKey(nomeContatto))
                                conversazioni.put(nomeContatto,new ArrayList<>());
                            Label contatto = new Label(contatti.getString(i));
                            contatto.setPrefWidth(200);
                            contatto.setPadding(new Insets(15,10,15,10));
                            if(fill)
                                contatto.setStyle("-fx-background-color: #e6e6e6;-fx-font-weight: bold");
                            containerContatti.getChildren().add(contatto);
                            contatto.setOnMouseClicked(event -> setDestinatarioAttivo(nomeContatto));
                            fill = !fill;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(!destinatarioPresente){
                    setDestinatarioAttivo(null);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUser.setText("Username: " + client.getUsername());
        conversazioni = new HashMap<>();
        new Thread(()->{
            for(;;) {
                Pacchetto pacchettoRicevuto = client.riceviPacchetto();
                switch (pacchettoRicevuto.getTipo()){
                    case DISCONNESSIONE:
                        client.inviaPacchetto(new Pacchetto(Pacchetto.Tipo.DISCONNESSIONE));
                    case FINE:
                        System.exit(0);
                        return;
                    case MESSAGGIO:
                        Pacchetto.Parametri parametri = pacchettoRicevuto.getParametri();
                        riceviMessaggio(parametri.get("testo"),parametri.get("mittente"));
                        break;
                    case LISTA_CONTATTI:
                        aggiornaListaContatti(pacchettoRicevuto.getParametro("lista"));
                        break;
                }
            }
        }).start();
        txtMessaggio.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                inviaMessaggio();
            }
        });
    }

}
