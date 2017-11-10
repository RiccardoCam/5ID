package com.tramontini.marco.risponditore.server;

import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

class AzioniDefault {

    private static Map<String,Azione> azioni;

    private AzioniDefault(){}

    static Map<String,Azione> getAzioni(){
        azioni = new HashMap<>();

        azioni.put("CHIEDI_NOME", new Azione() {
            @Override
            protected String esegui() {
                return "Ciao, come ti chiami?";
            }
        });

        azioni.put("SALVA_NOME", new Azione() {
            @Override
            protected String esegui() {
                datiUtente.put("nomeUtente",parametri);
                macchinaStatiFiniti.elabora("ATTESA");
                return "Piacere, " + parametri +
                        azioni.get("MESSAGGIO_BENVENUTO").esegui();
            }
        });

        azioni.put("MESSAGGIO_BENVENUTO", new Azione() {
            @Override
            protected String esegui() {
                return "\nEcco alcuni esempi di ciò che puoi chiedermi:" +
                        "\n- Chi è Matteo Renzi?" +
                        "\n- Cos'è un abaco?" +
                        "\n- Che tempo fa oggi?" +
                        "\n- Che tempo farà domani?";
            }
        });

        azioni.put("RICERCA_WIKIPEDIA", new Azione() {
            @Override
            protected String esegui() {
                try {
                    String nomePagina = parametri.trim();
                    nomePagina = nomePagina.replaceAll("^((un|una|uno|il|lo|la|i|gli|le)\\s|(l\\'|un\\'))","").trim();
                    nomePagina = nomePagina.replaceAll("\\p{Punct}","").trim();
                    String url = "https://it.wikipedia.org/w/api.php?action=opensearch&format=json&redirects=return&search="+ URLEncoder.encode(nomePagina, "UTF-8");

                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONArray jsonArray = new JSONArray(response.toString());

                    String descrizione = jsonArray.getJSONArray(2).getString(0);
                    if(descrizione.trim().isEmpty())
                        return "Non sono riuscito a trovare informazioni, puoi essere più preciso?";

                    return descrizione;
                }catch (Exception e){
                    e.printStackTrace();
                    return "Non sono riuscito a trovare informazioni. Sicuro di essere connesso ad internet?";
                }finally {
                    macchinaStatiFiniti.elabora("ATTESA");
                }
            }
        });

        azioni.put("METEO", new Azione() {
            @Override
            protected String esegui() {
                try {

                    if(parametri.trim().isEmpty() || parametri.contains("oggi")){
                        parametri = "oggi";
                    }else{
                        parametri = "domani";
                    }

                    String url = "http://www.arpa.veneto.it/previsioni/it/xml/bollettino_utenti.xml";

                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document doc = documentBuilder.parse(con.getInputStream());
                    doc.getDocumentElement().normalize();

                    NodeList meteogrammi = doc.getElementsByTagName("meteogramma");

                    StringBuilder meteo = new StringBuilder();

                    for(int i=0;i<meteogrammi.getLength();i++){
                        Element meteogramma = (Element) meteogrammi.item(i);
                        String idZona = meteogramma.getAttribute("zoneid");
                        if(idZona.equals("11")){//Venezia
                            NodeList giorni = meteogramma.getElementsByTagName("scadenza");
                            Node giorno = giorni.item((parametri.equals("oggi")?0:1));
                            NodeList dati = ((Element) giorno).getElementsByTagName("previsione");
                            for(int j=0;j<dati.getLength();j++){
                                Element dato = (Element) dati.item(j);
                                switch (dato.getAttribute("title")){
                                    case "Precipitazioni":
                                        meteo.append("Precipitazioni: ");
                                    case "Temperatura":
                                    case "Cielo":
                                        meteo.append(dato.getAttribute("value")+"\n");
                                        break;
                                }
                            }
                        }
                    }

                    if(parametri.equals("oggi"))
                        return "Ecco che tempo fa oggi a Venezia:\n" + meteo;
                    else
                        return "Ecco che tempo farà domani a Venezia:\n"+meteo;
                }catch (Exception e){
                    e.printStackTrace();
                    return "Non sono riuscito a trovare informazioni. Sicuro di essere connesso ad internet?";
                }finally {
                    macchinaStatiFiniti.elabora("ATTESA");
                }
            }
        });

        azioni.put("MIO_NOME", new Azione() {
            @Override
            protected String esegui() {
                macchinaStatiFiniti.elabora("ATTESA");
                return "Puoi chiamarmi come preferisci, "+datiUtente.get("nomeUtente");
            }
        });

        azioni.put("SALUTO", new Azione() {
            @Override
            protected String esegui() {
                macchinaStatiFiniti.elabora("ATTESA");
                return "Ciao, "+datiUtente.get("nomeUtente");
            }
        });

        azioni.put("SPEGNI", new Azione() {
            @Override
            protected String esegui() {
                macchinaStatiFiniti.elabora("END");
                return "Ok, come preferisci. A presto!";
            }
        });

        return azioni;
    }

}
