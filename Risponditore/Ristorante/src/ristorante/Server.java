/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ristorante;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import static ristorante.Ristorante.*;

/**
 *
 * @author Alvise
 */
public class Server {

    public static class Ordinazione implements Runnable {

        private Socket socket;
        private int numeroCliente;
        private final String RIPETI = "non ho capito, potresti ripetere?";
        private final String TOTALE = "per che ora ha intenzione di venire a ritirare l'ordinazione?";
        

        public Ordinazione(Socket socket, int clientNumber) {
            this.socket = socket;
            this.numeroCliente = clientNumber;
            myLog("Connessione con il cliente numero " + clientNumber + " all'indirizzo: " + socket.getInetAddress());
        }

        @Override
        public void run() {
            String nomeCliente;
            String domCorr="", ultimaDomanda;
            String app;
            boolean tot = false;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Ristorante r = new Ristorante();
                ultimaDomanda = getUltimaDomanda();
                out.println(ultimaDomanda);
                out.println("Buonasera ristorante giapponese mi dica il suo nome cortesemente");
                nomeCliente = in.readLine();
                out.println(nomeCliente);
                
                domCorr = getPrimaDomanda();
                out.println(nomeCliente + " " + domCorr);

                while (true) {
                    String input = in.readLine();
                    app=domCorr;
                    domCorr = getProssimaDomanda(domCorr, input);
                    if (!tot) out.println(nomeCliente + " " + domCorr);
                    else out.println(nomeCliente + " " + domCorr);
                    
                    if (domCorr.equals(TOTALE)) tot=true;
                    if (domCorr.equals(ripetizione))domCorr=app;
                    if (domCorr.equals(ultimaDomanda)) {
                        if(tot) out.println("Il totale è di "+ getCostoTotale() + "€");
                        else out.println("");
                        setCostoTotale(0);
                        tot=false;
                        socket.close();
                        break;
                    }
                }

            } catch (IOException e) {
                myLog("Errore con il cliente numero " + numeroCliente);
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    myLog("Errore con il cliente numero " + numeroCliente);
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
                }
                myLog("Connessione con il cliente numero " + numeroCliente + " chiusa");
            }

        }

        private void myLog(String message) {
            System.out.println(message);
        }

    }

    public static void main(String[] args) throws Exception {
        System.out.println("Stai per essere messo in linea.");
        int idCliente = 0;
        ServerSocket listener = new ServerSocket(8080);
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            while (true) {
                executor.submit(new Ordinazione(listener.accept(), idCliente));
                idCliente++;
            }
        } finally {
            listener.close();
        }

    }

}
