/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author Cristian
 */
public class SocketThread extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private String myUsername = "";
    private PrintWriter pw;
    private String msgOut;
    private ArrayList<String> usersOnline = new ArrayList<>();
    private Semaphore semaforo = new Semaphore(0);
    private SQLHelper dbMessages = new SQLHelper("Messages");
    int i = 0;

    public SocketThread(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        //try {
        //dbMessages.executeQuery("DELETE FROM Messages");
        //} catch (SQLException ex) {
        //setMessage(ex.getMessage());
        //}

    }

    public String getData() {
        try {
            semaforo.acquire();
        } catch (InterruptedException ex) {
            setMessage(ex.getMessage());
        }
        return msgOut;
    }

    private void setMessage(String nuovo) {
        msgOut = nuovo;
        semaforo.release();
    }

    @Override
    public void run() {
        setMessage("The server is running...");
        try {
            refreshUsersOnline();
            handleCmd();
        } catch (Exception e) {
            setMessage(e.getMessage());
        }
    }

    public void close() {
        try {
            clientSocket.close();
        } catch (IOException ex) {
            setMessage("Socket closed");
        }
    }

    public void refreshUsersOnline() {
        for (SocketThread s : server.getSocketList()) {
            usersOnline.add(s.getMyUsername());
        }
    }

    public ArrayList<String> getUsersOnline() {
        return usersOnline;
    }

    private void handleCmd() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            pw = new PrintWriter(clientSocket.getOutputStream(), true);

            String input;
            StringTokenizer st;
            while (true) {
                input = in.readLine();
                st = new StringTokenizer(input);
                if (st != null && st.countTokens() > 0) {
                    String cmd = st.nextToken();
                    if ("logoff".equals(cmd)) {
                        logOff();
                        break;
                    } else if ("login".equalsIgnoreCase(cmd)) {
                        logIn(st);
                    } else if ("signup".equalsIgnoreCase(cmd)) {
                        signUp(st);
                    } else if ("msg".equalsIgnoreCase(cmd)) {
                        handleMessage(st);

                    } else {
                        setMessage("unknown " + cmd);
                    }
                }
            }

            close();

        } catch (Exception ex) {
            setMessage(ex.getMessage());
        }

    }


    private void handleMessage(StringTokenizer st) throws IOException {
        String receiver = st.nextToken();
        String message = "";
        while (st.hasMoreTokens()) {
            message = message + " " + st.nextToken();
        }

        ArrayList<SocketThread> socketList = server.getSocketList();
        for (SocketThread socket : socketList) {
            if (receiver.equalsIgnoreCase(socket.getMyUsername())) {
                String outMsg = "msg " + myUsername + " " + message;
                socket.send(outMsg);
            }
        }
        setMessage("New message from " + myUsername + " to " + receiver + " ==> " + message);
        try {
            dbMessages.executeUpdate("Insert INTO Messages " + "VALUES ('" + myUsername + "','" + receiver + "','" + message + "')");
        } catch (SQLException ex) {
            setMessage(ex.getMessage());
        }
    }

    private void logOff() throws IOException {
        server.removeUser(this);
        ArrayList<SocketThread> socketList = server.getSocketList();
        setMessage(myUsername + " logged out");

        // notifico agli altri utenti il mio log off
        String outMsg = "offline " + myUsername;
        for (SocketThread socket : socketList) {
            if (!myUsername.equals(socket.getMyUsername())) {
                socket.send(outMsg);
            }
        }
        clientSocket.close();
    }

    public String getMyUsername() {
        return myUsername;
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
            setMessage(ex.getMessage());
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

    private boolean signUp(StringTokenizer st) throws IOException {
        int i = 0;
        if (st.countTokens() == 2) {
            String myUsername = st.nextToken();
            String password = st.nextToken();
            if (isUsernameUsed(myUsername)) {
                pw.println("Error signup\n" + i++);
                return false;
            } else {
                try {
                    SQLHelper database = new SQLHelper("Accounts");
                    database.executeUpdate("Insert INTO Accounts " + "VALUES ('" + myUsername + "','" + password + "')");
                    pw.println("ok signup\n");
                    setMessage(myUsername + " signed up succesfully!");
                    database.close();
                } catch (SQLException ex) {
                    setMessage(ex.getMessage());
                }

                this.myUsername = myUsername;
                notifyJoin();
                return true;
            }
        }
        pw.println("Error signup\n" + i++);
        return false;
    }

    private boolean logIn(StringTokenizer st) throws IOException {
        if (st.countTokens() == 2) {
            String username = st.nextToken();
            String password = st.nextToken();
            if (usersOnline.contains(username)) {
                pw.println("Username already in use");
                return false;
            } else if (!getAccounts().contains(new Pair(username, password))) {
                pw.println("Account non esistente");
                return false;
            } else {
                pw.println("ok login!");
                this.myUsername = username;
                setMessage(myUsername + " logged in succesfully!");
                notifyJoin();
                return true;
            }
        }

        return false;
    }

    private void notifyJoin() throws IOException {
        ArrayList<SocketThread> socketList = server.getSocketList();
        // ricerco tutti gli utenti online
        for (SocketThread socket : socketList) {
            if (socket.getMyUsername() != null) {
                if (!myUsername.equals(socket.getMyUsername())) {
                    String outMsg = "online " + socket.getMyUsername();
                    send(outMsg);
                }
            }
        }

        //notifico gli altri utenti il mio log in
        String outMsg = "online " + myUsername;
        for (SocketThread socket : socketList) {
            if (!myUsername.equals(socket.getMyUsername())) {
                socket.send(outMsg);
            }
        }
    }

    private void send(String msg) throws IOException {
        if (myUsername != null) {
            try {
                pw.println(msg);
            } catch (Exception ex) {
                setMessage(ex.getMessage());
            }
        }
    }

}
