package com.tramontini.marco.risponditore.server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

//Final State Machine
class FSM {

    private String statoAttuale;
    private String parametri;

    private Map<String,Element> stati;

    FSM(File fXmlFile) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document fileAzioni = dBuilder.parse(fXmlFile);
        fileAzioni.getDocumentElement().normalize();
        NodeList listaStati = fileAzioni.getElementsByTagName("state");
        stati = new HashMap<>();
        statoAttuale = "START";

        for(int i=0;i<listaStati.getLength();i++){
            Element statoAttuale = (Element) listaStati.item(i);
            stati.put(statoAttuale.getAttribute("id"),statoAttuale);
        }

    }

    String getStatoAttuale(){
        return statoAttuale;
    }

    String getParametri(){
        return parametri;
    }

     void elabora(String input){
        Element elementStatoAttuale = stati.get(statoAttuale);
        NodeList listaTransizioni = elementStatoAttuale.getElementsByTagName("transition");
        for(int i=0;i<listaTransizioni.getLength();i++){
            Element transizioneAttuale = (Element) listaTransizioni.item(i);
            Pattern pattern = Pattern.compile(transizioneAttuale.getAttribute("input"),
                    Pattern.CASE_INSENSITIVE);
            if(pattern.matcher(input).matches()){
                Pattern patternParametri = Pattern.compile(transizioneAttuale.getAttribute("patternParametri"),
                        Pattern.CASE_INSENSITIVE);
                parametri = patternParametri.matcher(input).replaceAll("");
                statoAttuale = transizioneAttuale.getAttribute("next");
                return;
            }
        }
    }


}
