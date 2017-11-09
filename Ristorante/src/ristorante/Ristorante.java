/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ristorante;

import java.util.ArrayList;

/**
 *
 * @author Alvise
 */
public class Ristorante {

    public static int prossimaDomanda = 0;
    public static boolean fine = false;
    public static ArrayList<String> domande;
    public static ArrayList<ArrayList<Arco>> g = new ArrayList<>();
    public static String ripetizione = "non ho capito, potresti ripetere?";
    public static double costoTotale = 0;

    public Ristorante() {
        domande = new ArrayList<>();
        domande.add("hai intenzione di prenotare un tavolo? [Si, No]"); //0
        domande.add("per quale orario?"); //1
        domande.add("per quante persone?"); //2
        domande.add("allora il menù da asporto lo vuoi ordinare con il Sushi o con il Tempura? [Sushi, Tempura]"); //3
        domande.add("vuoi ordinare anche da bere? [Si, No]"); //4
        domande.add("va bene dimmi quale bibita [Acqua, Fanta, CocaCola, Pepsi]"); //5
        domande.add("per che ora ha intenzione di venire a ritirare l'ordinazione?"); //6
        domande.add("grazie arrivederci!"); //7

        for (int i = 0; i < 9; i++) {
            g.add(new ArrayList<>());
        }

        g.get(0).add(new Arco("SI", 1, 0));
        g.get(0).add(new Arco("NO", 3, 0));

        g.get(1).add(new Arco("", 2, 0));

        g.get(2).add(new Arco("", 7, 0));

        g.get(3).add(new Arco("SUSHI", 4, 15));
        g.get(3).add(new Arco("TEMPURA", 4, 13));
        g.get(3).add(new Arco("NO", 7, 0));

        g.get(4).add(new Arco("SI", 5, 0));
        g.get(4).add(new Arco("NO", 6, 0));

        g.get(5).add(new Arco("FANTA", 6, 2));
        g.get(5).add(new Arco("ACQUA", 6, 1.5));
        g.get(5).add(new Arco("COCACOLA", 6, 3));
        g.get(5).add(new Arco("PEPSI", 6, 2.5));

        g.get(6).add(new Arco("", 7, 0));

        g.get(7).add(new Arco("", -1, 0));

    }

    public static String getPrimaDomanda() {
        return domande.get(0);
    }

    public static String getUltimaDomanda() {
        return domande.get(domande.size() - 1);
    }

    public static String getProssimaDomanda(String domCorrente, String risposta) {
        String app = "";
        for (int i = 0; i < domande.size(); i++) {
            if (domCorrente.equals(domande.get(i))) {
                for (int j = 0; j < g.get(i).size(); j++) {
                    if (g.get(i).get(j).risposta.equals("")) {
                        if (i == 2 && risposta.matches("\\d+")) {
                            prossimaDomanda = g.get(i).get(j).nProssimaDomanda;
                            app = domande.get(prossimaDomanda);
                            costoTotale += g.get(i).get(j).costo;
                            break;
                        }
                        if (risposta.matches("(^(0|1)[0-9]|^2[0-3]):[0-5]\\d")) {
                            prossimaDomanda = g.get(i).get(j).nProssimaDomanda;
                            app = domande.get(prossimaDomanda);
                            costoTotale += g.get(i).get(j).costo;
                            break;
                        }
                    }
                    if (risposta.toUpperCase().equals(g.get(i).get(j).risposta)) {
                        prossimaDomanda = g.get(i).get(j).nProssimaDomanda;
                        app = domande.get(prossimaDomanda);
                        costoTotale += g.get(i).get(j).costo;
                        break;
                    }
                }
                break;
            }
        }
        if (app.equals("")) {
            app = ripetizione;
        }
        return app;

    }

    public static double getCostoTotale() {
        return costoTotale;
    }

    public static void setCostoTotale(double costoTotale) {
        Ristorante.costoTotale = costoTotale;
    }
}

class Arco {

    String risposta;
    int nProssimaDomanda;
    double costo;

    public Arco(String risposta, int nProssimaDomanda, double costo) {
        this.risposta = risposta;
        this.nProssimaDomanda = nProssimaDomanda;
        this.costo = costo;
    }

}
