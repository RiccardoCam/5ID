/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
/**
 *
 * @author Alvise
 */
public class Client {

    private static MulticastSocket socket;
    private static final int PORTA = 4446;
    public static String user="";
    
    public Client() throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        this.socket = new MulticastSocket(PORTA);
        socket.joinGroup(InetAddress.getByName("224.100.100.1"));
    }
    
    public String ricevi() {
        try {
            byte buf[] = new byte[1024];
            DatagramPacket rPacket = new DatagramPacket(buf, buf.length);
            socket.receive(rPacket);
            return new String(rPacket.getData());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void invia(String mittente, String testo) {
        try {
            byte[] data = (mittente + ";" + testo).getBytes();
            socket.send(new DatagramPacket(data, data.length, InetAddress.getByName("224.100.100.1"), PORTA));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    

    
}
