package com.tramontini.marco.risponditore.server;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

class Risponditore {

    private FSM macchinaStatiFiniti;

    private Map<String,Azione> azioni = new HashMap<>();

    private final Map<String,String> datiUtente = new HashMap<>();

    Risponditore() throws Exception{
        inizializzaAzioniDefault();
        macchinaStatiFiniti = new FSM(new File("stati.xml"));
    }

    private void inizializzaAzioniDefault() throws Exception{
        azioni = AzioniDefault.getAzioni();
    }

    public String eseguiAzione(String rispostaClient){
        String statoPrecedente = macchinaStatiFiniti.getStatoAttuale();
        macchinaStatiFiniti.elabora(rispostaClient);
        String statoAttuale = macchinaStatiFiniti.getStatoAttuale();

        if(statoAttuale.equals("END"))
            return "Non pretendere che ti risponda dopo che mi hai spento...";

        if(statoAttuale.equals(statoPrecedente))
            return "Non ho capito, puoi spiegarti meglio?";

        Azione azioneDaEseguire = azioni.get(statoAttuale);

        if(azioneDaEseguire == null)
            return "Purtroppo non so cosa risponderti...";

        azioneDaEseguire.setMacchinaStatiFiniti(macchinaStatiFiniti);
        azioneDaEseguire.setDatiUtente(datiUtente);
        new Thread(azioneDaEseguire).start();
        return azioneDaEseguire.getRisultato();
    }



}
