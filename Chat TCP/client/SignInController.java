/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientchat;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
import java.util.logging.*;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Filippo
 */
public class SignInController implements Initializable {

    @FXML
    private Button conferma;

    @FXML
    private Button signUp;

    @FXML
    private TextField utente;
    @FXML
    private PasswordField pass;
    @FXML
    private Label Erro;

    protected PrintWriter writer;
    protected String address = "localhost";
    protected final int p = 2222;
    protected Socket sock;
    protected BufferedReader reader;
    protected Stage prevStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            sock = new Socket(address, p);
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            writer = new PrintWriter(sock.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

    @FXML
    private void b_Accedi() throws IOException {
        try {
            writer.println("login:" + utente.getText() + ":" + pass.getText());
            String nmUt=utente.getText();
            writer.flush();
            String stream = "";
            stream = reader.readLine();
            System.out.println(stream);
            System.out.println("nome "+nmUt);
            if (stream.equals("Errore")) {
                Erro.setText("Credenziali inesistenti o utente già connesso");
            } else if (stream.equals("entrato")) {
                Stage stage = new Stage();
                FXMLDocumentController next = new FXMLDocumentController(writer, reader, nmUt);
                FXMLLoader myLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
                Pane myPane = (Pane) myLoader.load();
                next = (FXMLDocumentController) myLoader.getController();
                next.setPrevStage(stage);
                Scene myScene = new Scene(myPane);
                stage.setScene(myScene);
                stage.sizeToScene();
                prevStage.close();
                stage.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void b_SignUp()throws IOException {
        try {
            writer.println("registrazione:" + utente.getText() + ":" + pass.getText());
            writer.flush();
            String stream = "";
            stream = reader.readLine();
            System.out.println(stream);
            if (stream.equals("Errore")) {
                Erro.setText("nome utente già presente");
            } else if (stream.equals("registrato")) {
                utente.setText("");
                pass.setText("");
            }
        } catch (IOException ex) {
        }
    }

}
