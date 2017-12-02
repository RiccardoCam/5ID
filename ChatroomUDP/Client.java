package chatroomudp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author luca.modolo
 */

public class Client {

    private MulticastSocket socket;
    private InetAddress address;
    private final int port = 4446;
    private BufferedReader msg;
    private String username;

    public Client() throws IOException {
        this.socket = new MulticastSocket(4446);
        address = InetAddress.getByName("224.124.160.6");
        socket.joinGroup(address);
        msg = new BufferedReader(new InputStreamReader(System.in));
        this.username = "";
    }

    public void start() throws IOException {
        System.out.println("Inserire nome Utente");
        username = msg.readLine();
        System.out.println("Ora puoi scrivere a tutti");
        new Thread(new SendMessage(msg, username)).start();
        new Thread(new ReceiveMessage()).start();
    }

    public static void main(String[] args) throws IOException {
        Client c = new Client();
        c.start();
    }

    class SendMessage implements Runnable {

        private byte[] sendData;
        private DatagramPacket sendPacket;
        private final BufferedReader inSystem;
        private final String username;

        public SendMessage(BufferedReader inSystem, String username) {
            this.inSystem = inSystem;
            this.username = username;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String message = username + ": " + inSystem.readLine();
                    sendData = message.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                    socket.send(sendPacket);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    class ReceiveMessage implements Runnable {

        private byte[] data;
        private DatagramPacket recivedPacket;

        @Override
        public void run() {
            while (true) {
                try {
                    byte buff[] = new byte[1024];
                    recivedPacket = new DatagramPacket(buff, buff.length);
                    socket.receive(recivedPacket);
                    data = recivedPacket.getData();
                    String message = new String(data);
                    System.out.println(message);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
