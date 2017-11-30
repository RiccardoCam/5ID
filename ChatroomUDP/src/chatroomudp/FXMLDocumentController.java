/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomudp;

import static chatroomudp.Client.dp;
import static chatroomudp.Client.sendData;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private TextField messaggio;
    @FXML
    private VBox containerMessaggi = new VBox();
    @FXML
    private ScrollPane scrollPane = new ScrollPane();

    private ArrayList<String> users, arrayMess;
    private ArrayList<String> arrayBooleanMessaggi;
    private int numeroUtenti = 0;
    private String[] app;
    public String ris, mex, messDaAgg;

    public FXMLDocumentController() throws IOException {
        Client.socket.joinGroup(Client.address);
        users = new ArrayList<>();
        arrayMess = new ArrayList<>();
        arrayBooleanMessaggi = new ArrayList<>();
        ris = riceviMessaggio();
        String[] app = ris.split(";");

        for (int i = 1; i < app.length; i++) {
            if (!app[i].equals(Client.user)) {
                users.add(app[i]);
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        utenteConnesso.setText(Client.user);
    }

    public void inviaMessaggio() throws IOException {
        System.out.println("entro inviamessaggio");
        mex = messaggio.getText();
        if (mex.equals("") || mex.equals(" ")) {
            messaggio.clear();
            System.out.println("Inserisci un messaggio valido");
            return;
        } else {
            String s = "INVIAMESSAGGIO" + mex + ";" + Client.user;
            inviaMessaggio(s);

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
        String s = "VISUALIZZACHAT" + Client.user;
        inviaMessaggio(s);

        ris = riceviMessaggio();

        System.out.println("ris " + ris);
        String[] app = ris.split(";");

        int sizeMessaggi = Integer.parseInt(app[0]);
        System.out.println(sizeMessaggi + " size");

        if (sizeMessaggi == 0) { //0 messaggi nella chat
            containerMessaggi.setSpacing(5);
            scrollPane.setContent(containerMessaggi);
            containerMessaggi.getChildren().clear();
            return;
        }
        for (int i = 1; i < sizeMessaggi; i++) {
            if (i % 2 != 0) {
                arrayMess.add(app[i]);
                System.out.println("arrayMess " + arrayMess.get(i));
            } else {
                arrayBooleanMessaggi.add(app[i]);
                System.out.println("arrayBooleanMessaggi " + arrayBooleanMessaggi.get(i));
            }

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

    public String riceviMessaggio() throws IOException {
        Arrays.fill(Client.receiveData, (byte) 0); // try to remove
        dp = new DatagramPacket(Client.receiveData, Client.receiveData.length);
        Client.socket.receive(dp);
        return new String(dp.getData());
    }

    public void inviaMessaggio(String messaggioDaInviare) throws IOException {
        Arrays.fill(Client.sendData, (byte) 0); // try to remove
        Client.sendData = messaggioDaInviare.getBytes();
        dp = new DatagramPacket(Client.sendData, Client.sendData.length, Client.address, 9898);
        Client.socket.send(dp);
    }
}
