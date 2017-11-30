/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author Sandro
 */
public class Server extends Thread {

    private final int PORTA;
    private MulticastSocket socket;

    public Server(int porta) {
        PORTA = porta;
        try {
            socket = new MulticastSocket(PORTA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  
    @Override
    public void run() {
        System.out.println("Server online");
        while (true) {
            try {
                byte[] buff = new byte[1024];
                DatagramPacket recievedPacket = new DatagramPacket(buff, buff.length);
                socket.receive(recievedPacket);
                byte[] data = recievedPacket.getData();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName("224.100.100.1"), PORTA);
                socket.send(sendPacket);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
