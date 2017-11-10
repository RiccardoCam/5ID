/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risponditore;

import java.util.ArrayList;

/**
 *
 * @author Martorel
 */
class Nodo<E> {
    
    private int risposta;
    private String domanda;
    private ArrayList<Integer> successivi;
    private E oggetto;

    public Nodo(String domanda,int risposta, ArrayList<Integer> successivi, E oggetto) {
        this.domanda = domanda;
        this.successivi = successivi;
        this.oggetto = oggetto;
        this.risposta=risposta;
    }
    
    public void aggiungiSuccessore(int i){
        successivi.add(i);
    }

    public String getDomanda() {
        return domanda;
    }

    public int getRisposta() {
        return risposta;
    }

    public ArrayList<Integer> getSuccessivi() {
        return successivi;
    }

    public E getOggetto() {
        return oggetto;
    }
    
     
}
