/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author Alvise
 */
public class Server implements Runnable {

    private final int PORTA = 4446;
    private MulticastSocket socket;

    public Server() throws IOException {
        socket = new MulticastSocket(PORTA);
    }

    @Override
    public void run() {
        System.out.println("Server in esecuzione");
        while (true) {
            try {
                byte[] messaggio = new byte[1024];
                DatagramPacket rPacket = new DatagramPacket(messaggio, messaggio.length);
                socket.receive(rPacket);
                socket.send(new DatagramPacket(rPacket.getData(), rPacket.getData().length, InetAddress.getByName("224.100.100.1"), PORTA));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server s = new Server();
        s.run();
    }

}
