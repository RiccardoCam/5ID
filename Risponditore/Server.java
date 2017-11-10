/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package risponditore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author riccardo.camillo
 */
public class Server {

    private static int port = 1500;
    private static int maxClient = 5;

    public static void main(String[] args) throws Exception {
        System.out.println("Server is running.");
        int clientNumber = 0;
        ServerSocket server = new ServerSocket(port);
        try {
            while (maxClient >0) {
                Socket cl = server.accept();
                Thread t = new Thread(new Cinema(cl, clientNumber++));
                t.start();
                maxClient--;
            }
        } finally {
            server.close();
        }
    }
}
