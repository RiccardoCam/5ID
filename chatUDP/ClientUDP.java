package chatudp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class ClientUDP {

    private MulticastSocket s;
    private InetAddress ip;
    private BufferedReader inTast;
    private String nome;
    private final int PORT = 4446;

    public ClientUDP() {
        try {
            s = new MulticastSocket(PORT);
            ip = InetAddress.getByName("224.100.100.1");
            s.joinGroup(ip);
            inTast = new BufferedReader(new InputStreamReader(System.in));
            this.nome = "";
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void start() {
        try {
            System.out.println("Inserire il nome utente");
            nome = inTast.readLine();
            System.out.println("Inizio Chat");
            System.out.println("_________________________________________");
            new Thread(new In()).start();
            new Thread(new Out()).start();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }
    }

    class Out implements Runnable {

        private byte[] buff;
        private DatagramPacket recivedPacket;

        @Override
        public void run() {
            while (true) {
                try {
                    buff = new byte[1024];
                    recivedPacket = new DatagramPacket(buff, buff.length);
                    s.receive(recivedPacket);
                    String app = new String(recivedPacket.getData());
                    if (!app.substring(app.indexOf("[")+1, app.indexOf("]")).equals(nome))
                        System.out.println(app);
                    
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    System.exit(0);
                }
            }

        }
    }

    class In implements Runnable {

        private byte[] data;
        private DatagramPacket sendPacket;

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.print(">");
                    String message = "[" + nome + "]" + inTast.readLine();
                    data = message.getBytes();
                    sendPacket = new DatagramPacket(data, data.length, ip, PORT);
                    s.send(sendPacket);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    System.exit(0);
                }
            }

        }
    }

    public static void main(String[] args) {
        ClientUDP c = new ClientUDP();
        c.start();
    }
}
