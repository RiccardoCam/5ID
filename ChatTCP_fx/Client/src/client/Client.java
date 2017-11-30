/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.*;
import java.net.Socket;
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

    private Socket socket;
    private BufferedReader serverIn;
    private PrintWriter serverOut;
    private StringTokenizer chat = null;

    int i = 0;

    private String username;

    private ArrayList<UserStatusInterface> userStatus = new ArrayList<>();
    private ArrayList<MessageInterface> messages = new ArrayList<>();
    private ObservableList<String> usersOnline = FXCollections.observableArrayList();

    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void sendMessage(String receiver, String msg) {
        if (!receiver.equals(this.username)) {
            String out = "msg " + receiver + " " + msg + "\n";
            serverOut.println(out);
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean signUp(String username, String password) {
        this.username = username;
        String out = "signup " + username + " " + password + "\n";
        serverOut.println(out);
        String risposta = "";
        try {
            risposta = serverIn.readLine();
        } catch (IOException ex) {
        }

        if ("ok signup".equalsIgnoreCase(risposta)) {
            startMessageReader();
            return true;
        } else {
            return false;
        }
    }

    public boolean login(String username, String password) {
        this.username = username;
        if (!usersOnline.contains(username)) {
            String out = "login " + username + " " + password + "\n";
            serverOut.println(out);

            String risposta = "";

            try {
                risposta = serverIn.readLine();
            } catch (IOException ex) {
                
            }

            if ("ok login!".equalsIgnoreCase(risposta)) {
                startMessageReader();
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public void requestChat(String sender, String receiver) {
        serverOut.println("getchat " + sender + " " + receiver);

    }

    public void logoff() throws IOException {
        String out = "logoff\n";
        serverOut.println(out);
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

    public StringTokenizer getChat() {        
        return chat;
    }

    private void readMessage() {
        try {
            String input;
            StringTokenizer st;
            while (true) {
                input = serverIn.readLine();
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
                        } else if ("chat".equalsIgnoreCase(cmd)) {
                            chat = st;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(StringTokenizer st) {
        String username = st.nextToken();
        if (!username.equals(this.username)) {
            String msg = "";
            while (st.hasMoreTokens()) {
                msg = msg + " " + st.nextToken();
            }

            for (MessageInterface message : messages) {
                message.message(username, msg);
            }
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

    public void addOnlineUser(String userename) {
        usersOnline.add(username);
    }

    public boolean connect() {
        try {
            this.socket = new Socket(serverName, serverPort);
            this.serverOut = new PrintWriter(socket.getOutputStream(), true);
            this.serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

    public void addMessage(MessageInterface user) {
        messages.add(user);
    }

    public void removeMessage(MessageInterface user) {
        messages.remove(user);
    }

}
