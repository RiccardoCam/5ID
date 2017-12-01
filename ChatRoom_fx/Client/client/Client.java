/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Cristian
 */
public class Client {

    private final String serverName;
    private final int serverPort;
    private final int port = 8080;
    private byte[] buf = new byte[1024];
    private String username;

    private MulticastSocket socket;

    private ArrayList<UserStatusInterface> userStatus = new ArrayList<>();
    private ArrayList<MessageInterface> messages = new ArrayList<>();
    private ObservableList<String> usersOnline = FXCollections.observableArrayList();

    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void sendMessage(String msg) throws UnknownHostException {
            String out = "msg " + InetAddress.getLocalHost() + " " + username + " + msg + ";
            DatagramPacket dout = new DatagramPacket(out.getBytes(), out.length(), socket.getInetAddress(), socket.getPort());
        try {
            socket.send(dout);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean signUp(String username, String password) {
        String out = "signup " + username + " " + password + "\n";
        DatagramPacket dout = new DatagramPacket(out.getBytes(), out.length(), socket.getInetAddress(), socket.getPort());
        try {
            socket.send(dout);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        DatagramPacket din = new DatagramPacket(buf, buf.length);
        String risposta = new String(din.getData());

        if ("ok signup".equalsIgnoreCase(risposta)) {
            this.username = username;
            startMessageReader();
            return true;
        } else {
            return false;
        }
    }

    public boolean login(String username, String password) throws UnknownHostException {
        if (!usersOnline.contains(username)) {
            String out = "login " + username + " " + password + "\n";
            DatagramPacket dout = new DatagramPacket(out.getBytes(), out.length(), InetAddress.getLocalHost(), 8080);
            try {
                socket.send(dout);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            DatagramPacket din = new DatagramPacket(buf, buf.length);
            String risposta = new String(din.getData());
            if ("ok login!".equalsIgnoreCase(risposta)) {
                this.username = username;
                startMessageReader();
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public void logoff() throws IOException {
        String out = "logoff " + username;
        DatagramPacket app = new DatagramPacket(out.getBytes(), out.length(), socket.getInetAddress(), socket.getPort());
        socket.send(app);
        System.exit(0);
    }

    private void startMessageReader() {
        Thread t = new Thread() {
            @Override
            public void run() {
                readMessage();
            }
        };
        t.start();
    }

    private void readMessage() {
        try {
            String input;
            StringTokenizer st;
            while (true) {
                DatagramPacket in = new DatagramPacket(buf, buf.length);
                socket.receive(in);
                input = new String(in.getData());
                if (input != null) {
                    st = new StringTokenizer(input);
                    if (st != null && st.countTokens() > 0) {
                        String cmd = st.nextToken();
                        if ("online".equalsIgnoreCase(cmd)) {
                            addUser(st);
                        } else if ("offline".equalsIgnoreCase(cmd)) {
                            removeUser(st);
                        } else if ("msg".equalsIgnoreCase(cmd)) {
                            handleMessage(st);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleMessage(StringTokenizer st) {
        String sender = st.nextToken();
        String msg = "";
        while (st.hasMoreTokens()) {
            msg = msg + " " + st.nextToken();
        }

        for (MessageInterface message : messages) {
            message.message(sender, msg);
        }

    }

    private void removeUser(StringTokenizer st) {
        String username = st.nextToken();
        for (UserStatusInterface user : userStatus) {
            user.offline(username);
        }
    }

    public ObservableList<String> getUsersOnline() {
        return usersOnline;
    }

    private void addUser(StringTokenizer st) {
        String username = st.nextToken();
        usersOnline.add(username);
        for (UserStatusInterface user : userStatus) {
            user.online(username);
        }
    }

    public boolean connect() {
        try {
            this.socket = new MulticastSocket(port);            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUserStatus(UserStatusInterface user) {
        userStatus.add(user);
    }

    public void removeUserStatus(UserStatusInterface user) {
        userStatus.remove(user);
    }

    public void addMessage(MessageInterface message) {
        messages.add(message);
    }

    public void removeMessage(MessageInterface message) {
        messages.remove(message);
    }

}
