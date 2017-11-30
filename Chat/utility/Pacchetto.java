package utility;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Pacchetto {

    public enum Tipo{
       INIZIO,
       FINE,
       MESSAGGIO,
       LISTA_CONTATTI,
       LOGIN,
       REGISTRAZIONE,
       OK,
       ERRORE,
       DISCONNESSIONE
    }

    private JSONObject jsonObject;
    private Parametri parametri;
    private Tipo tipo;

    public Pacchetto(Tipo tipo,Parametri parametri){
        if(parametri == null)
            parametri = new Parametri();
        jsonObject = new JSONObject();
        try {
            jsonObject.put("tipo", tipo);
            for (String x : parametri) {
                jsonObject.put(x, parametri.get(x));
            }
        }catch (JSONException e){
            System.err.println(e.getMessage());
        }
        this.tipo = tipo;
        this.parametri = parametri;
    }

    public Pacchetto(Tipo tipo){
        this(tipo,null);
    }

    @NotNull
    public static Pacchetto decodificaPacchetto(String datiPacchetto){
        if(datiPacchetto == null)
            return new Pacchetto(Tipo.FINE);
        Parametri parametri = new Parametri();
        Tipo tipoPacchetto;
        try {
            JSONObject jsonObject = new JSONObject(datiPacchetto);
            tipoPacchetto = Tipo.valueOf(jsonObject.getString("tipo"));
            Iterator<String> itChiavi = jsonObject.keys();
            while(itChiavi.hasNext()){
                String chiave = itChiavi.next();
                if(!chiave.equals("tipo"))
                    parametri.put(chiave,jsonObject.getString(chiave));
            }
        } catch (JSONException e) {
            throw new IllegalArgumentException("Pacchetto malformato");
        }
        return new Pacchetto(tipoPacchetto,parametri);
    }

   @Override
   public String toString(){
        return jsonObject.toString();
   }

    public Tipo getTipo() {
        return tipo;
    }

    public Parametri getParametri() {
        return parametri;
    }

    public String getParametro(String chiave){
       return parametri.get(chiave);
    }

    public static class Parametri implements Iterable<String>{

       private Map<String,String> parametri;

       @NotNull
       @Override
       public Iterator<String> iterator() {
           return parametri.keySet().iterator();
       }

       public Parametri(){
          parametri = new HashMap<>();
       }

       public void put(String chiave,String valore){
           parametri.put(chiave,valore);
       }

       public String get(String chiave){
           return parametri.get(chiave);
       }

   }

}
