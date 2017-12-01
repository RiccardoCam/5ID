/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import server.Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author sandro
 */
public class ServerMain {
    

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

        System.out.println("Server running..");
        ServerSocket listener = new ServerSocket(9898);
        ThreadPoolExecutor ex = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        try {
            while (true) {
                Server.Task t = new Server.Task(listener.accept());
                User u = new User(t);
                Server.users.add(u);
                int pos = Server.users.indexOf(u);
                t.setPos(pos);
                System.out.println("la posizione del thread è: " + pos);

                ex.execute(t);

                System.out.println("Clienti on: " + ex.getPoolSize());
                System.out.println(Server.users);
            }
        } finally {
            listener.close();
        }

    }

    
}
