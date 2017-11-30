/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class ChatRoomUDPClient {

    private final int PORT = 9999; //PORTA DEDICATA AL SERVER
    //private int PORT;
    private final String MULTICASTIP = "224.236.25.1"; //IP DEL GRUPPO MULTICAST

    private MulticastSocket socket;
    private String username;
    private BufferedReader input;
    private InetAddress address;

    public ChatRoomUDPClient() {
        try {
            input = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Inserire un username con cui entrare nel gruppo");
            username = input.readLine();
            socket = new MulticastSocket(PORT);
            //PORT=socket.getPort();
            address = InetAddress.getByName(MULTICASTIP);
            socket.joinGroup(address);
            System.out.println("Benvenuto nella chat");

            new Thread(new Invio(socket, input, username, address, PORT)).start();
            new Thread(new Ascolto(socket, username)).start();
            
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void main(String[] args) {
        ChatRoomUDPClient chatroom = new ChatRoomUDPClient();
    }

    class Invio implements Runnable {

        private DatagramPacket sendPacket;
        private byte[] toSend;
        private BufferedReader input;
        private String username;
        private InetAddress ip;
        private int port;
        private MulticastSocket socket;

        public Invio(MulticastSocket s, BufferedReader in, String u, InetAddress ip, int p) {
            this.input = in;
            this.username = u;
            this.port = p;
            this.ip = ip;
            this.socket = s;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String message = ">>" + username + ">>" + input.readLine();
                    toSend = message.getBytes();
                    sendPacket = new DatagramPacket(toSend, toSend.length, ip, port);
                    socket.send(sendPacket);
                } catch (IOException ex) {
                    Logger.getLogger(Invio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    class Ascolto implements Runnable {

        private MulticastSocket socket;
        private String username;
        private byte[] recivedByte;
        private DatagramPacket recivedPacket;

        public Ascolto(MulticastSocket s, String u) {
            socket = s;
            username = u;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    byte packetData[] = new byte[2048]; //cercato un eventuale limite massimo da porre come grandezza del pacchetto
                    recivedPacket = new DatagramPacket(packetData, packetData.length);
                    socket.receive(recivedPacket);
                    recivedByte = recivedPacket.getData();
                    String recivedMessage = new String(recivedByte);
                    String recivedUsername[] = recivedMessage.split(">>");
                    if (!recivedUsername[0].equals(username)) {
                        System.out.println(recivedMessage);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

}
