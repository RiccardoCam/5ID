package Server;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Server extends Thread{

    private final int PORTA;
    private final int NUMERO_MASSIMO_CLIENT;
    private MulticastSocket socket;

    public Server(int porta,int numeroMassimoClient){
        PORTA = porta;
        NUMERO_MASSIMO_CLIENT = numeroMassimoClient;
        try {
            socket = new MulticastSocket(PORTA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server(int porta){
        this(porta,50);
    }

    @Override
    public void run(){
        System.out.println("Server avviato");
        while (true) {
            try {
                byte[] buff = new byte[1024];
                DatagramPacket pacchettoRicevuto = new DatagramPacket(buff, buff.length);
                socket.receive(pacchettoRicevuto);
                byte[] data = pacchettoRicevuto.getData();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName("224.236.25.1"), PORTA);
                socket.send(sendPacket);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
