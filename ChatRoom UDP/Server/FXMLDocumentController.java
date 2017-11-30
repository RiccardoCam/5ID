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
    protected boolean continua = true;

    @FXML
    private TextArea area;
    Thread starter=null;

    @FXML
    private void startButtonAction(ActionEvent event) {
        area.appendText("Server avviato!!\n");
        starter = new Thread(new GestioneCliente());
        starter.start();
    }

    @FXML
    private void stopButtonAction(ActionEvent event) {
        continua=false;
        starter.interrupt();
        Thread.currentThread().interrupt();
        area.appendText("Server stop... \n");
        System.exit(0);
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
        area.appendText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public class GestioneCliente implements Runnable {
        static final int PORT = 2222;
        protected DatagramSocket soc = null;

        public GestioneCliente() {
            us = new ArrayList();
            clientOutput = new ArrayList();
            try {
                soc = new DatagramSocket(PORT);
            } catch (Exception ex) {
                area.appendText(" error\n");
            }
        }

        @Override
        public void run() {
            String[] dat;
            DatagramPacket packet=null;
            try {
                while (continua) {
                    byte[] buffer = new byte[1024];
                    packet = new DatagramPacket(buffer, buffer.length);
                    soc.receive(packet);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    System.out.println("mess: " + received);
                    dat = received.split(":");
                    if (dat[0].equals("login")) {
                        clientOutput.add(packet);
                        if (SQLHelper.esistonoCredenziali(dat[1], dat[2]) && !isUtenteConnesso(dat[1])) {
                            us.add(dat[1]);
                            buffer = "entrato".getBytes();
                            soc.send(new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort()));
                        } else {
                            area.appendText("Sbagliato");
                            buffer = "Errore".getBytes();
                            soc.send(new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort()));
                        }
                    }
                    else if (dat[0].equals("registrazione")) {
                        if (!SQLHelper.esisteUsername(dat[1])) {
                            SQLHelper.inserisciUtente(dat[1], dat[2]);
                            buffer = "registrato".getBytes();
                            soc.send(new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort()));
                        } else {
                            buffer = "Errore".getBytes();
                            soc.send(new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort()));
                        }
                    } else if (dat[2].equals("Disconnect")) {
                        inviaMessDisc(dat[0], soc);
                        eliminaUtente(dat[0]);
                    } else if (dat[2].equals("Chat")) {
                        inviaMess(dat[1], dat[0], soc);

                    }
                    else {
                        area.appendText("non va  \n");
                    }
                }
            } catch (Exception ex) {
                area.appendText("persa connessione \n");
                ex.printStackTrace();
                clientOutput.remove(packet);
               
            }
             soc.close();
        }
    }

    public boolean isUtenteConnesso(String nome) {
        return us.contains(nome);
    }

    public void inviaMess(String message, String sorg, DatagramSocket s) {
        Iterator it = clientOutput.iterator();
        byte[] buffer = new byte[1024];
        while (it.hasNext()) {
            try {
                DatagramPacket packet = (DatagramPacket) it.next();
                buffer = (message + ":" + sorg + ":Chat").getBytes();
                s.send(new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort()));
                area.appendText("INVIATO: " + message + "\n");
                area.positionCaret(area.getLength());
            } catch (Exception ex) {
                area.appendText("Error\n");
            }
        }
        System.out.println(Arrays.toString(clientOutput.toArray()));
    }

    public void inviaMessDisc(String sorg, DatagramSocket s) {
        Iterator it = clientOutput.iterator();
        byte[] buffer = new byte[1024];
        while (it.hasNext()) {
            try {
                DatagramPacket packet = (DatagramPacket) it.next();
                buffer = (sorg + ": :" + "Disconnect").getBytes();
                s.send(new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort()));
                area.appendText("INVIATO: " + "Disconnect " + sorg + "\n");
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
