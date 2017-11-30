package Server;

/**
 *
 * @author Sandro
 */
public class Main{

    public static void main(String[] args) throws Exception {
        Server server = new Server(4446);
        server.start();
    }
}