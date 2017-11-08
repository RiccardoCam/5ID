/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import graph.Edge;
import graph.Graph;
import graph.GraphException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Utente
 */
public class Server {
    
    private static Graph grafo = new Graph();
    
     public static void main(String[] args) throws Exception {
     System.out.println("Negozio start...");

        
        grafo = server.Negozio.inizializeGraph();
        try (ServerSocket listener = new ServerSocket(9898)) {
            while (true) {
                // crea il thread e lo lancia
                new Commesso(listener.accept()).start();
            }
        }
     }
     
     private static class Commesso extends Thread {

        private final Socket socket;
        private String clientName;

        public Commesso(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String currentNode = "inizio";
            String outStrm;
            String succ;
            PrintWriter out = null;
            double costoTotale=0;
            try {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Buongiorno , come si chiama?");
                clientName = in.readLine();
                ArrayList adj = null;
                double weight = 0;
                while (!currentNode.equals("fine")) {
                    outStrm = "";
                    out.println("Buonasera " + clientName + ", cosa desidera? " + costoTotale);
                    try {
                        adj = grafo.getAdjList(currentNode);
                        outStrm += "*** - ";
                        for (Object x : adj) {
                            Edge app = (Edge) x;
                            weight = grafo.getEdgeWeight(currentNode, app.getDest());
                            costoTotale+=weight;
                            if (weight > 0) {
                                outStrm += app.getDest() + " € --> " + weight;
                            } else {
                                outStrm += app.getDest() +  " <> ";
                            }
                        }
                        outStrm+=" - ***";
                        out.println(outStrm);
                        succ = in.readLine().toLowerCase();
                        currentNode = succ;
                    } catch (GraphException e) {
                        out.println("Articolo inesistente!:(");
                        currentNode = in.readLine();
                    }
                }
            } catch (IOException e) {
                myLog("Error handling client# " + clientName + ": " + e);
            } finally {
                out.println("Arrivederci e grazie!");
                try {
                    socket.close();
                } catch (IOException e) {

                }
                myLog("Connection with client# " + clientName + " closed");
            }

        }

        private void myLog(String message) {
            System.out.println(message);
        }
    }
}
