/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risponditorebar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author andrea zoccarato
 */
public class Bar implements Runnable {

    private final ArrayList<Pair<String, Double>> bevandeMaggiorenni;
    private final ArrayList<Pair<String, Double>> bevandeMinorenni;
    private final ArrayList<String> gustiGelato;
    private final ArrayList<String> gustiTramezzini;
    private double prezzoPatatine;
    private double prezzoGelato;
    private double prezzoTramezzino;
    private Double prezzo;
    private String nome;
    private int età;
    private Socket socket;
    
    public static final String ERROR1="Risposta non valida";
    public static final String ERROR2="Bevanda non valida";
    public static final String ERROR3="Gusto non valido";

    private Map<Integer, String> domande;

    public Bar(Socket socket) {
        
        this.socket=socket;
        this.bevandeMaggiorenni = new ArrayList<>();
        this.bevandeMinorenni = new ArrayList<>();
        this.gustiGelato = new ArrayList<>();
        this.gustiTramezzini = new ArrayList<>();
        this.prezzo = 0.0;
        this.nome = "";
        this.età = 0;
        this.domande = new HashMap<>();
        this.prezzoPatatine=2.0;
        this.prezzoGelato=1.30;
        this.prezzoTramezzino=2.0;
        impostaMappa();
    }
    
    private void impostaMappa(){
        domande.put(0, "Come ti chiami?");
        domande.put(1, "Quanti anni hai?");
        domande.put(2, "Desideri da bere?");
        domande.put(3, "Cosa desideri da bere?");
        domande.put(4, "Altro da bere?");
        domande.put(5, "Desideri da mangiare?");
        domande.put(6, "Gelato?");
        domande.put(7, "Patatine??");
        domande.put(8, "Tramezzino?");
        domande.put(9, "Altro da mangiare?");
        bevandeMaggiorenni.add(new Pair<>("caffè",1.20));
        bevandeMaggiorenni.add(new Pair<>("acqua",0.20));
        bevandeMaggiorenni.add(new Pair<>("birra",2.00));
        bevandeMaggiorenni.add(new Pair<>("vino",3.50));
        bevandeMaggiorenni.add(new Pair<>("spritz",2.50));
        bevandeMaggiorenni.add(new Pair<>("succo",1.50));
        bevandeMinorenni.add(new Pair<>("succo",1.50));
        bevandeMinorenni.add(new Pair<>("acqua",0.20));
        gustiGelato.add("amarena");
        gustiGelato.add("pistacchio");
        gustiGelato.add("nocciola");
        gustiGelato.add("cioccolato");
        gustiGelato.add("banana");
        gustiGelato.add("fragola");
        gustiGelato.add("stracciatella");
        gustiTramezzini.add("tonno");
        gustiTramezzini.add("tonno cipolle");
        gustiTramezzini.add("prosciutto funghi");
        gustiTramezzini.add("sapore di mare");
    }

    private boolean contains(String nome, ArrayList<Pair<String, Double>> a) {
        for (int i = 0; i < a.size(); i++) {
            if(a.get(i).getKey().equals(nome))return true;
        }
        return false;
    }

    public int getPos(String x, ArrayList<Pair<String, Double>> arr) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getKey().equals(x)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void run() {
        String input = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            for (int i = 0; i < domande.size(); i++) {
                out.println(domande.get(i));
                input = in.readLine().toLowerCase();
                switch (i){
                    //Come ti chiami?
                    case 0:
                        this.nome=input;
                        break;
                    //Quanti anni hai?
                    case 1:
                        this.età=Integer.parseInt(input);
                        break;
                    //Desideri da bere
                    case 2:
                        if(input.equals("si"))break;
                        else if(input.equals("no"))i=4;
                        else{
                            //err.println(ERROR1);
                            i--;
                        }
                        break;
                    //Cosa desideri da bere
                    case 3:
                        if(età>=18){
                            if(contains(input,this.bevandeMaggiorenni)){
                                prezzo+=this.bevandeMaggiorenni.get(getPos(input,bevandeMaggiorenni)).getValue();
                            }else{
                                //err.println(ERROR2);
                                i--;
                            }
                        }else{
                            if(contains(input,this.bevandeMinorenni)){
                                prezzo+=this.bevandeMinorenni.get(getPos(input,bevandeMinorenni)).getValue();
                            }else{
                                //err.println(ERROR2);
                                i--;
                            }
                        }
                        break;
                    //Altro da bere?
                    case 4:
                        if(input.equals("si"))i=2;
                        else if(input.equals("no"))break;
                        else{
                           // err.println(ERROR1);
                            i--;
                        }
                    //Desideri da mangiare?
                    case 5:
                        if(input.equals("si"))break;
                        else if(input.equals("no"))i=9;
                        else{
                            //err.println(ERROR1);
                            i--;
                        }
                    //Gelato?
                    case 6:
                        if(input.equals("si")){
                            out.println("Che gusto?");
                            String gusto=in.readLine();
                            while(!gustiGelato.contains(gusto)){
                                    //out.println("ERROR3");
                                    out.println("Che gusto?");
                                    gusto=in.readLine();
                            }
                            prezzo+=prezzoGelato;
                        }else if(input.equals("no"))break;
                        else{
                            //err.println(ERROR1);
                            i--; 
                        }
                        break;
                    //Patatine
                    case 7:
                        if(input.equals("si")){
                            prezzo+=prezzoPatatine;
                            break;
                        }
                        else if(input.equals("no"))break;
                        else{
                            //err.println(ERROR1);
                            i--;
                        }
                    //Tramezzino?  
                    case 8:
                        if(input.equals("si")){
                           out.println("Che gusto?");
                            String gusto=in.readLine();
                            while(!gustiTramezzini.contains(gusto)){
                                    //err.println(ERROR3);
                                    out.println("Che gusto?");
                                    gusto=in.readLine();
                            }
                            prezzo+=prezzoTramezzino;
                        break;
                        }else if(input.equals("no"))break;
                        else{
                            //err.println(ERROR1);
                            i--; 
                        }
                        break;
                    //Altro da mangiare?
                    case 9:
                        if(input.equals("si")){
                            i=5;
                            break;
                        }
                        else if(input.equals("no"))break;
                        else{
                           // err.println(ERROR1);
                            i--; 
                        }
                        break;
                }
            }
            out.println("prezzo: " + this.prezzo);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
