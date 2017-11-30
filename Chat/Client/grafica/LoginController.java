package Client.grafica;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import utility.Pacchetto;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static Client.Main.client;

public class LoginController implements Initializable{

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label lblEsito;
    @FXML
    private CheckBox restaConnesso;

    private Preferences preferences;

    private static Stage currentStage;

    public static void setCurrentStage(Stage stage){
        currentStage = stage;
        stage.setOnCloseRequest(event -> {
            client.inviaPacchetto(new Pacchetto(Pacchetto.Tipo.FINE));
            System.exit(0);
        });
    }

    @FXML
    public void accedi(){
        eseguiOperazione(Pacchetto.Tipo.LOGIN);
    }

    @FXML
    public void registrati(){
        eseguiOperazione(Pacchetto.Tipo.REGISTRAZIONE);
    }

    private void eseguiOperazione(Pacchetto.Tipo tipologiaOperazione){
        new Thread(() -> {
            Pacchetto.Parametri parametriLogin = new Pacchetto.Parametri();
            parametriLogin.put("username", txtUsername.getText());
            parametriLogin.put("password", txtPassword.getText());
            client.inviaPacchetto(new Pacchetto(tipologiaOperazione, parametriLogin));
            Platform.runLater(() -> {
                while(true) {
                    final Pacchetto risposta = client.riceviPacchetto();
                    if (risposta.getTipo() == Pacchetto.Tipo.OK) {
                        if (restaConnesso.isSelected()) {
                            preferences.put("username", txtUsername.getText());
                            preferences.put("password", txtPassword.getText());
                            preferences.putBoolean("resta_connesso", true);
                        }
                        client.setUsername(txtUsername.getText());
                        if(tipologiaOperazione == Pacchetto.Tipo.LOGIN)
                            startChat();
                        else
                            eseguiOperazione(Pacchetto.Tipo.LOGIN);
                        return;
                    } else if(risposta.getTipo() == Pacchetto.Tipo.ERRORE){
                        lblEsito.setText(risposta.getParametro("errore"));
                        return;
                    }
                }
            });
        }).start();
    }

    private void startChat(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("chat.fxml"));
            currentStage.setScene(new Scene(root,700, 450));
        } catch (IOException e) {
           System.err.println("Impossibile avviare la chat: "+e.getMessage());
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        preferences = Preferences.userNodeForPackage(getClass());
        if(preferences.getBoolean("resta_connesso",false)){
            txtUsername.setText(preferences.get("username",""));
            txtPassword.setText(preferences.get("password",""));
            eseguiOperazione(Pacchetto.Tipo.LOGIN);
        }
        txtPassword.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                accedi();
            }
        });
    }
}
