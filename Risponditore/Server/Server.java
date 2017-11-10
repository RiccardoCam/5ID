package com.tramontini.marco.risponditore.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server{

    private final int PORTA;
    private final int NUMERO_MASSIMO_CLIENT;

    public Server(int porta,int numeroMassimoClient){
        PORTA = porta;
        NUMERO_MASSIMO_CLIENT = numeroMassimoClient;
        avvia();
    }

    public Server(int porta){
        this(porta,15);
    }

    private void avvia(){
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

        private Risponditore risponditore;
        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private int numeroClient;

        private ServerThread(Socket client,int numeroClient){
            this.client = client;
            this.numeroClient = numeroClient;
            try {
                this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                this.out = new PrintWriter(client.getOutputStream());
            }catch (Exception e){
                throw new IllegalStateException("Impossibile comunicare con il client");
            }
            try {
                risponditore = new Risponditore();
            }catch (Exception e){
                System.err.println("Errore creazione risponditore");
            }
        }

        @Override
        public void run(){
            System.out.println("Client "+numeroClient);

            out.println(risponditore.eseguiAzione("CHIEDI_NOME"));
           // out.println("Benvenuto client "+numeroClient);
            out.flush();

            while(true){
                try {
                    String risposta = in.readLine();
                    if(risposta == null)break;
                    out.println(risponditore.eseguiAzione(risposta));
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Client "+numeroClient+" disconnesso");
        }

    }

}
