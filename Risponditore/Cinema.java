
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risponditore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author riccardo.camillo
 */
public class Cinema implements Runnable {

    private Socket socket;
    private int clientNumber;

    public Cinema(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        System.out.println("Conversando con il cliente:" + clientNumber);
    }

    @Override
    public void run() {
        Grafo g = new Grafo();
        ArrayList<String> lista = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            lista = g.getFirst();
            int conta = 0;
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).indexOf("server:") < 0) {
                    out.println(conta + " - " + lista.get(i));
                    conta++;
                } else {
                    out.println(lista.get(i));
                }
            }
            out.println("inserisci numero");
            conta = 0;
            while (true) {
                int input = Integer.parseInt(in.readLine());
                lista = g.get(input);
                if (lista == null) {
                    out.println("Buona visione del film cliente: " + clientNumber + ".");
                    socket.close();
                    break;
                }
                if (lista.size() != 0) {
                    for (int i = 0; i < lista.size(); i++) {
                        if (lista.get(i).indexOf("server") < 0) {
                            out.println(conta + " - " + lista.get(i));
                            conta++;
                        } else {
                            out.println(lista.get(i));
                        }
                    }
                    out.println("inserisci numero");
                    conta = 0;
                } else {
                    out.println("numero non valido");
                    out.println("inserisci numero");
                }
            }
        } catch (IOException e) {
            System.out.println("Errore con cliente:  " + clientNumber + ": " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Il socket non è stato chiuso correttamente");
            }
            System.out.println("connessione con il cliente: " + clientNumber + " chiusa");
        }
    }
}
    class Grafo {

        private Nodo alberoOriginale;
        private Nodo attuale;
        private String ultima;

        public Grafo() {
            Nodo albero = new Nodo("server: Buongiorno");
            alberoOriginale = albero;
            albero.creaNext("server: Cosa desidera?");
            albero = albero.getNext();
            Nodo alberoIIStep = albero;
            Nodo z = new Nodo("server: Ecco a lei i biglietti, vuole qualcos'altro?");
            z.creaNext("No");
            z.creaNext("Si");
            Nodo zII = z;
            z = z.getNext(1);
            z.immettiNext(alberoIIStep);
            albero.creaNext("Ritirare i biglietti");
            albero.creaNext("Comprare i biglietti");
            albero = albero.getNext(0);
            albero.creaNext("Mi può dare il codice?");
            albero = albero.getNext();
            albero.immettiNext(zII);
            albero = alberoIIStep.getNext(1);
            albero.creaNext("server: Per quale spettacolo vuole coprare i biglietti?");
            albero = albero.getNext();
            albero.creaNext("Thor");
            albero.creaNext("Deadpool");
            albero.creaNext("Ironman");
            albero.getNext(0).immettiNext(zII);
            albero.getNext(1).immettiNext(zII);
            albero.getNext(2).immettiNext(zII);
            attuale = alberoOriginale;
            ultima = attuale.getValore();
        }

        public ArrayList<String> getFirst() {
            ArrayList<String> lista = new ArrayList<>();
            lista.add(attuale.getValore());
            while (true) {
                for (int j = 0; j < attuale.getSuccessori().size(); j++) {
                    lista.add(attuale.getNext(j).getValore());
                }
                if (lista.get(lista.size() - 1).indexOf("server") < 0) {
                    ultima = lista.get(lista.size() - 1);
                    return lista;
                }
                attuale = attuale.getNext();
            }
        }

        public ArrayList<String> get(int i) {
            ArrayList<String> lista = new ArrayList<>();
            int controlloCodice;
            if (attuale.getNext().getValore().equals("Mi può dare il codice?")) {
                i = 0;
            }
            if (i >= attuale.getSuccessori().size()) {
                return lista;
            }
            attuale = attuale.getNext(i);
            if (attuale.getValore().equals("No")) {
                return null;
            }
            while (true) {
                for (int j = 0; j < attuale.getSuccessori().size(); j++) {
                    lista.add(attuale.getNext(j).getValore());
                }
                if (lista.get(lista.size() - 1).indexOf("server") < 0) {
                    return lista;
                }
                attuale = attuale.getNext();
            }
        }
    }

    class Nodo {

        private ArrayList<Nodo> successori = new ArrayList<>();
        private String valore;

        public Nodo(String s) {
            valore = s;
        }

        public void creaNext(String s) {
            successori.add(new Nodo(s));
        }

        public void immettiNext(Nodo x) {
            successori.add(x);
        }

        public String getValore() {
            return valore;
        }

        public ArrayList<Nodo> getSuccessori(){
            return successori;
        }
        
        public Nodo getNext() {
            return successori.get(0);
        }

        public Nodo getNext(int i) {
            return successori.get(i);
        }
    }
