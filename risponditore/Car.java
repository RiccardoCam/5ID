import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author leonardo
 */
public class Car implements Runnable {

    private ArrayList<Pair<String, Integer>> allestimenti;
    private ArrayList<Pair<String, Integer>> colori;
    private ArrayList<Pair<String, Integer>> cerchioni;
    private ArrayList<Pair<String, Integer>> sedili;
    private ArrayList<Pair<String, Integer>> optionalConfort;
    private ArrayList<ArrayList<Pair<String, Integer>>> macchina;
    private ArrayList<String> nomi;

    private int costo = 0;
    private Socket s;

    public Car(Socket s) {
        this.allestimenti = new ArrayList<>();
        this.colori = new ArrayList<>();
        this.cerchioni = new ArrayList<>();
        this.sedili = new ArrayList<>();
        this.optionalConfort = new ArrayList<>();
        this.macchina = new ArrayList<>();
        this.nomi = new ArrayList<>();
        this.macchina.add(allestimenti);
        this.macchina.add(colori);
        this.macchina.add(cerchioni);
        this.macchina.add(sedili);
        this.macchina.add(optionalConfort);
        this.s = s;

        nomi.add("L'ALLESTIMENTO");
        nomi.add("I COLORI");
        nomi.add("I CERCHIONI");
        nomi.add("IL SEDILE");
        nomi.add("GLI OPTIONAL PERT IL CONFORT");

        allestimenti.add(new Pair("classica", 20000));
        allestimenti.add(new Pair("pista", 22000));
        allestimenti.add(new Pair("turismo", 24000));
        allestimenti.add(new Pair("competizione", 27000));

        colori.add(new Pair("colore rosso", 500));
        colori.add(new Pair("colore giallo", 500));
        colori.add(new Pair("colore nero", 500));
        colori.add(new Pair("colore blu-nero", 1000));
        colori.add(new Pair("colore rosa", 500));

        cerchioni.add(new Pair("cerchioni turismo", 0));
        cerchioni.add(new Pair("cerchioni sport", 200));
        cerchioni.add(new Pair("cerchioni turing", 300));
        cerchioni.add(new Pair("cerchioni trofeo", 400));

        sedili.add(new Pair("sedile classico", 0));
        sedili.add(new Pair("sedile sportivo", 1000));
        sedili.add(new Pair("sedile trofeo", 2000));

        optionalConfort.add(new Pair("fendinebbia", 200));
        optionalConfort.add(new Pair("sensori parcheggio", 300));
        optionalConfort.add(new Pair("tetto apribile elettronicamente", 1000));
        optionalConfort.add(new Pair("fendinebbia", 200));

    }

    public ArrayList<Pair<String, Integer>> give(int i) {
        return this.macchina.get(i);
    }

    public void aggiunta(int i, int j) {
        ArrayList<Pair<String, Integer>> app = macchina.get(i);
        String s = app.get(j).getKey();
        this.costo = this.costo + this.macchina.get(i).get(j).getValue();
        app.add(j, new Pair(s + "@", app.get(j).getValue()));
        app.remove(j + 1);

    }

    public void rimuovi(int i, int j) {

        ArrayList<Pair<String, Integer>> app = macchina.get(i);
        String s = app.get(j).getKey();
        this.costo = this.costo - app.get(j).getValue();
        app.add(j, new Pair(s.substring(0, s.length() - 1), app.get(j).getValue()));
        app.remove(j + 1);

    }

    public int getPrezzo() {
        return this.costo;
    }

    public String componi(int indice) {
        String s = "Scegli " + nomi.get(indice) + ": 0)avanti ";
        int contatore = 1;
        ArrayList<Pair<String, Integer>> app = macchina.get(indice);
        for (int i = 0; i < app.size(); i++) {
            s = s + contatore + ")" + app.get(i).getKey() + "  ";
            contatore++;
        }
        s += contatore + ")" + "indietro";
        return s;
    }

    @Override
    public void run() {
        int app;
        String controllo = "";
        int contatore = 0;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            while (contatore < macchina.size()) {
                out.println(this.componi(contatore));
                controllo = in.readLine();

                app = Integer.parseInt(controllo);

                if (app <= macchina.get(contatore).size() + 1 && app >= 0) {

                    if (app == 0) {
                        contatore++;
                        out.println("");
                    } else {

                        if (app == macchina.get(contatore).size() + 1) {
                            if (contatore != 0) {
                                contatore--;
                                out.println("");
                            }else out.println("impossibile tornare indietro");
                        } else {

                            if (macchina.get(contatore).get(app - 1).getKey().contains("@")) {
                                this.rimuovi(contatore, app - 1);
                            } else {
                                this.aggiunta(contatore, app - 1);
                            }
                            contatore++;
                            out.println("Prezzo attuale:" + this.getPrezzo());
                        }
                        
                    }

                } else {
                    out.println("selezione non trovata");
                }

                if (contatore == macchina.size()) {

                    out.print("sei sicuro di voler concludere l'acquisto?? (S/N)");

                    out.println("");
                    System.out.println("entrato");

                    String str = in.readLine();
                    str = str.toLowerCase();
                    System.out.println("entrato");
                    if (str.equals("n")) {
                        contatore--;
                    }

                    out.println("Prezzo attuale:" + this.getPrezzo());

                    System.out.println("entrato");
                }
            }

            out.println("#");
            out.println("Prezzo finale:" + this.getPrezzo());

        } catch (IOException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
