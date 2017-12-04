/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomudp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 *
 * @author DYLAN VANZAN
 */
public class Client {

    private MulticastSocket socket;
    private InetAddress indirizzo;
    private final int porta;
    private BufferedReader inSystem;
    private String username;

    public Client(int porta) {
        this.porta = porta;
        try {
            this.socket = new MulticastSocket(4446);
            indirizzo = InetAddress.getByName("224.100.100.1");
            socket.joinGroup(indirizzo);
            inSystem = new BufferedReader(new InputStreamReader(System.in));
            this.username = "";
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void start() {
        try {
            System.out.println("Inserisci username");
            username = inSystem.readLine();
            System.out.println("Ora puoi scrivere un messaggio");
            new Thread(new InThread(inSystem, username)).start();
            new Thread(new OutThread()).start();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    class InThread implements Runnable {

        private byte[] inviaData;
        private DatagramPacket inviaPacchetto;
        private final BufferedReader inSystem;
        private final String username;

        public InThread(BufferedReader inSystem, String username) {
            this.inSystem = inSystem;
            this.username = username;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    String message = username + " : " + inSystem.readLine();

                    inviaData = message.getBytes();
                    inviaPacchetto = new DatagramPacket(inviaData, inviaData.length, indirizzo, porta);
                    socket.send(inviaPacchetto);

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }

        }
    }


    class OutThread implements Runnable {

        private byte[] data;
        private DatagramPacket pacchetto;

        @Override
        public void run() {

            while (true) {
                try {
                    byte add[] = new byte[1024];
                    pacchetto = new DatagramPacket(add, add.length);
                    socket.receive(pacchetto);

                    data = pacchetto.getData();
                    String message = new String(data);
                    System.out.println(message);

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }

        }
    }
    
    public static void main(String[] args) {
        Client c = new Client(4446);
        c.start();
    }
}
