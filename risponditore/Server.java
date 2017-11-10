

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    protected ServerSocket server;

    public Server(ServerSocket s) {
        this.server = s;
    }

    protected boolean isStopped = false;

    private boolean isStopped() {
        return this.isStopped;
    }

    public void stop() {
        this.isStopped = true;
        
        try {
            this.server.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("The server is running...");
        
        ServerSocket socket = new ServerSocket(1234);
        
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        
        Server server = new Server(socket);
        
        while (!server.isStopped) {
            
            Socket client;
            try {
                
                client = socket.accept();
            } catch (IOException e) {
                if (server.isStopped()) {
                    System.out.println("Server Stopped.");
                    break;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            
            
            threadPool.execute(new Car(client));
        }
        threadPool.shutdown();
        System.out.println("Server Stopped.");
    }
}

