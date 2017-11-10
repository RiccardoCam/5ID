import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Mercato implements Runnable {

    //Prodotti disponibili
    private static final Map<String, Double> PRODOTTI = new HashMap<>();
    //Sconti disponibili
    private static final Map<String, Double> SCONTI = new HashMap<>();

    static {
        PRODOTTI.put("Uova", 2.0);
        PRODOTTI.put("Succo", 3.0);
        PRODOTTI.put("Zucchero", 1.0);
        PRODOTTI.put("Pane", 0.3);
        PRODOTTI.put("Carote", 0.5);
        PRODOTTI.put("Formaggio", 2.5);
        PRODOTTI.put("Pasta", 1.0);
        PRODOTTI.put("Carne", 4.0);
        PRODOTTI.put("Farina", 2.0);
        PRODOTTI.put("Latte", 1.0);
        PRODOTTI.put("Cioccolato", 1.5);

        SCONTI.put("Pava", 10.0);
        SCONTI.put("Guido", 15.0);
        SCONTI.put("Mario", 5.0);
        SCONTI.put("Luigi", 7.5);
    }

    //Comunicazione con il client
    private final Socket s;
    private final BufferedReader in;
    private final PrintWriter out;

    //Informazione del client
    private Double conto;
    private Double sconto;
    private List<String> prodotti;
    private String username;

    public Mercato(Socket socket) throws IOException {
        //Connessione
        s = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        //Sessione
        sconto = 0.0;
        conto = 0.0;
        prodotti = new ArrayList<>();
        username = "Guest";
    }

    private List<String> getProdotti() {
        List<String> lista = new ArrayList<>();
        for (Entry<String, Double> entry : PRODOTTI.entrySet()) {
            lista.add(entry.getKey() + " " + String.format("%.2f", entry.getValue()) + "€");
        }
        return lista;
    }

    private Double getCosto(String prodotto) {
        return PRODOTTI.get(prodotto);
    }

    private Double getSconto() {
        return SCONTI.getOrDefault(username, 0.0);
    }

    private Double getConto() {
        conto = 0.0;
        for (String prodotto : prodotti) {
            conto += getCosto(prodotto);
        }
        conto = Math.max(0, conto);
        return conto;
    }

    private String riceviMessaggio() throws IOException {
        System.out.println("Ricevo");
        return in.readLine();
    }

    private void inviaMessaggio(String messaggio) {
        out.println(messaggio);
    }

    private String formattaValuta(Double valore) {
        return String.format("%.2f", valore) + "€";
    }

    @Override
    public void run() {
        try {
            String clientLine;
            label:
            while ((clientLine = riceviMessaggio()) != null) {
                switch (clientLine) {
                    case "#SET Utente":
                        username = riceviMessaggio();
                        System.out.println("Client connesso: " + username);
                        break;
                    case "#GET Prodotti":
                        for (String prodotto : getProdotti()) {
                            inviaMessaggio(prodotto);
                        }
                        inviaMessaggio("#END");
                        break;
                    case "#SET Prodotti":
                        prodotti.clear();
                        while (!(clientLine = riceviMessaggio()).equals("#END")) {
                            prodotti.add(clientLine.split(" ")[0]);
                        }
                        break;
                    case "#GET Conto":
                        inviaMessaggio(formattaValuta(getConto()) + ((sconto == 0.0) ? "" : " -" + formattaValuta(sconto)));
                        break;
                    case "#GET Sconto":
                        sconto = getSconto();
                        inviaMessaggio(formattaValuta(getConto()) + ((sconto == 0.0) ? "" : " -" + formattaValuta(sconto)));
                        break;
                    case "#GET Pagamento":
                        if (username.equals("Guest")) {
                            inviaMessaggio("Il tuo pagamento di " + formattaValuta(conto) + " è stato eseguito.");
                        } else {
                            Double totale = Math.max(0, conto - sconto);
                            Double nuovoSconto;
                            //Se lo sconto copre totalmente il conto non assegno nuovo sconto
                            if (conto <= sconto) {
                                nuovoSconto = sconto - conto;
                            } else {
                                //Se il conto supera lo sconto
                                if (sconto == 0) {
                                    //Se non ho usato lo sconto aggiungo un nuovo sconto allo sconto gia presente
                                    nuovoSconto = getSconto() + totale * 0.1;
                                } else {
                                    //Se ho usato lo sconto ricalcolo lo sconto sull'eccedenza
                                    nuovoSconto = (conto - sconto) * 0.1;
                                }
                            }
                            inviaMessaggio("Il tuo pagamento di " + formattaValuta(totale) + " è stato eseguito,"
                                    + " il tuo sconto è di " + formattaValuta(nuovoSconto));
                            SCONTI.put(username, nuovoSconto);
                        }
                        break label;
                }
            }
        } catch (IOException ex) {
        } finally {
            try {
                s.close();
                System.out.println("Client disconnesso: " + username);
            } catch (IOException ex1) {

            }
        }
    }
}
