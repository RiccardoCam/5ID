package com.tramontini.marco.risponditore.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread{

    private Socket server;
    private BufferedReader in;
    private PrintWriter out;
    private final InetAddress INDIRIZZO_IP;
    private final int PORTA;

    public Client(String indirizzoIp,int porta) throws IOException{
        INDIRIZZO_IP = InetAddress.getByName(indirizzoIp);
        PORTA = porta;
        server = new Socket(INDIRIZZO_IP, porta);
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            out = new PrintWriter(server.getOutputStream(), true);
        }catch (Exception e){
            throw new IllegalStateException("Impossibile comunicare con il server");
        }
    }

    public void invia(String messaggio){
        out.println(messaggio);
        out.flush();
    }

    public String ricevi(){
        try {
            return in.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return "";
    }

}
