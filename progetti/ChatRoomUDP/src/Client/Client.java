package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author Sandro
 */
public class Client {

    private MulticastSocket socket;
    private InetAddress address;
    private int porta;

    public Client(int porta) throws IOException {
        this.porta = porta;
        this.socket = new MulticastSocket(porta);
        address = InetAddress.getByName("224.100.100.1");
        socket.joinGroup(address);
    }

    public void inviaMex(String username, String messaggio) {
        try {
            String message = "[ " + username + " ]: " + messaggio;
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, porta);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String riceviMex() {
        try {
            byte buff[] = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buff, buff.length);
            socket.receive(packet);

            byte[] data = packet.getData();
            String messaggio = new String(data);
            System.out.println(messaggio);
            return messaggio;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
