package Server;


import javafx.util.Pair;
import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import utility.Pacchetto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server extends Thread{

    private final int PORTA;
    private final int NUMERO_MASSIMO_CLIENT;
    private Map<Integer,Pair<BufferedReader,PrintWriter>> clientConnessi;
    private Map<String,Integer> utenti;

    public Server(int porta,int numeroMassimoClient){
        PORTA = porta;
        NUMERO_MASSIMO_CLIENT = numeroMassimoClient;
        clientConnessi = new HashMap<>();
        utenti = new HashMap<>();
    }

    public Server(int porta){
        this(porta,50);
    }

    private synchronized void aggiungiClient(int numeroClient,BufferedReader in,PrintWriter out){
        clientConnessi.put(numeroClient,new Pair<>(in,out));
    }

    private synchronized void aggiungiUtente(String utente,int numeroClient){
        utenti.put(utente,numeroClient);
        aggiornaListaUtenti();
    }

    private synchronized void rimuoviClient(int numeroClient){
        clientConnessi.remove(numeroClient);
    }

    private synchronized void rimuoviUtente(String utente){
        utenti.remove(utente);
        aggiornaListaUtenti();
    }

    @Contract(pure = true)
    private synchronized boolean isUtenteConnesso(String username){
        return utenti.containsKey(username);
    }

    private void aggiornaListaUtenti(){
        JSONArray listaUtenti = new JSONArray();
        for(String x: utenti.keySet()){
            listaUtenti.put(x);
        }
        Pacchetto.Parametri parametri = new Pacchetto.Parametri();
        parametri.put("lista",listaUtenti.toString());
        Pacchetto pacchetto = new Pacchetto(Pacchetto.Tipo.LISTA_CONTATTI,parametri);
        for(int x: utenti.values()){
            clientConnessi.get(x).getValue().println(pacchetto);
        }
    }

    @Override
    public void run(){
        ServerSocket server=null;
        try
        {
            server = new ServerSocket(PORTA);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Server in ascolto sulla porta "+PORTA);

        ExecutorService executor= Executors.newFixedThreadPool(NUMERO_MASSIMO_CLIENT);
        try
        {
            int numeroAttualeClient=1;
            for(;;) {
                ServerThread serverThread = new ServerThread(server.accept(), numeroAttualeClient++);
                executor.execute(serverThread);
                aggiungiClient(serverThread.numeroClient,serverThread.in,serverThread.out);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException ignored) {}
            System.out.println("Server spento");
        }
    }

    private class ServerThread extends Thread{

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private int numeroClient;

        private SessioneChat sessione;

        private ServerThread(Socket client,int numeroClient){
            this.client = client;
            this.numeroClient = numeroClient;
            try {
                this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                this.out = new PrintWriter(client.getOutputStream(),true);
            }catch (Exception e){
                throw new IllegalStateException("Impossibile comunicare con il client");
            }
        }

        @Override
        public void run(){
            try {
                while (Pacchetto.decodificaPacchetto(in.readLine()).getTipo() != Pacchetto.Tipo.INIZIO);//Attendo il messaggio di inizio
                System.out.println("il client "+numeroClient+" ha iniziato una nuova sessione di chat");
                sessione = new SessioneChat();
            }catch (IOException e){
                return;
            }
           for(;;){
               try {
                   Pacchetto pacchettoRicevuto = Pacchetto.decodificaPacchetto(in.readLine());
                   switch (pacchettoRicevuto.getTipo()){
                       case DISCONNESSIONE:
                           return;
                       case FINE:
                           sessione.aggiornaUltimoAccesso();
                           System.out.println("Il client " + numeroClient + " si è disconnesso");
                           try {
                               rimuoviClient(utenti.get(sessione.getUsername()));
                               rimuoviUtente(sessione.getUsername());
                           }catch (Exception ignored){}
                           return;//Termino l'esecuzione del thread
                       case LOGIN:
                           String username = pacchettoRicevuto.getParametro("username");
                           if(sessione.login(username,pacchettoRicevuto.getParametro("password"))){
                                out.println(new Pacchetto(Pacchetto.Tipo.OK));
                                if(isUtenteConnesso(username)){
                                    clientConnessi.get(utenti.get(username)).getValue().println(new Pacchetto(Pacchetto.Tipo.DISCONNESSIONE));
                                }
                                aggiungiUtente(username,numeroClient);
                                out.println(new Pacchetto(Pacchetto.Tipo.OK));
                                System.out.println("Il client "+numeroClient+" ha effettuato il login con username '"+username+"'");
                           }else{
                               Pacchetto.Parametri parametri = new Pacchetto.Parametri();
                               parametri.put("errore","Credenziali errate");
                               out.println(new Pacchetto(Pacchetto.Tipo.ERRORE,parametri));
                           }
                           break;
                       case REGISTRAZIONE:
                           username = pacchettoRicevuto.getParametro("username");
                           if(sessione.registrazione(username,pacchettoRicevuto.getParametro("password"))){
                               System.out.println("Il client "+numeroClient+" ha effettuato la registrazione con username '"+username+"'");
                               out.println(new Pacchetto(Pacchetto.Tipo.OK));
                           }else{
                               Pacchetto.Parametri parametri = new Pacchetto.Parametri();
                               parametri.put("errore","Username non disponibile");
                               out.println(new Pacchetto(Pacchetto.Tipo.ERRORE,parametri));
                           }
                           break;
                       case MESSAGGIO:
                           String mittente = pacchettoRicevuto.getParametro("mittente");
                           String destinatario = pacchettoRicevuto.getParametro("destinatario");
                           String testoMessaggio = pacchettoRicevuto.getParametro("testo");
                           clientConnessi.get(utenti.get(destinatario)).getValue().println(pacchettoRicevuto);
                           break;
                   }
               } catch (IOException e) {
                   System.err.println("Errore durante la lettura del pacchetto: "+e.getMessage());
               }
           }
        }

        @Override
        public void finalize() throws IOException {
            client.close();
        }

    }

}
