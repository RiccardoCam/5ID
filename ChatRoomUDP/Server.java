/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 *
 * @author DYLAN VANZAN
 */
public class Server implements Runnable {

    private MulticastSocket socket;
    private InetAddress multicast;
    private int porta;

    public Server(int porta) {
        this.porta = porta;
        try {
            socket = new MulticastSocket(porta);
            multicast = InetAddress.getByName("224.100.100.1");
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Accensione server...");
        while (true) {
            try {
                byte[] buff = new byte[1024];
                DatagramPacket recivePacket = new DatagramPacket(buff, buff.length);
                socket.receive(recivePacket);
                System.out.println("Messaggio ricevuto");
                byte[] data = recivePacket.getData();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, multicast, porta);
                socket.send(sendPacket);
                System.out.println("Messaggio spedito");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public static void main(String[]args){
        Server s=new Server(4446);
        s.run();
    }
}
