/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import graph.Graph;
import graph.GraphException;

/**
 *
 * @author Utente
 */
public class Negozio {
    private static final Graph grafo = new Graph();
    
    
    public static Graph inizializeGraph() throws GraphException {
          grafo.addNode("inizio");
          grafo.addNode("fine");
          grafo.addNode("compra");
          grafo.addNode("indietro");
          grafo.addNode("pizza");
          grafo.addNode("dolce");
          grafo.addNode("vino");
          grafo.addNode("margherita");
          grafo.addNode("diavola");
          grafo.addNode("patatosa");
          grafo.addNode("4 stagioni");
          grafo.addNode("tiramisu");
          grafo.addNode("salame al cioccolato");
          grafo.addNode("panna cotta");
          grafo.addNode("barbera");//12,80
          grafo.addNode("pinot nero");//12
          grafo.addNode("brunello"); //32
          
          grafo.addEdge("inizio", "pizza");
          grafo.addEdge("inizio", "dolce");
          grafo.addEdge("inizio", "vino");
          grafo.addEdge("pizza", "margherita");
          grafo.addEdge("pizza", "diavola");
          grafo.addEdge("pizza", "patatosa");
          grafo.addEdge("pizza", "4 stagioni");
          grafo.addEdge("dolce", "tiramisu");
          grafo.addEdge("dolce", "salame al cioccolato");
          grafo.addEdge("dolce", "panna cotta");
          grafo.addEdge("vino", "barbera");
          grafo.addEdge("vino", "pinot nero");
          grafo.addEdge("vino", "brunello");
          grafo.addEdge("pizza", "indietro");
          grafo.addEdge("dolce","indietro");
          grafo.addEdge("vino", "indietro");
          grafo.addEdge("indietro", "pizza");
          grafo.addEdge("indietro", "dolce");
          grafo.addEdge("indietro", "vino");
          grafo.addEdge("diavola", "indietro");
          grafo.addEdge("margherita", "indietro");
          grafo.addEdge("patatosa", "indietro");
          grafo.addEdge("4 stagioni", "indietro");
          grafo.addEdge("tiramisu", "indietro");
          grafo.addEdge("salame al cioccolato", "indietro");
          grafo.addEdge("panna cotta", "indietro");
          grafo.addEdge("barbera", "indietro");
          grafo.addEdge("pinot nero", "indietro");
          grafo.addEdge("brunello", "indietro");
          grafo.addEdge("compra", "indietro");
          grafo.addEdge("compra", "fine");
          grafo.addEdge("margherita", "compra", 4);
          grafo.addEdge("patatosa", "compra", 5.50);
          grafo.addEdge("diavola", "compra", 5.50);
          grafo.addEdge("4 stagioni", "compra", 5.55);
          grafo.addEdge("tiramisu", "compra", 3);
          grafo.addEdge("salame al cioccolato", "compra", 4.50);
          grafo.addEdge("panna cotta", "compra", 2.90);
          grafo.addEdge("barbera", "compra", 12.80);
          grafo.addEdge("pinot nero", "compra", 12);
          grafo.addEdge("brunello", "compra", 32);
        return grafo;
    }
}
