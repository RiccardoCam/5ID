package Server;

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

public class Server {

    private static Graph graph = new Graph();
    private static double costoTotale = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("Negozio aperto...");

        graph = server.Negozio.getInitGraph();
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                // crea il thread e lo lancia
                new Commesso(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Commesso extends Thread {

        private Socket socket;
        private String clientName;

        public Commesso(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String currentNode = "start";
            String outStrm;
            String succ;
            PrintWriter out = null;
            try {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Salve , come si chiama?");
                clientName = in.readLine();
                ArrayList adj = null;
                double weight = 0;
                while (!currentNode.equals("end")) {
                    outStrm = "";
                    out.println("Salve " + clientName + ", desidera? (CONTO € " + costoTotale  +")");
                    try {
                        adj = graph.getAdjList(currentNode);
                        outStrm += "---------- | ";
                        for (Object x : adj) {
                            Edge app = (Edge) x;
                            weight = graph.getEdgeWeight(currentNode, app.getDest());
                            if (weight > 0) {
                                outStrm += app.getDest() + " € " + weight + " | ";
                            } else {
                                outStrm += app.getDest() +  " | ";
                            }
                        }
                        outStrm+=" ----------";
                        out.println(outStrm);
                        succ = in.readLine().toLowerCase();
                        costoTotale += graph.getEdgeWeight(currentNode, succ);
                        currentNode = succ;
                    } catch (GraphException e) {
                        out.println("Non ho capito");
                        currentNode = in.readLine();
                    }
                }
            } catch (IOException e) {
                myLog("Error handling client# " + clientName + ": " + e);
            } finally {
                out.println("Costo totale: € " + costoTotale);
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
