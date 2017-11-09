package risponditore;


import grafo.GestioneGrafo;
import grafo.Nodo;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Filippo
 */
public class Banca {
    private GestioneGrafo<String> domande;
    
    public Banca() {
        domande=new GestioneGrafo<>();
        domande.addNodo(new Nodo<String>(" Buongiorno gentile Cliente, vuole avere informazioni su questa banca (info) o vuole accedere (login)?")); //0
        domande.addNodo(new Nodo<String>(" Questa banca è nata nel 1925 ed il suo fondatore è Carlo Zuccante. Vuole Proseguire?"));//1
        domande.addNodo(new Nodo<String>(" Lei è gia' registrato?"));//2
        domande.addNodo(new Nodo<String>(" Vuole accedere?"));//3
        domande.addNodo(new Nodo<String>(" Vuole registrasi?"));//4
        domande.addNodo(new Nodo<String>(" Inserisca ora il suo NOMEUTENTE e la sua PASSWORD separate da uno spazio"));//5
        domande.addNodo(new Nodo<String>(" Inserisca ora il NOMEUTENTE che vuole avere e la PASSWORD  ed il SALDO separate da uno spazio"));//6
        
        domande.addNodo(new Nodo<String>("FINE"));//7
        
        domande.addArco(domande.nodi.get(0), domande.nodi.get(1));//no
        domande.addArco(domande.nodi.get(0), domande.nodi.get(2));//si
        domande.addArco(domande.nodi.get(0), domande.nodi.get(0));//invalito
        
        domande.addArco(domande.nodi.get(1), domande.nodi.get(7));
        domande.addArco(domande.nodi.get(1), domande.nodi.get(2));
         domande.addArco(domande.nodi.get(1), domande.nodi.get(1));//invalito
        
        domande.addArco(domande.nodi.get(2), domande.nodi.get(4));//no
        domande.addArco(domande.nodi.get(2), domande.nodi.get(3));
        domande.addArco(domande.nodi.get(2), domande.nodi.get(2));
        
        domande.addArco(domande.nodi.get(3), domande.nodi.get(7));
        domande.addArco(domande.nodi.get(3), domande.nodi.get(5));
        domande.addArco(domande.nodi.get(3), domande.nodi.get(3));
        
        domande.addArco(domande.nodi.get(4), domande.nodi.get(7));
        domande.addArco(domande.nodi.get(4), domande.nodi.get(6));
        domande.addArco(domande.nodi.get(4), domande.nodi.get(4));
        
        domande.addArco(domande.nodi.get(5), domande.nodi.get(5));
        
        domande.addArco(domande.nodi.get(6), domande.nodi.get(6));
        domande.addArco(domande.nodi.get(6), domande.nodi.get(3));
        
        ArrayList<String> rispostePos=new ArrayList<>();
         rispostePos.add("SI");
        rispostePos.add("VA BENE");
        rispostePos.add("GIUSTO");
        rispostePos.add("OK");
        
        ArrayList<String> risposteNeg=new ArrayList<>();
        risposteNeg.add("NO");
        risposteNeg.add("NOT");
        
        ArrayList<String> rispostePrimoPos=new ArrayList<>();
        rispostePrimoPos.add("INFO");
        
        ArrayList<String> rispostePrimoNeg=new ArrayList<>();
        rispostePrimoNeg.add("LOGIN");
        
        ArrayList<String> acc=new ArrayList<>();
        acc.add("OK");
        
        domande.listaArchi.get(0).get(0).informazioni=rispostePrimoPos;
        domande.listaArchi.get(0).get(1).informazioni=rispostePrimoNeg;
        
        domande.listaArchi.get(1).get(0).informazioni=risposteNeg;
        domande.listaArchi.get(1).get(1).informazioni=rispostePos;
        
        domande.listaArchi.get(2).get(0).informazioni=risposteNeg;
        domande.listaArchi.get(2).get(1).informazioni=rispostePos;
        
         domande.listaArchi.get(3).get(0).informazioni=risposteNeg;
        domande.listaArchi.get(3).get(1).informazioni=rispostePos;
        
         domande.listaArchi.get(4).get(0).informazioni=risposteNeg;
        domande.listaArchi.get(4).get(1).informazioni=rispostePos;
        domande.listaArchi.get(6).get(1).informazioni=acc;
        domande.listaArchi.get(6).get(0).informazioni=risposteNeg;
        
    }

    public GestioneGrafo<String> getDomande() {
        return domande;
    }
    
}
    