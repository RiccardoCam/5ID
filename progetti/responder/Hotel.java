/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package responder;

import java.util.ArrayList;

/**
 *
 * @author Sandro
 */
public class Hotel {

    public static void main(String[] args) {
        Hotel h= new Hotel();
       ArrayList<Arco<String>> x= h.getAdiacenti(h.domandaAttuale);
        System.out.println(x);
    }
    protected String domandaAttuale;
    protected ArrayList<String> domande;
    protected ArrayList<ArrayList<Arco<String>>> grafo;

    public Hotel() {
        domandaAttuale = "Come posso aiutarti?prenotare,visitare,informazioni";
        domande = new ArrayList<>();
        grafo = new ArrayList<>();
        init();
    }
//inizializzo il grafo, vedi starUML

    public void init() {
        initDomande();
        initArchi();
    }

    //aggiungo una domanda nel grafo
    public void addDomanda(String domanda) {
        domande.add(domanda);
        grafo.add(domande.indexOf(domanda), new ArrayList<>());

    }

    //aggiungo un arco nel grafo
    public void addArco(String s, String d, String risposta) {
        if (domande.contains(s) && domande.contains(d)) {
            Arco a = new Arco(s, d, risposta);
            grafo.get(domande.indexOf(s)).add(a);
        }

    }

    /**
     * metodo usato dal server per prendere le domande a seconda della risposta
     * da parte del client
     *
     * @param risposta
     * @return ret prendo tutti gli adiacenti di 'risposta' e controllo con
     * espressione regolare per scegliere la prossima domanda
     */
    public String getDomanda(String risposta) {
        String ret = "risposta non valida, rispondi di nuovo per favore";
        ArrayList<Arco<String>> adiacenti = getAdiacenti(domandaAttuale);
        for (Arco x : adiacenti) {
            if (risposta.matches(x.domanda)) {
                ret = x.dest;
                domandaAttuale = ret;
            } 

        }
        return ret;
    }
//prendo gli adiacenti dato una stringa

    public ArrayList<Arco<String>> getAdiacenti(String sorg) {
        return grafo.get(domande.indexOf(sorg));
    }

    @Override
    public String toString() {
        return "Hotel{" + "domande=" + domande + ", grafo=" + grafo + '}';
    }

    /**
     * inizializzo domande, vedi progetto starUML
     */
    public void initDomande() {
        String[] domande = {"Come posso aiutarti?prenotare,visitare,informazioni",
            "Quando devi venire?", "Quando parti?", "Quante camere?", "Vuoi accedere al ristorante riservato a soli clienti?", "Vuoi accedere all'ultimo piano relax?",
            "Il direttore è Sandro Lu, Contatto telefonico:3451146381, vuoi prendere apputamento?", "Per quando vuoi prenotare?",
            "Chi vieni a visitare?", "Quando vieni?", "end"};
        for (int i = 0; i < domande.length; i++) {
            addDomanda(domande[i]);
        }
    }

    /**
     * inizializzo gli archi, vedi progetto starUML uso espressioni regolari per
     * passare tra una domanda all'altra con il metodo .matches
     */
    public void initArchi() {
//prima parte
        addArco("Come posso aiutarti?prenotare,visitare,informazioni", "Quando devi venire?", "^.*prenot.*$");
        addArco("Quando devi venire?", "Quando parti?", "^.*(0[1-9]|(1|2)[0-9]|30)/(0[1-9]|1[0-2])/(2017|2018|2019).*$");
        addArco("Quando parti?", "Quante camere?", "^.*(0[1-9]|(1|2)[0-9]|30)/(0[1-9]|1[0-2])/(2017|2018|2019).*$");
        addArco("Quante camere?", "Vuoi accedere al ristorante riservato a soli clienti?", "[0-5]");
        addArco("Vuoi accedere al ristorante riservato a soli clienti?", "Vuoi accedere all'ultimo piano relax?", "si|no");
        addArco("Vuoi accedere all'ultimo piano relax?", "end", "si|no");
//seconda parte
        addArco("Come posso aiutarti?prenotare,visitare,informazioni", "Il direttore è Sandro Lu, Contatto telefonico:3451146381, vuoi prendere apputamento?", "^.*info.*$");

        addArco("Il direttore è Sandro Lu, Contatto telefonico:3451146381, vuoi prendere apputamento?", "end", "no");
        addArco("Il direttore è Sandro Lu, Contatto telefonico:3451146381, vuoi prendere apputamento?", "Per quando vuoi prenotare?", "si");
        addArco("Per quando vuoi prenotare?", "end", "^.*(0[1-9]|(1|2)[0-9]|30)/(0[1-9]|1[0-2])/(2017|2018|2019).*$");
//terza parte
        addArco("Come posso aiutarti?prenotare,visitare,informazioni", "Chi vieni a visitare?", "^.*visit.*$");
        addArco("Chi vieni a visitare?", "Quando vieni?", "^.*[a-z].*$");
        addArco("Quando vieni?", "end", "^.*(0[1-9]|(1|2)[0-9]|30)/(0[1-9]|1[0-2])/(2017|2018|2019).*$");

    }

}

/**
 * classe usato per la struttura del grafo
 *
 */
class Arco<E> {

    protected String source;
    protected String dest;
    protected String domanda;

    public Arco(String s, String d, String domanda) {
        source = s;
        dest = d;
        this.domanda = domanda;

    }
//toString usato per fare test di questa classe utilizzando il main method delle classi

    @Override
    public String toString() {
        return "\nArco{" + "source=" + source + ", dest=" + dest + ", domanda=" + domanda + "}";
    }

}
