/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risponditorebar;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author andrea zoccarato
 */
public class Server {

    protected ServerSocket serverSocket;

    public Server(ServerSocket s) {
        this.serverSocket = s;
    }

    protected boolean isStopped = false;

    private boolean isStopped() {
        return this.isStopped;
    }

    public void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(8080);
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        Server server = new Server(socket);
        System.out.println("The server is running...");
        while (!server.isStopped) {
            Socket clientSocket = null;
            try {
                clientSocket = socket.accept();
            } catch (IOException e) {
                if (server.isStopped()) {
                    System.out.println("Server Stopped.");
                    break;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            threadPool.execute(new Bar(clientSocket));
        }
        threadPool.shutdown();
        System.out.println("Server Stopped.");
    }
}

