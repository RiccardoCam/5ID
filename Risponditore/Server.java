import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private static final int SERVER_PORT = 8008;

    public static void main(String[] args) throws IOException {
        try (ServerSocket listener = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server acceso");
            while (true) {
                new Thread(new Mercato(listener.accept())).start();
            }
        } catch (Exception e) {
            System.out.println("Il server già acceso");
        }
    }
}
