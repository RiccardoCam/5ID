package ristorante;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca
 */
public class Ristorante extends Thread {

    private int stato;
    private final int ATTESA_NOME = 1;
    private final int RICHIESTA_ANTIPASTO = 2;
    private final int ALTRO_ANTIPASTO = 3;
    private final int RICHIESTA_PRIMO = 4;
    private final int ALTRO_PRIMO = 5;
    private final int RICHIESTA_SECONDO = 6;
    private final int ALTRO_SECONDO = 7;
    private final int RICHIESTA_BIBITE = 8;
    private final int ALTRE_BIBITE = 9;
    private final int CHIUSURA = 10;
    public String nome;
    public ArrayList<Integer> antipasto;
    public ArrayList<Integer> primo;
    public ArrayList<Integer> secondo;
    public ArrayList<Integer> bibite;
    public ArrayList<String> menuAntipasto;
    public ArrayList<String> menuPrimo;
    public ArrayList<String> menuSecondo;
    public ArrayList<String> menuBibite;
    private Socket socket;

    public Ristorante(Socket socket) {
        stato = ATTESA_NOME;

        menuAntipasto = new ArrayList<>();
        menuAntipasto.add("Culatello e Burrata");
        menuAntipasto.add("Formaggio fuso, funghi e polenta");
        menuAntipasto.add("Affettati Misti");

        menuPrimo = new ArrayList<>();
        menuPrimo.add("Spaghetti cacio e pepe");
        menuPrimo.add("Risotto di rane");
        menuPrimo.add("Gnocchi al ragù");

        menuSecondo = new ArrayList<>();
        menuSecondo.add("Fiorentina");
        menuSecondo.add("Tartar di manzo");
        menuSecondo.add("Pollo fritto con patate");

        menuBibite = new ArrayList<>();
        menuBibite.add("CocaCola");
        menuBibite.add("Fanta");
        menuBibite.add("Acqua");
        menuBibite.add("Vino");
        menuBibite.add("Birra");

        this.socket = socket;
        antipasto = new ArrayList<>();
        primo = new ArrayList<>();
        secondo = new ArrayList<>();
        bibite = new ArrayList<>();

    }

    public String riceviInfo(String s) {
        String result;
        switch (stato) {
            case ATTESA_NOME:
                result = salvaNome(s);
                break;
            case RICHIESTA_ANTIPASTO:
                result = aggiungiAntipasto(s);
                break;
            case ALTRO_ANTIPASTO:
                result = richiestaAltroAntipasto(s);
                break;
            case RICHIESTA_PRIMO:
                result = aggiungiPrimo(s);
                break;
            case ALTRO_PRIMO:
                result = richiestaAltroPrimo(s);
                break;
            case RICHIESTA_SECONDO:
                result = aggiungiSecondo(s);
                break;
            case ALTRO_SECONDO:
                result = richiestaAltroSecondo(s);
                break;
            case RICHIESTA_BIBITE:
                result = aggiungiBibita(s);
                break;
            case ALTRE_BIBITE:
                result = richiestaAltraBibita(s);
                break;
            default: //stato non consentito
                result = "Errore";
        }

        String messaggio = messaggioStato();
        return result + ", " + messaggio;
    }

    private String salvaNome(String s) {
        s = s.trim();
        if (s.length() > 0) {
            nome = s;
            stato = RICHIESTA_ANTIPASTO;
            return "Nome salvato";
        } else {
            return "Nome errato, reinserirlo";
        }
    }

    private String aggiungiAntipasto(String s) {
        String errore = "Antipasto non presente nel menu";
        try {
            int idAntipasto = Integer.parseInt(s);
            if (idAntipasto < menuAntipasto.size() && idAntipasto>=0) {
                antipasto.add(idAntipasto);
                stato = ALTRO_ANTIPASTO;
                return "Antipasto aggiunto";
            } else {
                return errore;
            }
        } catch (NumberFormatException e) {
            return errore;
        }
    }

    private String richiestaAltroAntipasto(String s) {
        if (s.equals("S")) {
            stato = RICHIESTA_ANTIPASTO;
            return "Ok";
        } else if (s.equals("N")) {
            stato = RICHIESTA_PRIMO;
            return "Ok";
        } else {
            return "Errore";
        }
    }

    private String aggiungiPrimo(String s) {
        String errore = "Primo non presente nel menu";
        try {
            int idPrimo = Integer.parseInt(s);
            if (idPrimo < menuPrimo.size() && idPrimo>=0) {
                antipasto.add(idPrimo);
                stato = ALTRO_PRIMO;
                return "Primo aggiunto";
            } else {
                return errore;
            }
        } catch (NumberFormatException e) {
            return errore;
        }
    }

    private String richiestaAltroPrimo(String s) {
        if (s.equals("S")) {
            stato = RICHIESTA_PRIMO;
            return "Ok";
        } else if (s.equals("N")) {
            stato = RICHIESTA_SECONDO;
            return "Ok";
        } else {
            return "Errore";
        }
    }

    private String aggiungiSecondo(String s) {
        String errore = "Secondo non presente nel menu";
        try {
            int idSecondo = Integer.parseInt(s);
            if (idSecondo < menuSecondo.size() && idSecondo>=0) {
                antipasto.add(idSecondo);
                stato = ALTRO_SECONDO;
                return "Secondo aggiunto";
            } else {
                return errore;
            }
        } catch (NumberFormatException e) {
            return errore;
        }
    }

    private String richiestaAltroSecondo(String s) {
        if (s.equals("S")) {
            stato = RICHIESTA_SECONDO;
            return "Ok";
        } else if (s.equals("N")) {
            stato = RICHIESTA_BIBITE;
            return "Ok";
        } else {
            return "Errore";
        }
    }

    private String aggiungiBibita(String s) {
        String errore = "Bibita non presente nel menu";
        try {
            int idBibita = Integer.parseInt(s);
            if (idBibita < menuBibite.size() && idBibita>=0) {
                bibite.add(idBibita);
                stato = ALTRE_BIBITE;
                return "Bibita aggiunta";
            } else {
                return errore;
            }
        } catch (NumberFormatException e) {
            return errore;
        }
    }

    private String richiestaAltraBibita(String s) {
        if (s.equals("S")) {
            stato = RICHIESTA_BIBITE;
            return "Ok";
        } else if (s.equals("N")) {
            stato = CHIUSURA;
            return "Ok";
        } else {
            return "Errore";
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Ciao, " + messaggioStato());
            while (true) {
                String input = in.readLine();
                out.println(riceviInfo(input));
                if (stato == CHIUSURA) {
                    out.println("Close connection for client");
                    socket.close();
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Ristorante.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String creaElenco(ArrayList menu) {
        String lista = "";
        for (int i = 0; i < menu.size(); i++) {
            lista += i + " - " + menu.get(i) + ", ";
        }
        return lista;
    }

    private String messaggioStato() {
        String messaggio;
        switch (stato) {
            case ATTESA_NOME:
                messaggio = "Nome della prenotazione";
                break;
            case RICHIESTA_ANTIPASTO:
                messaggio = "Elenco antipasti (scegli numero antipasto): " + creaElenco(menuAntipasto);
                break;
            case ALTRO_ANTIPASTO:
                messaggio = "Un'altro antipasto? (S/N)";
                break;
            case RICHIESTA_PRIMO:
                messaggio = "Elenco primi (scegli numero primo): " + creaElenco(menuPrimo);
                break;
            case ALTRO_PRIMO:
                messaggio = "Un'altro primo? (S/N)";
                break;
            case RICHIESTA_SECONDO:
                messaggio = "Elenco secondi (scegli numero secondo): " + creaElenco(menuSecondo);
                break;
            case ALTRO_SECONDO:
                messaggio = "Un'altro secondo? (S/N)";
                break;
            case RICHIESTA_BIBITE:
                messaggio = "Elenco bibite (scegli numero bibita)" + creaElenco(menuBibite);
                break;
            case ALTRE_BIBITE:
                messaggio = "Un'altra bibita ? (S/N)";
                break;
            case CHIUSURA:
                messaggio = "Ordine ricevuto";
                break;
            default:
                messaggio = "";
                break;
        }
        return messaggio;
    }

}

