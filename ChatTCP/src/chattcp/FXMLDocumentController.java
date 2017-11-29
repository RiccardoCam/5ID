/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattcp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.RED;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Alvise
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label utenteConnesso;
    @FXML
    private Label destinatario;
    @FXML
    private ComboBox<String> cbox = new ComboBox<>();
    @FXML
    private TextField messaggio;
    @FXML
    private VBox containerMessaggi = new VBox();
    @FXML
    private ScrollPane scrollPane = new ScrollPane();

    private ArrayList<String> users, arrayMess;
    private ArrayList<String> arrayBooleanMessaggi;
    private int numeroUtenti = 0;
    private String[] app;
    public String userDest;
    public String ris, mex, messDaAgg;

    public FXMLDocumentController() throws IOException {
        users = new ArrayList<>();
        arrayMess = new ArrayList<>();
        arrayBooleanMessaggi = new ArrayList<>();
        numeroUtenti = Integer.parseInt(Client.in.readLine());
        for (int i = 0; i < numeroUtenti; i++) {
            ris = Client.in.readLine();
            if (!ris.equals(Client.user)) {
                users.add(ris);
            }
        }
        cbox.getItems().addAll(users);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        utenteConnesso.setText(Client.user);
    }

    public void getUtentiConnessi() throws IOException {
        destinatario.setTextFill(BLACK);
        Client.out.println("RICHIESTANUTENTI");
        Client.out.println(Client.user);
        System.out.println("mando richiesta numero utenti");
        numeroUtenti = Integer.parseInt(Client.in.readLine());
        System.out.println(numeroUtenti + "numeroUtenti");
        users.clear();
        for (int i = 0; i < numeroUtenti; i++) {
            ris = Client.in.readLine();
            if (!ris.equals(Client.user)) {
                users.add(ris);
            }

        }
        System.out.println(Arrays.toString(users.toArray()));
        cbox.getItems().clear();
        cbox.getItems().addAll(users);
        Client.out.flush();

    }

    public void aggiorna() throws IOException {
        if (userDest != null) {
            visualizzaChat();
        } else {
            System.out.println("Seleziona prima un destinatario");
        }
    }

    public void setUserDest() throws IOException {

        userDest = cbox.getValue();
        System.out.println("USER DEST: " + userDest);

        if (userDest != null) {
            destinatario.setTextFill(BLACK);
            visualizzaChat();
        } else {
            destinatario.setTextFill(RED);
        }

    }

    public void inviaMessaggio() throws IOException {
        System.out.println("entro inviamessaggio");
        mex = messaggio.getText();
        System.out.println(userDest + " userdest");
        if (mex.equals("") || mex.equals(" ")) {
            messaggio.clear();
            System.out.println("Inserisci un messaggio valido");
            return;
        }
        if (userDest == null) {
            destinatario.setTextFill(RED);
            messaggio.clear();
            System.out.println("Seleziona un destinatario");
            return;
        } else {
            Client.out.println("INVIAMESSAGGIO");
            Client.out.println(mex);
            Client.out.println(Client.user);
            Client.out.println(userDest);
            Client.out.flush();
            messaggio.clear();
            System.out.println(mex + " messaggio inviato");
            visualizzaChat();
        }

    }

    public void disconnetti() throws IOException, Exception {
        Platform.exit();
    }

    public void visualizzaChat() throws IOException {

        System.out.println("ENTRO IN VISUALIZZA CHAT");
        arrayMess.clear();
        arrayBooleanMessaggi.clear();
        Client.out.println("VISUALIZZACHAT");
        Client.out.println(Client.user);
        Client.out.println(userDest);

        ris = Client.in.readLine();

        System.out.println("ris " + ris);
        int sizeMessaggi = Integer.parseInt(ris);
        System.out.println(sizeMessaggi + " size");

        if (sizeMessaggi == 0) { //0 messaggi nella chat
            containerMessaggi.setSpacing(5);
            scrollPane.setContent(containerMessaggi);
            containerMessaggi.getChildren().clear();
            return;
        }
        for (int i = 0; i < sizeMessaggi; i++) {
            arrayMess.add(Client.in.readLine());
            System.out.println("arrayMess " + arrayMess.get(i));
            arrayBooleanMessaggi.add(Client.in.readLine());
            System.out.println("arrayBooleanMessaggi " + arrayBooleanMessaggi.get(i));

        }
        System.out.println(Arrays.toString(arrayMess.toArray()));
        System.out.println(Arrays.toString(arrayBooleanMessaggi.toArray()));

        containerMessaggi.getChildren().clear();

        containerMessaggi.setSpacing(5);
        scrollPane.setContent(containerMessaggi);
        scrollPane.setVvalue(1.0);

        for (int i = 0; i < arrayMess.size(); i++) {
            if (arrayBooleanMessaggi.get(i).equals("true")) {
                containerMessaggi.getChildren().add(addMessage(arrayMess.get(i), true));

            } else {
                containerMessaggi.getChildren().add(addMessage(arrayMess.get(i), false));
            }

        }
        System.out.println("FINITO VISUALIZZACHAT");
        Client.out.flush();

    }

    public HBox addMessage(String message, boolean mittente) {
        HBox hbox = new HBox();
        hbox.setPrefWidth(400);
        Label label;
        System.out.println("MESSAGGIO: " + message);
        if (mittente) {
            label = new Label(message);
            hbox.setAlignment(Pos.BASELINE_RIGHT);
            label.setTextAlignment(TextAlignment.LEFT);
        } else {
            label = new Label("   " + message);
            hbox.setAlignment(Pos.BASELINE_LEFT);
            label.setTextAlignment(TextAlignment.LEFT);
        }
        label.setWrapText(true);
        label.setMaxWidth(200);
        label.setFont(Font.font("Verdana", 20));
        label.setPadding(new Insets(10, 0, 10, 0));

        hbox.getChildren().add(label);
        return hbox;
    }
}
