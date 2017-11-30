package client;

import chat.Messaggio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Ricevitore implements Runnable {

    private static Ricevitore istanza;

    private final Client client;
    private final Socket socket;
    private final ObjectInputStream input;

    private Ricevitore(Client client, Socket socket, ObjectInputStream input) {
        this.client = client;
        this.socket = socket;
        this.input = input;
    }

    public static Ricevitore getIstanza(Client client, Socket socket, ObjectInputStream input) throws IOException {
        if (istanza == null) {
            istanza = new Ricevitore(client, socket, input);
        }
        return istanza;
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                Messaggio messaggio = (Messaggio) input.readObject();
                switch (messaggio.getTipo()) {
                    case Utente:
                        boolean stato = Boolean.parseBoolean(messaggio.getIntestazione());
                        String utente = messaggio.getContenuto();
                        client.aggiornaContatto(utente, stato);
                        break;
                    case Conversazione:
                        int lM = Integer.parseInt(messaggio.getIntestazione().split(" ")[0]);
                        int lD = Integer.parseInt(messaggio.getIntestazione().split(" ")[1]);
                        String mittente = messaggio.getContenuto().substring(0, lM);
                        String destinatario = messaggio.getContenuto().substring(lM, lM + lD);
                        String contenuto = messaggio.getContenuto().substring(lM + lD);
                        client.riceviConversazione(mittente, destinatario, contenuto);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Il server è stato chiuso");
                return;
            }
        }
    }
}
