package chattcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    public static String login(PrintWriter output, BufferedReader inSystem, BufferedReader input) {
        String name = "", app;
        try {
            System.out.println(input.readLine());
            app = inSystem.readLine();
            output.println("1");
            output.flush();
            while (true) {
                if (app.equals("1")) {
                    //username
                    System.out.println(input.readLine());
                    name = inSystem.readLine();
                    output.println(name);
                    output.flush();
                    //password
                    System.out.println(input.readLine());
                    output.println(inSystem.readLine());
                    output.flush();

                    app = input.readLine();
                    if (app.equals("k")) {
                        break;
                    } else {
                        System.out.println(input.readLine());
                        app = inSystem.readLine();
                        output.println(app);
                        output.flush();
                        if (app.toLowerCase().equals("s") || app.toLowerCase().equals("si")) {
                            app = "2";

                        } else {
                            app = "1";
                        }
                    }
                } else {
                    while (true) {
                        System.out.println(input.readLine());
                        output.println(inSystem.readLine());
                        output.flush();
                        System.out.println(input.readLine());
                        output.println(inSystem.readLine());
                        output.flush();
                        app = input.readLine();
                        if (app.equals("rt")) {
                            System.out.println(input.readLine());
                            app = "1";
                            break;
                        } else {
                            System.out.println(input.readLine());
                        }
                    }

                }
            }

        } catch (IOException ex) {
            System.exit(0);
        }
        return name;
    }

    public static void inizioChat(PrintWriter output, BufferedReader inSystem, BufferedReader input, String name) throws IOException, InterruptedException {
        String app = "";
        System.out.println("sei loggato");

        ThreadGroup g = new ThreadGroup("group");
        Thread tout = new Thread(g, new out(output, inSystem, name));
        Thread tin = new Thread(g, new input(input));

        System.out.println(input.readLine());
        System.out.println("Codice persona che vuoi parlare oppure scrivere -1 per attendere di agganciarsi");
        app = inSystem.readLine();

        output.println(app);
        output.flush();

        if (app.equals("-1")) {

            System.out.println("Aspetto l'agganciamento di una comunicazione");

            String app2 = input.readLine();
            if (app2.equals("update")) {
                inizioChat(output, inSystem, input, name);
            } else {
                System.out.println(app2);
                app = inSystem.readLine();

                output.println(app);
                output.flush();

                System.out.println(app);

                if (app.toLowerCase().equals("s") || app.toLowerCase().equals("si")) {
                    //output
                    tout.start();
                    //input
                    tin.start();
                    System.out.println("chat partita");
                }
            }
        } else {

            System.out.println("aspetto");
            app = input.readLine();
            System.out.println(app);
            if (app.toLowerCase().equals("s") || app.toLowerCase().equals("si")) {
                System.out.println("t");
                //output
                tout.start();
                //input
                tin.start();
                System.out.println("chat partita");
            }

        }
        tout.join();
        tin.join();

    }

    public static void main(String[] args) {
        String name = "";

        System.out.println("Connecting to server...");

        try {
            Socket Client = new Socket("127.0.0.1", 1234);
            System.out.println("Client connected");

            BufferedReader input = new BufferedReader(new InputStreamReader(Client.getInputStream()));
            BufferedReader inSystem = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter output = new PrintWriter(Client.getOutputStream());

            name = login(output, inSystem, input);
            System.out.println(name);
            inizioChat(output, inSystem, input, name);

        } catch (IOException ex) {
            System.exit(0);
        } catch (InterruptedException ex) {
            System.exit(0);
        }

    }
}

//________________________________________________________________________________
class out implements Runnable {

    public PrintWriter stream;
    public BufferedReader inSystem;
    public String nome;

    public out(PrintWriter stream, BufferedReader inSystem, String nome) {
        this.stream = stream;
        this.inSystem = inSystem;
        this.nome = nome;
    }

    @Override
    public void run() {
        String app = "";
        try {
            while (true) {

                app = inSystem.readLine();
                if (app.equals("fine")) {
                    stream.println("fine");
                    stream.flush();
                    System.out.println("e uscito fine");

                } else {
                    stream.println("[" + nome + "]" + app);
                    stream.flush();
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        } finally {
            System.exit(0);
        }
    }

}

//________________________________________________________________________________
class input implements Runnable {

    public BufferedReader stream;

    public input(BufferedReader stream) {
        this.stream = stream;
    }

    @Override
    public void run() {
        String app = "";
        try {
            while (true) {
                app = stream.readLine();
                if (app.equals("fine")) {
                    break;
                }
                System.out.println(app);

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("GoodBye");
            System.exit(0);
        } finally {
            System.out.println("GoodBye");
            System.exit(0);
        }

    }

}
