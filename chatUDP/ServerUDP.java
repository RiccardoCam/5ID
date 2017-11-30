package chatudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class ServerUDP implements Runnable {

    private MulticastSocket s;
    private InetAddress ip;
    private final int PORT = 4446;

    public ServerUDP() {
        try {
            s = new MulticastSocket(PORT);
            ip = InetAddress.getByName("224.10.10.1");
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }
    }

    @Override
    public void run() {
        System.out.println("Il server sta funzionando...");
        while (true) {
            try {
                byte[] buff = new byte[1024];
                DatagramPacket recivePacket = new DatagramPacket(buff, buff.length);
                s.receive(recivePacket);
                byte[] data = recivePacket.getData();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip, PORT);
                s.send(sendPacket);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        ServerUDP s = new ServerUDP();
        s.run();
    }
}
