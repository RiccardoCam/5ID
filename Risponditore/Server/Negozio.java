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
 * @author Cristian
 */
public class Negozio {

    private static final Graph graph = new Graph();

    public static Graph getInitGraph() throws GraphException {
        graph.addNode("start");
        graph.addNode("view t-shirts");
        graph.addNode("view pants");
        graph.addNode("view shoes");
        graph.addNode("polo");
        graph.addNode("gucci");
        graph.addNode("t3");
        graph.addNode("jeans");
        graph.addNode("sweatview pants");
        graph.addNode("yeezy");
        graph.addNode("air max");
        graph.addNode("buy");
        graph.addNode("end");
        graph.addEdge("start", "view t-shirts");
        graph.addEdge("start", "view pants");
        graph.addEdge("start", "view shoes");
        graph.addEdge("start", "end");
        graph.addEdge("polo", "buy", 70);
        graph.addEdge("gucci", "buy", 150);
        graph.addEdge("jeans", "buy", 80);
        graph.addEdge("sweatview pants", "buy", 60);
        graph.addEdge("yeezy", "buy", 220);
        graph.addEdge("air max", "buy", 130);
        graph.addEdge("view t-shirts", "polo");
        graph.addEdge("polo", "view t-shirts");
        graph.addEdge("view t-shirts", "gucci");
        graph.addEdge("gucci", "view t-shirts");
        graph.addEdge("polo", "end");
        graph.addEdge("end", "polo");
        graph.addEdge("gucci", "end");
        graph.addEdge("end", "gucci");
        graph.addEdge("view pants", "jeans");
        graph.addEdge("jeans", "view pants");
        graph.addEdge("view pants", "sweatview pants");
        graph.addEdge("sweatview pants", "view pants");
        graph.addEdge("jeans", "end");
        graph.addEdge("end", "jeans");
        graph.addEdge("sweatview pants", "end");
        graph.addEdge("end", "sweatview pants");
        graph.addEdge("view shoes", "yeezy");
        graph.addEdge("yeezy", "view shoes");
        graph.addEdge("view shoes", "air max");
        graph.addEdge("air max", "view shoes");
        graph.addEdge("yeezy", "end");
        graph.addEdge("end", "yeezy");
        graph.addEdge("air max", "end");
        graph.addEdge("end", "air max");
        graph.addEdge("view t-shirts", "view pants");
        graph.addEdge("view pants", "view t-shirts");
        graph.addEdge("view t-shirts", "view shoes");
        graph.addEdge("view shoes", "view t-shirts");
        graph.addEdge("view shoes", "view pants");
        graph.addEdge("view pants", "view shoes");
        graph.addEdge("view t-shirts", "end");
        graph.addEdge("end", "view t-shirts");
        graph.addEdge("view pants", "end");
        graph.addEdge("end", "view pants");
        graph.addEdge("view shoes", "end");
        graph.addEdge("end", "view shoes");
        return graph;
    }

}
