package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

public class Server {

    public static void main(String[] args) {
        System.out.println("The server is running...");
        ServerSocket socket;
        ArrayList<Pair<Socket, String>> app = new ArrayList<>();
        ArrayList<Boolean> impegnati = new ArrayList<>();
        ArrayList<Thread> allT = new ArrayList<>();
        ThreadGroup g = new ThreadGroup("group");
        try {
            socket = new ServerSocket(1234);

            Socket client;
            while (true) {
                client = socket.accept();

                new Thread(new Login(client, app, impegnati, allT, g)).start();
            }
        } catch (IOException ex) {
            System.exit(0);
        }

    }
}

class Login implements Runnable {

    PrintWriter output;
    BufferedReader input;
    Socket s;
    ArrayList<Pair<Socket, String>> utenti;
    ArrayList<Boolean> impegnati;
    ThreadGroup g;

    public Login(Socket s, ArrayList<Pair<Socket, String>> utenti, ArrayList<Boolean> impegnati, ArrayList<Thread> allT, ThreadGroup g) throws IOException {
        this.s = s;
        this.impegnati = impegnati;
        this.utenti = utenti;
        this.output = new PrintWriter(s.getOutputStream());
        this.input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.g = g;
    }

    @Override
    public void run() {
        String app = "";
        String name = "", password = "";
        int indice = 0;
        SQLHelper db;
        try {
            output.println("invia 1 per loggarsi,invia qualunque altra cosa per loggarsi");
            output.flush();
            app = input.readLine();
            db = new SQLHelper();
            while (true) {
                if (app.equals("1")) {
                    output.println("scrivere il nome utente");
                    output.flush();
                    name = input.readLine();
                    output.println("scrivere la password");
                    output.flush();
                    password = input.readLine();
                    if (db.login(name, password)) {
                        System.out.println("loggato");
                        output.println("k");
                        output.flush();

                        utenti.add(new Pair(s, name));
                        impegnati.add(Boolean.FALSE);
                        indice = utenti.size() - 1;
                        Thread t = new Thread(g, new Collegamento(s, utenti, impegnati, indice));

                        t.start();

                        break;

                    } else {
                        output.println("#");
                        output.flush();
                        output.println("vuoi registrarti?? (s/n)");
                        output.flush();
                        app = input.readLine();
                        if (app.toLowerCase().equals("s") || app.toLowerCase().equals("si")) {
                            app = "2";

                        } else {
                            app = "1";
                        }

                    }
                } else {
                    while (true) {
                        output.println("scrivere il nome utente");
                        output.flush();
                        name = input.readLine();
                        output.println("scrivere la password");
                        output.flush();
                        password = input.readLine();

                        if (!db.exist(name)) {
                            System.out.println("non c'e");
                            db.addUser(name, password);
                            //registrato
                            output.println("rt");
                            output.flush();
                            output.println("registrazione effettuata");
                            output.flush();
                            app = "1";
                            break;

                        } else {
                            //non registrato
                            output.println("rf");
                            output.flush();
                            output.println("utente gia registrato");
                            output.flush();
                        }
                    }

                }
            }

            db.closeConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

    }

}

class Collegamento implements Runnable {

    private PrintWriter output;
    private BufferedReader input;
    private Socket s;
    private ArrayList<Pair<Socket, String>> utenti;
    private ArrayList<Boolean> impegnati;
    private int indice;

    public Collegamento(Socket s, ArrayList<Pair<Socket, String>> utenti, ArrayList<Boolean> impegnati, int indice) throws IOException {
        this.s = s;
        this.impegnati = impegnati;
        this.utenti = utenti;
        this.output = new PrintWriter(s.getOutputStream());
        this.input = new BufferedReader(new InputStreamReader(s.getInputStream()));

        this.indice = indice;
    }

    private String outPersoneDisponibili(int j) {
        String str = "";
        for (int i = 0; i < impegnati.size(); i++) {
            if (i != j && impegnati.get(i)) {
                //da cambiare molto probabilmente con un codice se ho abbastanza tempo
                str = str + i + ") " + utenti.get(i).getValue();
            }

        }

        return str;
    }

    @Override
    public void run() {
        String app = "";
        impegnati.set(indice, Boolean.TRUE);
        while (true) {
            try {

                output.println(outPersoneDisponibili(indice));
                output.flush();

                Thread timer = new Thread(new Contatore());
                timer.start();
                boolean isInterrupted = false;
                while (!input.ready()) {
                    if (isInterrupted != timer.isInterrupted()) {
                        isInterrupted = true;
                        break;
                    }
                }
                if (isInterrupted) {
                    System.out.println("Sono stato interrotto");
                    s.close();
                    utenti.remove(indice);
                    impegnati.remove(indice);
                } else {
                    Contatore t = new Contatore();
                    app = input.readLine();
                    System.out.println(app);
                    //aggiungere controllo se intero
                    int user = Integer.parseInt(app);

                    if (user > -1 && user < utenti.size() && impegnati.get(user) && user != indice) {

                        impegnati.set(indice, Boolean.FALSE);

                        Socket s2 = utenti.get(user).getKey();

                        BufferedReader inpu = new BufferedReader(new InputStreamReader(s2.getInputStream()));
                        PrintWriter outpu = new PrintWriter(s2.getOutputStream());

                        outpu.println("Vuoi accettare la connessione? (s/n)");
                        outpu.flush();
                        t.interrupt();
                        app = inpu.readLine();

                        if (app.toLowerCase().equals("s") || app.toLowerCase().equals("si")) {
                            output.println(app);
                            output.flush();
                            impegnati.set(user, Boolean.FALSE);

                            new Thread(new Chat(user, indice, utenti, impegnati)).start();
                            new Thread(new Chat(indice, user, utenti, impegnati)).start();
                            System.out.println("chat partita");
                        }

                    } else {
                        impegnati.set(indice, Boolean.TRUE);

                        t.start();
                        while (t.isAlive());
                        impegnati.set(indice, Boolean.FALSE);
                        output.println("update");
                        output.flush();

                    }
                }

            } catch (IOException ex) {
                System.exit(0);
            }
            System.out.println("finito t");
        }
    }

}

class Chat implements Runnable {

    ArrayList<Pair<Socket, String>> utenti;
    ArrayList<Boolean> impegnati;
    int utente1, utente2;

    public Chat(int utente1, int utente2, ArrayList<Pair<Socket, String>> utenti, ArrayList<Boolean> impegnati) {
        this.impegnati = impegnati;
        this.utenti = utenti;
        this.utente1 = utente1;
        this.utente2 = utente2;
    }

    @Override
    public void run() {
        Socket s1 = utenti.get(utente1).getKey();
        Socket s2 = utenti.get(utente2).getKey();
        String app = "";
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(s1.getInputStream()));
            PrintWriter output = new PrintWriter(s2.getOutputStream());

            output.println("_________________________________________");
            output.flush();
            while (true) {
                app = input.readLine();
                if (!app.equals("fine")) {
                    output.println(app);
                    output.flush();
                } else {

                    s1.close();
                    s2.close();
                    System.out.println("chiso");
                }

            }
        } catch (IOException ex) {

        }

    }

}

class Contatore extends Thread {

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(20);
            Thread.currentThread().interrupt();
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
