/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomudp;

import java.io.*;
import java.net.*;

/**
 *
 * @author Camillo
 */
public class Client {

    private MulticastSocket socket;
    private final int port = 4446;
    protected static InetAddress address;
    private String nomeUtente;
    private BufferedReader tastiera;
    private boolean inside;//diventa false per terminare il tutto;

    public Client() throws IOException {
        inside=true;
        socket = new MulticastSocket(port);
        address = InetAddress.getByName("224.0.1.1");
        socket.joinGroup(address);
        tastiera = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Nome utente da utilizzare:");
        nomeUtente = tastiera.readLine();
        System.out.println("Ora fai parte del Gruppo");
        Thread cI = new Thread(new ChatIn());
        Thread cO = new Thread(new ChatOut());
        cI.start();
        cO.start();
    }

    public static void main(String[] args) throws IOException {
        Client c = new Client();
    }

//_________________________________________________________
    class ChatIn implements Runnable {

        public ChatIn() throws IOException {
        }

        @Override
        public void run() {
            DatagramPacket packet;
            while (inside) {
                try {
                    byte[] buf = new byte[256];
                    packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    buf = packet.getData();
                    String received = new String(buf);
                    if (!received.contains(nomeUtente)) {
                        System.out.println(received);
                    }
                } catch (IOException ex) {
                    System.out.println("Errore in ricezione");
                }
            }
        }
    }

//______________________________________________________________
    class ChatOut implements Runnable {

        private byte[] buf;

        public ChatOut() throws IOException {
            buf = new byte[256];
        }

        public void run() {
            DatagramPacket packet;
            while (inside) {
                try {
                    String s=tastiera.readLine();
                    if(s.equals("exit")){
                        System.out.println("SESSIONE TERMINATA");
                        inside=false;
                    }
                    String ris = "FROM__" + nomeUtente + ": " +s ;
                    buf = ris.getBytes();
                    packet = new DatagramPacket(buf, buf.length, address, port);
                    socket.send(packet);

                } catch (IOException ex) {
                    System.out.println("Errore in invio");
                }
            }
        }
    }
}
