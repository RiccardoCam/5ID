/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;
import graph.Graph;
import graph.GraphException;
/**
 *
 * @author Mana
 */
public class Amazon {
    


    private static Graph g = new Graph();
    
    
    public static Graph inizializeGraph() throws GraphException {
        g.addNode("start");
        g.addNode("Offerte del giorno");
        g.addNode("elettronica");
        g.addNode("Sport");
        g.addNode("casa e cucina");
        g.addNode("computer");
        g.addNode("cavo lan");
        g.addNode("usb");
        g.addNode("lavatrice");
        g.addNode("nike");
        g.addNode("samsung");
        g.addNode("apple");
        g.addNode("hp");
        g.addNode("kingston");
        g.addNode("end");

        
        g.addEdge("start", "offerte del giorno");
        g.addEdge("start", "elettronica");
        g.addEdge("start", "sport");
        g.addEdge("start", "casa e cucina");
        g.addEdge("offerte del giorno", "elettronica");
        g.addEdge("offerte del giorno", "sport");
        g.addEdge("offerte del giorno", "casa e cucina");
        g.addEdge("elettronica", "computer");
        g.addEdge("elettronica", "cavo lan");
        g.addEdge("elettronica", "usb");
        g.addEdge("computer", "elettronica");
        g.addEdge("cavo lan", "elettronica");
        g.addEdge("usb", "elettronica");
        g.addEdge("sport", "nike");
        g.addEdge("nike", "sport");
        g.addEdge("casa e cucina", "lavatrice");
        g.addEdge("lavatrice", "casa e cucina");
        g.addEdge("computer ", "samsung");
        g.addEdge("computer", "apple");
        g.addEdge("computer", "hp");
        g.addEdge("samsung", "computer");
        g.addEdge("apple", "computer");
        g.addEdge("hp", "computer");
        g.addEdge("lavatrice", "samsung");
        g.addEdge("samsung", "lavatrice");
        g.addEdge("usb", "kingston");
        g.addEdge("kingston", "usb");
        g.addEdge("nike", "end", 200);
        g.addEdge("samsung", "end", 800);
        g.addEdge("hp", "end", 600);
        g.addEdge("apple", "end",1225);
        g.addEdge("kingston", "end",70);
        g.addEdge("cavo lan", "end",50);
        g.addEdge("start", "end");
        return g;
    }
}

