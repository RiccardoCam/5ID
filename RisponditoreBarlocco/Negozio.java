
package server;

import graph.Graph;
import graph.GraphException;

public class Negozio {
    private static Graph g = new Graph();
    
    
    public static Graph inizializeGraph() throws GraphException {
        g.addNode("start");//creazione nodi grafi
        g.addNode("giocatore");
        g.addNode("scarpe");
        g.addNode("parastinchi");
        g.addNode("portiere");
        g.addNode("protezioni");
        g.addNode("guanti");
        g.addNode("end");
        g.addNode("nike");
        g.addNode("adidas");
        g.addNode("carbonio");
        g.addNode("plastica");
        g.addNode("mcdavid");
        g.addNode("errea");
        g.addNode("reush");
        g.addNode("uhlsport");
        
        g.addEdge("start", "giocatore");//creazioni archi grafi
        g.addEdge("start", "portiere");
        g.addEdge("giocatore", "scarpe");
        g.addEdge("giocatore", "parastinchi");
        g.addEdge("scarpe", "nike");
        g.addEdge("scarpe", "adidas");
        g.addEdge("nike", "scarpe");
        g.addEdge("adidas", "scarpe");
        g.addEdge("parastinchi", "carbonio");
        g.addEdge("parastinchi", "plastica");
        g.addEdge("plastica", "parastinchi");
        g.addEdge("carbonio", "parastinchi");
        g.addEdge("portiere", "protezioni");
        g.addEdge("portiere", "guanti");
        g.addEdge("protezioni", "mcdavid");
        g.addEdge("protezioni", "errea");
        g.addEdge("mcdavid", "protezioni");
        g.addEdge("errea", "protezioni");
        g.addEdge("guanti", "reush");
        g.addEdge("guanti", "uhlsport");
        g.addEdge("reush", "guanti");
        g.addEdge("uhlsport", "guanti");
        g.addEdge("scarpe", "giocatore");
        g.addEdge("scarpe", "portiere");
        g.addEdge("parastinchi", "giocatore");
        g.addEdge("parastinchi", "portiere");
        g.addEdge("protezioni", "portiere");
        g.addEdge("protezioni", "giocatore");
        g.addEdge("guanti", "portiere");
        g.addEdge("guanti", "giocatore");
        g.addEdge("nike", "end", 200);//aggiungo come costo, il prezzo degli articoli
        g.addEdge("adidas", "end", 150);
        g.addEdge("carbonio", "end", 90);
        g.addEdge("plastica", "end", 25);
        g.addEdge("mcdavid", "end",70);
        g.addEdge("errea", "end",50);
        g.addEdge("reush", "end",105);
        g.addEdge("uhlsport", "end",50);
        g.addEdge("start", "end");
        return g;
    }
}
