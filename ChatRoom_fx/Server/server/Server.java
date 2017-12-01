/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import javafx.util.Pair;

/**
 *
 * @author Cristian
 */
public class Server {

    private MulticastSocket UdpSocket;
    public InetAddress address;
    private Semaphore semaforo = new Semaphore(0);
    private String messaggio;
    private static final int portNumber = 8080;
    private byte[] buf = new byte[1024];
    private DatagramPacket dp;
    private SQLHelper dbMessages = new SQLHelper("Messages");

    private ArrayList<Pair<String, InetAddress>> ipList = new ArrayList<>();
    private ArrayList<Pair<String, Integer>> portList = new ArrayList<>();

    public Server(int serverPort) {
        try {
            UdpSocket = new MulticastSocket(serverPort);
            writeData("Server started");
            new Thread(() -> {
                while (true) {
                    try {
                        dp = new DatagramPacket(buf, buf.length);
                        UdpSocket.receive(dp);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    String input = new String(dp.getData());
                    handleCMD(input, dp.getAddress(), dp.getPort());
                }
            }).start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void handleCMD(String input, InetAddress ipAddress, int port) {
        try {
            StringTokenizer st;
            while (true) {
                st = new StringTokenizer(input);
                if (st != null && st.countTokens() > 0) {
                    String cmd = st.nextToken();
                    if ("login".equalsIgnoreCase(cmd)) {
                        logIn(st, ipAddress, port);
                    } else if ("signup".equalsIgnoreCase(cmd)) {
                        signUp(st, ipAddress, port);
                    } else if ("msg".equalsIgnoreCase(cmd)) {
                        handleMessage(st);
                    } else if ("logoff".equalsIgnoreCase(cmd)) {
                        logOut(st);
                    } else {
                        writeData("Unknown " + cmd);
                    }
                }
            }
        } catch (Exception ex) {
            writeData(ex.getMessage());

        }
    }

    private void logOut(StringTokenizer st) {
        String username = st.nextToken();
        int i = 0;
        for (Pair<String, InetAddress> x : ipList) { //remove from ipaddress list
            if (x.getKey().equals(username)) {
                ipList.remove(i);
            }
            i++;
        }
        i = 0;
        for (Pair<String, Integer> x : portList) { //remove from port list
            if (x.getKey().equals(username)) {
                portList.remove(i);
            }
            i++;
        }
        try {
            notifyLeave(username);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void handleMessage(StringTokenizer st) throws IOException {
        String sender = st.nextToken();
        InetAddress ipSender = getAddressByUser(sender);
        String message = "";
        while (st.hasMoreTokens()) {
            message = message + " " + st.nextToken();
        }

        String outMsg = "msg " + sender + " " + message;
        for (int i = 0; i < ipList.size(); i++) { //send all
            if (!ipList.get(i).getValue().equals(ipSender)) {
                DatagramPacket app = new DatagramPacket(outMsg.getBytes(), outMsg.length(), ipList.get(i).getValue(), portList.get(i).getValue());
                UdpSocket.send(app);
            }

        }

        writeData("New message from " + sender + " ==> " + message);
    }

    private InetAddress getAddressByUser(String username) {
        for (Pair<String, InetAddress> x : ipList) {
            if (x.getKey().equals(username)) {
                return x.getValue();
            }
        }
        return null;
    }

    private int getPortByUser(String username) {
        for (Pair<String, Integer> x : portList) {
            if (x.getKey().equals(username)) {
                return x.getValue();
            }
        }
        return -1;
    }

    private boolean signUp(StringTokenizer st, InetAddress ipAddress, int port) throws IOException {
        String out = "";
        if (st.countTokens() >= 2) {
            String myUsername = st.nextToken();
            String password = st.nextToken();
            if (isUsernameUsed(myUsername)) {
                out = ("Username already in use.");
                return false;
            } else {
                try {
                    SQLHelper database = new SQLHelper("Accounts");
                    database.executeUpdate("Insert INTO Accounts " + "VALUES ('" + myUsername + "','" + password + "')");
                    ipList.add(new Pair(myUsername, ipAddress));
                    portList.add(new Pair(myUsername, port));
                    out = "Ok signup";
                    writeData(myUsername + " signed up succesfully!");
                    database.close();
                } catch (SQLException ex) {
                    writeData(ex.getMessage());
                }

                notifyJoin(myUsername);
                return true;
            }
        }
        out = "Error signup";
        DatagramPacket app = new DatagramPacket(out.getBytes(), out.length(), ipAddress, port);
        UdpSocket.send(app);
        return false;
    }

    private boolean logIn(StringTokenizer st, InetAddress ipAddress, int port) throws IOException {
        String out = "";
        DatagramPacket app;
        if (st.countTokens() >= 2) {
            String username = st.nextToken();
            String password = st.nextToken();
            if (!getAccounts().contains(new Pair(username, password))) {
                out = ("Account non esistente");
                app = new DatagramPacket(out.getBytes(), out.length(), InetAddress.getLocalHost(), port);
                UdpSocket.send(app);
                return false;
            } else {
                out = ("ok login!");
                app = new DatagramPacket(out.getBytes(), out.length(), ipAddress, port);
                UdpSocket.send(app);
                writeData(username + " logged in succesfully!");
                ipList.add(new Pair(username, ipAddress));
                portList.add(new Pair(username, port));
                notifyJoin(username);
                return true;
            }
        }

        return false;
    }

    private void notifyLeave(String sender) throws IOException {
        // notifico a tutti gli utenti online il mio login
        String out = "offline " + sender;
        for (int i = 0; i < ipList.size(); i++) {
            if (!getAddressByUser(sender).equals(ipList.get(i).getValue())) {
                DatagramPacket app = new DatagramPacket(out.getBytes(), out.length(), ipList.get(i).getValue(), portList.get(i).getValue());
                UdpSocket.send(app);
            }
        }
    }

    private void notifyJoin(String sender) throws IOException {
        // notifico a tutti gli utenti online il mio login
        String out = "online " + sender;
        for (int i = 0; i < ipList.size(); i++) {
            if (!getAddressByUser(sender).equals(ipList.get(i).getValue())) {
                DatagramPacket app = new DatagramPacket(out.getBytes(), out.length(), ipList.get(i).getValue(), portList.get(i).getValue());
                UdpSocket.send(app);
            }
        }
    }

    private ArrayList<Pair<String, String>> getAccounts() {
        ArrayList<Pair<String, String>> result = new ArrayList<>();
        try {
            SQLHelper database = new SQLHelper("Accounts");
            ResultSet rs = database.executeQuery("Select * from Accounts");
            while (rs.next()) {
                result.add(new Pair(rs.getString("Username"), rs.getString("Password")));
            }
            database.close();
        } catch (SQLException ex) {
            writeData(ex.getMessage());
        }
        return result;
    }

    private boolean isUsernameUsed(String username) {
        ArrayList<Pair<String, String>> list = getAccounts();
        for (Pair<String, String> x : list) {
            if (x.getKey().equals(username)) {
                return true;
            }
        }

        return false;
    }

    public synchronized void writeData(String data) {
        messaggio = data;
        semaforo.release();
    }

    public String getData() {
        try {
            semaforo.acquire();
        } catch (InterruptedException ex) {
            writeData(ex.getMessage());
        }
        return messaggio;
    }

    public void close() {
        UdpSocket.close();
    }

}
