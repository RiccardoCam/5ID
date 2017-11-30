package server;

import chat.Messaggio;

import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Server {

    private static final int SERVER_PORT = 8008;

    private final Buffer buffer;
    private final Map<String, Connessione> utentiConnessi;

    private Server() {
        utentiConnessi = new ConcurrentHashMap<>();
        buffer = Buffer.getIstanza(this);
        new Thread(buffer).start();
    }

    boolean isUtenteConnesso(String username) {
        return utentiConnessi.containsKey(username);
    }

    void connettiUtente(String username, Connessione connessione) {
        if (username != null && !isUtenteConnesso(username)) {
            System.out.println("Utente connesso: " + username);
            for (String utente : utentiConnessi.keySet()) {
                connessione.inviaMessaggio(Messaggio.creaMessaggioUtente(utente, true));
                utentiConnessi.get(utente).inviaMessaggio(Messaggio.creaMessaggioUtente(username, true));
            }
            utentiConnessi.put(username, connessione);
        }
    }

    void disconettiUtente(String username) {
        if (username != null && isUtenteConnesso(username)) {
            System.out.println("Utente disconesso: " + username);
            utentiConnessi.remove(username);
            for (String utente : utentiConnessi.keySet()) {
                utentiConnessi.get(utente).inviaMessaggio(Messaggio.creaMessaggioUtente(username, false));
            }
        }
    }

    void inoltraMessaggio(Messaggio messaggio) {
        switch (messaggio.getTipo()) {
            case Conversazione:
                int lM = Integer.parseInt(messaggio.getIntestazione().split(" ")[0]);
                int lD = Integer.parseInt(messaggio.getIntestazione().split(" ")[1]);
                String mittente = messaggio.getContenuto().substring(0, lM);
                String destinatario = messaggio.getContenuto().substring(lM, lM + lD);
                String contenuto = messaggio.getContenuto().substring(lM + lD);
                if (utentiConnessi.containsKey(destinatario)) {
                    utentiConnessi.get(mittente).inviaMessaggio(Messaggio.creaMessaggioConversazione(mittente, destinatario, contenuto));
                    utentiConnessi.get(destinatario).inviaMessaggio(Messaggio.creaMessaggioConversazione(mittente, destinatario, contenuto));
                } else {
                    buffer.aggiungiMessaggio(messaggio);
                    System.out.println();
                }
                break;
        }
    }

    public static void main(String[] args) {
        try (ServerSocket listener = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server acceso");
            Server server = new Server();
            while (true) {
                new Thread(new Connessione(server, listener.accept())).start();
            }
        } catch (Exception e) {
            System.out.println("Il server già acceso");
        }
    }
}