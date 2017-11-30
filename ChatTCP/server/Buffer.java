package server;

import chat.Messaggio;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Buffer implements Runnable {

    private static Buffer istanza;
    private final Server server;
    private final List<Messaggio> messaggi;

    private Buffer(Server server) {
        this.server = server;
        messaggi = new CopyOnWriteArrayList<>();
    }

    public static Buffer getIstanza(Server server) {
        if (istanza == null) {
            istanza = new Buffer(server);
        }
        return istanza;
    }

    public void aggiungiMessaggio(Messaggio messaggio) {
        messaggi.add(messaggio);
    }

    @Override
    public void run() {
        while (true) {
            if (!messaggi.isEmpty()) {
                for (int i = 0; i < messaggi.size(); i++) {
                    Messaggio messaggio = messaggi.get(i);
                    int lM = Integer.parseInt(messaggio.getIntestazione().split(" ")[0]);
                    int lD = Integer.parseInt(messaggio.getIntestazione().split(" ")[1]);
                    String mittente = messaggio.getContenuto().substring(0, lM);
                    String destinatario = messaggio.getContenuto().substring(lM, lM + lD);
                    if (server.isUtenteConnesso(mittente) && server.isUtenteConnesso(destinatario)) {
                        server.inoltraMessaggio(messaggi.get(i));
                        messaggi.remove(i);
                        i--;
                    }
                }
            }
        }
    }
}
