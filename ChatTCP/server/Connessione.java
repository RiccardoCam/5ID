package server;


import chat.Messaggio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class Connessione implements Runnable {

    private static final DatabaseUtenti DATABASE_UTENTI = DatabaseUtenti.getIstanza();

    private final Server server;
    private final Socket socket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private String username;

    public Connessione(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.username = null;
    }

    public void inviaMessaggio(Messaggio messaggio) {
        try {
            output.writeObject(messaggio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Messaggio riceviMessaggio() {
        try {
            return (Messaggio) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            Messaggio messaggio = riceviMessaggio();
            if (messaggio != null) {
                switch (messaggio.getTipo()) {
                    case Login:
                        int usernameLength = Integer.parseInt(messaggio.getIntestazione());
                        String u = messaggio.getContenuto().substring(0, usernameLength);
                        String p = messaggio.getContenuto().substring(usernameLength);
                        if (!server.isUtenteConnesso(u)) {
                            if (DATABASE_UTENTI.loginUtente(u, p)) {
                                inviaMessaggio(Messaggio.creaMessaggioConferma(true, "Utente loggato!"));
                                username = u;
                                server.connettiUtente(u, this);
                            } else {
                                inviaMessaggio(Messaggio.creaMessaggioConferma(false, "Credenziali errate."));
                            }
                        } else {
                            inviaMessaggio(Messaggio.creaMessaggioConferma(false, "Utente già connesso."));
                        }
                        break;
                    case Registrazione:
                        int uL = Integer.parseInt(messaggio.getIntestazione());
                        String nU = messaggio.getContenuto().substring(0, uL);
                        String nP = messaggio.getContenuto().substring(uL);
                        if (DATABASE_UTENTI.registraUtente(nU, nP)) {
                            inviaMessaggio(Messaggio.creaMessaggioConferma(true, "Utente registrato!"));
                        } else {
                            inviaMessaggio(Messaggio.creaMessaggioConferma(false, "Utente gia registrato."));
                        }
                        break;
                    case Conversazione:
                        server.inoltraMessaggio(messaggio);
                        break;
                }
            } else {
                server.disconettiUtente(username);
                return;
            }
        }
    }
}
