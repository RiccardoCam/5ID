package com.tramontini.marco.risponditore.server;

import java.util.Map;
import java.util.concurrent.Semaphore;

public abstract class Azione implements Runnable{

    private String risultato;
    private Semaphore semaphore = new Semaphore(0);
    protected String parametri = "";
    protected Map<String,String> datiUtente;
    protected FSM macchinaStatiFiniti;

    Azione(){
    }

     String getRisultato(){
        try {
            semaphore.acquire();
        } catch (InterruptedException ignored) {}
        return risultato;
    }

    void setMacchinaStatiFiniti(FSM macchinaStatiFiniti){
        this.parametri = macchinaStatiFiniti.getParametri();
        this.macchinaStatiFiniti = macchinaStatiFiniti;
    }

    void setDatiUtente(Map<String,String> datiUtente){
         this.datiUtente = datiUtente;
    }

    protected abstract String esegui();

    @Override
    public void run() {
        risultato = esegui();
        semaphore.release();
    }
}
