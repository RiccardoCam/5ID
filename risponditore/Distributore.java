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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martorel
 */
public class Distributore implements Runnable {

    private Socket socket;
    private ArrayList<Nodo> domande;
    private double prezzo;
    private int attuale;
    private int inserito;

    public Distributore(Socket cliente) {
        socket = cliente;
        prezzo = 0.0;
        attuale = 0;

    }

    public void grafo() {
        domande = new ArrayList<Nodo>();
        //aggiungo 0
        System.out.println("aggiungo 0");
        domande.add(new Nodo("Benvenuto, Da dove vuoi cominciare? Digitare 1 per Carburante, 2 per Meccanico, 3 per Gommista e 4 per terminare e pagare", 0, new ArrayList<Integer>(), null));
        domande.get(0).aggiungiSuccessore(1);
        domande.get(0).aggiungiSuccessore(8);
        domande.get(0).aggiungiSuccessore(11);
        //aggiungo 1
        System.out.println("aggiungo 1");
        domande.add(new Nodo("Digitare 1 per fare benzina, 2 per fare disel, 3 per fare GPL", 1, new ArrayList<Integer>(), null));
        domande.get(1).aggiungiSuccessore(2);
        domande.get(1).aggiungiSuccessore(4);
        domande.get(1).aggiungiSuccessore(6);
        Benzina b = new Benzina(1.369);
        //aggiungo 2
        System.out.println("aggiungo 2");
        domande.add(new Nodo("Digitare la quantitá intera di litri da caricare, prezzo al L " + b.getPrezzo() + "€", 1, new ArrayList<Integer>(), b));
        domande.get(2).aggiungiSuccessore(3);
        //aggiungo 3
        System.out.println("aggiungo 3");
        domande.add(new Nodo("Il prezzo per ora raggiunto è " + prezzo + "€ digitare una qualsiasi cosa per continuare", 0, new ArrayList<Integer>(), null));
        domande.get(3).aggiungiSuccessore(0);
        Disel d = new Disel(1.295);
        //aggiungo 4
        System.out.println("aggiungo 4");
        domande.add(new Nodo("Digitare la quantitá intera di litri da caricare, prezzo al L " + d.getPrezzo() + "€", 2, new ArrayList<Integer>(), d));
        domande.get(4).aggiungiSuccessore(5);
        //aggiungo 5
        System.out.println("aggiungo 5");
        domande.add(new Nodo("Il prezzo per ora raggiunto è " + prezzo + "€ digitare una qualsiasi cosa per continuare", 0, new ArrayList<Integer>(), null));
        domande.get(5).aggiungiSuccessore(0);
        GPL g = new GPL(1.295);
        //aggiungo 6
        System.out.println("aggiungo 6");
        domande.add(new Nodo("Digitare la quantitá intera di litri da caricare, prezzo al L " + g.getPrezzo() + "€", 3, new ArrayList<Integer>(), g));
        domande.get(6).aggiungiSuccessore(7);
        //aggiungo 7
        System.out.println("aggiungo 7");
        domande.add(new Nodo("Il prezzo per ora raggiunto è " + prezzo + "€ digitare una qualsiasi cosa per continuare", 0, new ArrayList<Integer>(), null));
        domande.get(7).aggiungiSuccessore(0);
        //aggiungo 8
        System.out.println("aggiungo 8");
        domande.add(new Nodo("Digitare 1 per eseguire il cambio dell`olio e 2 per eseguire il tagliando", 2, new ArrayList<Integer>(), null));
        domande.get(8).aggiungiSuccessore(9);
        domande.get(8).aggiungiSuccessore(10);
        //aggiungo 9    
        System.out.println("aggiungo 9");
        Olio o = new Olio(12.0);
        domande.add(new Nodo("Cambio dell`olio completato, il prezzo del cambio è " + o.getPrezzo() + "€, mentre il totale è " + prezzo + "€ digitare una qualsiasi cosa per continuare", 1, new ArrayList<Integer>(), o));
        domande.get(9).aggiungiSuccessore(0);
        //aggiungo 10
        System.out.println("aggiungo 10");
        Tagliando t = new Tagliando(45.0);
        domande.add(new Nodo("Tagliando completato, il prezzo del tagliando è " + t.getPrezzo() + "€, mentre il totale è " + prezzo + "€ digitare una qualsiasi cosa per continuare", 2, new ArrayList<Integer>(), t));
        domande.get(10).aggiungiSuccessore(0);
        //aggiungo 11
        System.out.println("aggiungo 11");
        domande.add(new Nodo("Digitare 1 per eseguire il cambio gomme", 3, new ArrayList<Integer>(), null));
        domande.get(11).aggiungiSuccessore(12);
        //aggiungo 12
        System.out.println("aggiungo 12");
        domande.add(new Nodo("Digitare 1 per inserire un set di gomme 4 stagioni,2 per inserire un set di gomme estive e 3 per inserire un set di gomme invernali", 1, new ArrayList<Integer>(), null));
        domande.get(12).aggiungiSuccessore(13);
        domande.get(12).aggiungiSuccessore(14);
        domande.get(12).aggiungiSuccessore(15);
        //aggiungo 13
        System.out.println("aggiungo 13");
        Gomme g1 = new Gomme(125.0);
        domande.add(new Nodo("Gomme 4 stagioni montate e il prezzo è " + g1.getPrezzo() + "€, mentre il totale è " + prezzo + "€ digitare una qualsiasi cosa per continuare", 1, new ArrayList<Integer>(), g1));
        domande.get(13).aggiungiSuccessore(0);
        //aggiungo 14
        System.out.println("aggiungo 14");
        Gomme g2 = new Gomme(90.0);
        domande.add(new Nodo("Gomme estive montate e il prezzo è " + g2.getPrezzo() + "€, mentre il totale è " + prezzo + "€ digitare una qualsiasi cosa per continuare", 2, new ArrayList<Integer>(), g2));
        domande.get(14).aggiungiSuccessore(0);
        //aggiungo 15
        System.out.println("aggiungo 15");
        Gomme g3 = new Gomme(83.0);
        domande.add(new Nodo("Gomme invernali montate e il prezzo è " + g3.getPrezzo() + "€, mentre il totale è " + prezzo + "€ digitare una qualsiasi cosa per continuare", 3, new ArrayList<Integer>(), g3));
        domande.get(15).aggiungiSuccessore(0);
        System.out.println("lunghezza");
        System.out.println(domande.size());
        
    }

    @Override
    public void run() {
        String input = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            //out.println("Digitare ; per chiudere la connessione, premere invio per continuare");
            while (socket.isConnected() || input.equals(";")) {
                grafo();
                System.out.println(domande.get(attuale).getDomanda());
                out.println(domande.get(attuale).getDomanda());
                input = in.readLine();
                System.out.println("RICEVUTO " + input);
                if (input.equals(";")) {
                    socket.close();
                }
                switch (attuale) {
                    //inizio
                    case 0:
                        if (input.equals("4")) {
                            socket.close();
                        } else {
                            try {
                                inserito = Integer.parseInt(input);
                                successivo(inserito, out);
                            } catch (NumberFormatException ex) {
                                out.println("INSERIRE UN VALORE NUMERICO");
                            }
                        }
                        break;
                    //benzinaio
                    case 1:
                        try {
                            inserito = Integer.parseInt(input);
                            successivo(inserito, out);
                        } catch (NumberFormatException ex) {
                            out.println("INSERIRE UN VALORE NUMERICO");
                        }
                        break;
                    //benzina
                    case 2:
                        try {
                            inserito = Integer.parseInt(input);
                            Benzina app = (Benzina) domande.get(attuale).getOggetto();
                            Double molt = app.getPrezzo() * inserito;
                            prezzo += molt;
                            successivo(0, out);
                        } catch (NumberFormatException ex) {
                            out.println("INSERIRE UN VALORE NUMERICO");
                        }
                        break;
                    //output del prezzo e sucessivo
                    case 3:
                        successivo(0, out);
                        break;
                    //disel
                    case 4:
                        try {
                            inserito = Integer.parseInt(input);
                            Disel app = (Disel) domande.get(attuale).getOggetto();
                            Double molt = app.getPrezzo() * inserito;
                            prezzo += molt;
                            successivo(0, out);
                        } catch (NumberFormatException ex) {
                            out.println("INSERIRE UN VALORE NUMERICO");
                        }
                        break;
                    //output del prezzo e sucessivo
                    case 5:
                        successivo(0, out);
                        break;
                    //GPL
                    case 6:
                        try {
                            inserito = Integer.parseInt(input);
                            GPL app = (GPL) domande.get(attuale).getOggetto();
                            Double molt = app.getPrezzo() * inserito;
                            prezzo += molt;
                            successivo(0, out);
                        } catch (NumberFormatException ex) {
                            out.println("INSERIRE UN VALORE NUMERICO");
                        }
                        break;
                    //output del prezzo e sucessivo
                    case 7:
                        successivo(0, out);
                        break;
                    //meccanico
                    case 8:
                        try {
                            inserito = Integer.parseInt(input);
                            successivo(inserito, out);
                            if (inserito == 1) {
                                System.out.println(attuale);
                                Olio app = (Olio) domande.get(attuale).getOggetto();
                                Double molt = app.getPrezzo();
                                prezzo += molt;
                            } else if (inserito == 2) {
                                System.out.println(attuale);
                                Tagliando app = (Tagliando) domande.get(attuale).getOggetto();
                                Double molt = app.getPrezzo();
                                prezzo += molt;
                            }
                        } catch (NumberFormatException ex) {
                            out.println("INSERIRE UN VALORE NUMERICO");
                        }
                        break;
                    //cambio olio
                    case 9:
                        successivo(0, out);
                        break;
                    //tagliando
                    case 10:
                        successivo(0, out);
                        break;
                    //gommista
                    case 11:
                        try {
                            inserito = Integer.parseInt(input);
                            successivo(inserito, out);
                        } catch (NumberFormatException ex) {
                            out.println("INSERIRE UN VALORE NUMERICO");
                        }
                        break;
                    //cambio gomme
                    case 12:
                        try {
                            int appattuale = attuale;
                            inserito = Integer.parseInt(input);
                            successivo(inserito, out);
                            if (attuale == appattuale) {
                                Gomme app = (Gomme) domande.get(attuale).getOggetto();
                                System.out.println("prezzo gomme " + app.getPrezzo());
                                Double molt = app.getPrezzo();
                                prezzo += molt;
                            }
                            System.out.println("prezzo " + prezzo);
                        } catch (NumberFormatException ex) {
                            out.println("INSERIRE UN VALORE NUMERICO");
                        }
                        break;
                    //4stagioni
                    case 13:
                        successivo(0, out);
                        break;
                    //estive
                    case 14:
                        successivo(0, out);
                        break;
                    //invernali
                    case 15:
                        successivo(0, out);
                        break;
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Distributore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
		                            out.println("Il totale da pagare è " + prezzo + "€");
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    private void successivo(int inserito, PrintWriter o) {
        System.out.println("ATTUALE " + attuale);
        ArrayList<Integer> possibili = domande.get(attuale).getSuccessivi();
        for (int i = 0; i < possibili.size(); i++) {
            if (domande.get(possibili.get(i)).getRisposta() == inserito) {
                attuale = possibili.get(i);
                break;
            }
        }
        System.out.println("SUCCESSIVO " + attuale);
    }
}
