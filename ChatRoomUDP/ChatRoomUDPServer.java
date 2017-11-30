/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martorel
 */
public class ChatRoomUDPServer implements Runnable {

    private final int PORT = 9999; //PORTA DEDICATA AL SERVER
    private final String MULTICASTIP = "224.236.25.1"; //IP DEL GRUPPO MULTICAST

   // private int PORT;
    private MulticastSocket socket;
    private InetAddress address;

    public ChatRoomUDPServer() {
        try {
            socket = new MulticastSocket(PORT);
            address = InetAddress.getByName(MULTICASTIP);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Server Avviato");
        while (true) {
            try {
                byte[] buff = new byte[2048];
                DatagramPacket recivedPacket = new DatagramPacket(buff, buff.length);
                socket.receive(recivedPacket);
               //System.out.println("ricevuto");
                byte[] data = recivedPacket.getData();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, PORT);
                //System.out.println(new String(data));
                socket.send(sendPacket);
                //System.out.println("inviato");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Thread s = new Thread(new ChatRoomUDPServer());
        s.start();
    }
}
