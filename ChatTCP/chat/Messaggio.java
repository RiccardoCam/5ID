package chat;

import java.io.Serializable;

public class Messaggio implements Serializable {

    private static final long serialVersionUID = 7436590573031923341L;

    public enum Tipo {
        Login,
        Registrazione,
        Conversazione,
        Utente,
        Conferma
    }

    private final Tipo tipo;
    private final String intestazione;
    private final String contenuto;

    private Messaggio(Tipo tipo, String intestazione, String contenuto) {
        this.tipo = tipo;
        this.intestazione = intestazione;
        this.contenuto = contenuto;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getIntestazione() {
        return intestazione;
    }

    public String getContenuto() {
        return contenuto;
    }

    public static Messaggio creaMessaggioLogin(String username, String password) {
        //Crea messaggio di tipo login: le lunghezze sono inserite per evitare di usare caratteri delimitatori che possono essere raggirati
        return new Messaggio(Tipo.Login, Integer.toString(username.length()), username + password);
    }

    public static Messaggio creaMessaggioRegistrazione(String username, String password) {
        //Crea messaggio di tipo registrazione: le lunghezze sono inserite per evitare di usare caratteri delimitatori che possono essere raggirati
        return new Messaggio(Tipo.Registrazione, Integer.toString(username.length()), username + password);
    }

    public static Messaggio creaMessaggioUtente(String username, boolean stato) {
        return new Messaggio(Tipo.Utente, Boolean.toString(stato), username);
    }

    public static Messaggio creaMessaggioConversazione(String mittente, String destinatario, String contenuto) {
        return new Messaggio(Tipo.Conversazione, mittente.length() + " " + destinatario.length(), mittente + destinatario + contenuto);
    }

    public static Messaggio creaMessaggioConferma(boolean conferma, String messaggio) {
        return new Messaggio(Tipo.Conferma, Boolean.toString(conferma), messaggio);
    }
}
