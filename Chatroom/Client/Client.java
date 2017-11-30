package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Client {

    private MulticastSocket socket;
    private InetAddress address;
    private int porta;

    public Client(int portaServer) throws IOException {
       this.porta = portaServer;
       this.socket = new MulticastSocket(portaServer);
       address = InetAddress.getByName("224.236.25.1");
       socket.joinGroup(address);
    }

    public void invia(String username, String messaggio){
        try {
            String message = "[ " + username + " ]" + messaggio;
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, porta);
            socket.send(packet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String  ricevi(){
        try {
            byte buff[] = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buff, buff.length);
            socket.receive(packet);

            byte[] data = packet.getData();
            String message = new String(data);
            System.out.println(message);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
