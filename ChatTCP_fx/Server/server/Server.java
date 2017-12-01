/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristian
 */
public class Server {

    private ArrayList<SocketThread> socketList = new ArrayList<>();
    private ServerSocket serverSocket;
    private Semaphore semaforo = new Semaphore(0);
    private String messaggio;

    public Server(int serverPort) {
        try {
            new Thread(()->{
                try {
                    serverSocket = new ServerSocket(serverPort);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                 while (true) {
                     try {
                         Socket clientSocket = serverSocket.accept();
                         SocketThread user = new SocketThread(this, clientSocket);
                         new Thread(()->{
                             while(true){
                                 writeData(user.getData());
                             }
                         }).start();
                         socketList.add(user);
                         user.start();
                     } catch (Exception ex) {
                         writeData(ex.getMessage());
                     }
            }}).start();
          
        } catch (Exception e) {
            writeData(e.getMessage());
        }
    }

    public ArrayList<SocketThread> getSocketList() {
        return socketList;
    }

    public synchronized void writeData(String data){
        messaggio = data;      
        semaforo.release();
    }

    public String getData(){
        try {
            semaforo.acquire();
        } catch (InterruptedException ex) {
            writeData (ex.getMessage());
        }
         return messaggio;
    }
    
    public void close() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            writeData(ex.getMessage());
        }
        System.exit(0);
    }

    public void removeUser(SocketThread socketThread) {
        socketList.remove(socketThread);
    }
}
