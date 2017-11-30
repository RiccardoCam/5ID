package Server;


public class Main{

    public static void main(String[] args) throws Exception {
        Server server = new Server(1455);
        server.run();
      /*  SessioneChat s = new SessioneChat();
        System.out.println(s.registrazione("pippo","test"));*/
    }
}
