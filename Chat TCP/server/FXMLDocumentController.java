/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.TextArea;

/**
 *
 * @author Filippo
 */
public class FXMLDocumentController implements Initializable {

    protected ArrayList clientOutput;
    protected ArrayList<String> us;

    @FXML
    private TextArea area;

    @FXML
    private void startButtonAction(ActionEvent event) {
        Thread starter = new Thread(new ServerStart());
        starter.start();
        area.appendText("Server avviato!!\n");

    }

    @FXML
    private void stopButtonAction(ActionEvent event) {
        Thread.currentThread().interrupt();
        Platform.exit();
        area.appendText("Server stop... \n");
        area.setText("");
    }

    @FXML
    private void utentiButtonAction(ActionEvent event) {
        area.appendText("\n Utenti connessi : \n");
        for (String utenteAtt : us) {
            area.appendText(utenteAtt);
            area.appendText("\n");
        }
    }

    @FXML
    private void pulisciButtonAction(ActionEvent event) {
        area.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public class ServerStart implements Runnable {

        @Override
        public void run() {
            clientOutput = new ArrayList();
            us = new ArrayList();
            try {
                ServerSocket socketServ = new ServerSocket(2222);
                while (true) {
                    Socket soClient = socketServ.accept();
                    PrintWriter wr = new PrintWriter(soClient.getOutputStream());
                    clientOutput.add(wr);
                    Thread listener = new Thread(new GestioneCliente(soClient));
                    listener.start();
                    area.appendText("Got a connection. \n");
                }
            } catch (Exception ex) {
                area.appendText("error \n");
            }
        }

    }

    public class GestioneCliente implements Runnable {

        BufferedReader rd;
        Socket soc;
        PrintWriter writer;

        public GestioneCliente(Socket clientSocket) {
            try {
                soc = clientSocket;
                rd= new BufferedReader(new InputStreamReader(soc.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(),true);
            } catch (Exception ex) {
                area.appendText(" error\n");
            }

        }

        @Override
        public void run() {
            String message;
            String[] dat;
            try {
                while ((message = rd.readLine()) != null) {
                    area.appendText("RICEVUTO: " + message + "\n");
                    dat = message.split(":");
                    if(dat[0].equals("login")){
                        if (SQLHelper.esistonoCredenziali(dat[1], dat[2]) && !isUtenteConnesso(dat[1])) {
                            us.add(dat[1]);
                            writer.println("entrato");
                        } else {
                            writer.println("Errore");
                        }
                    }else if(dat[0].equals("registrazione")){
                        if (!SQLHelper.esisteUsername(dat[1])) {
                            SQLHelper.inserisciUtente(dat[1], dat[2]);
                            writer.println("registrato");
                        } else {
                            writer.println("Errore");
                        }
                    }
                    else if (dat[2].equals("Disconnect")) {
                        inviaMessDisc(dat[0]);
                        eliminaUtente(dat[0]);
                    } else if (dat[2].equals("Chat")) {
                        if(isUtenteConnesso(dat[3])){
                            inviaMess(dat[1],dat[0],dat[3]);
                        }else{
                            writer.println("erroreUtente");
                        }
                    }
                }
            } catch (Exception ex) {
                area.appendText("persa connessione \n");
                ex.printStackTrace();
                clientOutput.remove(writer);
            }
        }
    }

    public boolean isUtenteConnesso(String nome) {
        return us.contains(nome);
    }

    public void inviaMess(String message,String sorg,String dest) {
        Iterator it = clientOutput.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message+":"+sorg+":Chat:"+dest);
                area.appendText("INVIATO: " + message + "\n");
                writer.flush();
                area.positionCaret(area.getLength());
            } catch (Exception ex) {
                area.appendText("Error\n");
            }
        }
    }
    public void inviaMessDisc(String sorg) {
        Iterator it = clientOutput.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(sorg+": :"+"Disconnect");
                area.appendText("INVIATO: " + "Disconnect " + sorg+ "\n");
                writer.flush();
                area.positionCaret(area.getLength());
            } catch (Exception ex) {
                area.appendText("Error\n");
            }
        }
    }

    public void eliminaUtente(String data) {
        String mess, add = ": :Connect", done = "Server: :Done", name = data;
        us.remove(name);
        String[] list = new String[(us.size())];
        us.toArray(list);
        for (String app : list) {
            mess = (app + add);
        }
    }

}
