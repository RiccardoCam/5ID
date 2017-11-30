/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomudp;

import java.io.*;
import java.net.*;
import java.util.logging.*;

/**
 *
 * @author Camillo
 */
public class Server implements Runnable {

    static final int PORT = 4446;
    protected MulticastSocket socket = null;
    InetAddress group;

    public Server() {
        try {
            group = InetAddress.getByName("224.0.1.1");
            socket = new MulticastSocket(PORT);
        } catch (UnknownHostException ex) {
            System.out.println("Errore in Server");
            System.exit(0);
        } catch (IOException ex) {
            System.out.println("Errore in Server");
            System.exit(0);
        }
    }

    @Override
    public void run() {
        System.out.println("Server avviato...");
        while (true) {
            try {
                byte[] buf = new byte[256];
                String dString = "Server: " + getFromClients();
                System.out.println(dString);
                buf = dString.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                socket.send(packet);
                Thread.sleep(2000);
            } catch (IOException ex) {
                System.out.println("Errore in invio");
                System.exit(0);
            } catch (InterruptedException ex) {
                System.out.println("Errore in invio");;
            }
        }
    }

    protected String getFromClients() throws IOException {
        DatagramPacket packet;
        String received="";
        try {
            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            buf = packet.getData();
            received = new String(buf);
            return received;
        } catch (IOException ex) {
            System.out.println("Errore in ricezione");
        }
        return received;
    }
    
    public static void main(String[] args) throws java.io.IOException {
        new Server().run();
    }
}
