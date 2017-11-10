/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risponditore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Martorel
 */
public class RisponditoreServer {

    /**
     * @param args the command line arguments
     */
    //variabile alla quale sarà assegnato il server
    protected ServerSocket server;

    //costruttore nel quale assegno il socket con la connessione alla variabile di classe
    public RisponditoreServer(ServerSocket connection) {
        server = connection;
    }

    public void avvia(){
        //si ipotizzano un massimo di 6 "impiegati" disposti ad aiutare il cliente
        ExecutorService impiegati = Executors.newFixedThreadPool(6);
        //comunico che il server è operativo
        System.out.println("IL DISTRIBUTORE È APERTO");
        //fincè il server non è chiuso
        while(!server.isClosed()){
            //preparo la connessione al server
            Socket cliente = null;
            try{
                //provo ad accettare la connessione
                cliente=server.accept();
            }catch(IOException e){
                //se non riesco controllo se il server è chiusto
                if (server.isClosed()) {
                    System.out.println("IL DISTRIBUTORE HA CHIUSO");
                    break;
                    // in caso contrario comunico un errore nell`accetare la richiesta
                }else throw new RuntimeException("ERRORE NELL'ACCETTARE IL CLIENTE",e);
                //assegno ad un impiegato il cliente
            }impiegati.execute(new Distributore(cliente));
        }
        //in caso di uscita dal ciclo chiudo tutto e lo comunico
        impiegati.shutdown();
        System.out.println("IL DISTRIBUTORE HA CHIUSO");
    }

    public static void main(String[] args) throws IOException{
        //creo un istanza della classe RisponditoreServer che contiene il socket pronto ad avviare la connessione 
        RisponditoreServer server = new RisponditoreServer(new ServerSocket(8080));
        //avvio il tutto
        server.avvia();
    }

}
