package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sandro
 */
public class Server {

    protected static ArrayList<User> users = new ArrayList<>();


    private static int getPos(String nick) {
        for (int i = 0; i < users.size(); i++) {
            if (nick.equals(users.get(i).getUsername())) {
                return i;
            }
        }
        return -1;
    }

    private static String getUsername(int pos) {
        return users.get(pos).getUsername();

    }

    private static User getUser(int pos) {
        return users.get(pos);
    }

    private static User getUser(String nick) {
        return users.get(getPos(nick));
    }
    /**
     *
     */
    public static class Task implements Runnable {

        private BufferedReader in;
        private PrintWriter out;
//---------------
        private String MIONOME;
        private SQHelper sqh;
        private Socket socket;
        private int indice;
        private boolean killa;

        public Task(Socket socket) throws ClassNotFoundException, SQLException, IOException {
            this.in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
//---------------
            this.killa = false;
            this.socket = socket;
            this.sqh = new SQHelper();

        }

        public void diminuisciPos() {
            this.indice--;
        }

        public void setPos(int c) {
            this.indice = c;
        }

        public void println(String mex, Task t) {
            t.out.println(mex);
        }

        /**
         *
         */
        @Override
        public void run() {
            try {

                while (true) {
                    String input = in.readLine();
                    //------------------------------------
                    if (input.equals("signIn")) {
                        String username = in.readLine();
                        String password = in.readLine();
                        if (sqh.isPresente(username)) {
                            if (sqh.getPassword(username).equals(password)) {
                                this.MIONOME = username;
                                for (int i = 0; i < users.size(); i++) {
                                    if (getUsername(i).equals(this.MIONOME)) {
                                        out.println("no");
                                        break;
                                    }
                                }
                                System.out.println("passato");
                                out.println("go");
                                System.out.println("indice nell'array di " + this.MIONOME + ": " + indice);
                                users.get(indice).setUsername(this.MIONOME);
                                System.out.println(users);
                                out.println(this.MIONOME);

                            } else {
                                this.killa = true;
                                out.println("no");
                            }
                        } else {
                            this.killa = true;
                            out.println("no");
                        }

                    }
                    //------------------------------------
                    if (input.equals("confermaregistrazione")) {
                        String username = in.readLine();
                        if (!sqh.isPresente(username)) {
                            out.println("go");
                        } else {
                            out.println("no");
                        }

                    }
                    //------------------------------------
                    if (input.equals("invioregistrazione")) {
                        String username = in.readLine();
                        String password = in.readLine();
                        sqh.register(username, password);
                    }
                    //------------------------------------
                    if (input.equals("Aggiorna")) {
                        System.out.println("Hai premuto aggiorna");
                        System.out.println(users);
                        out.println("go");
                        for (int i = 0; i < users.size(); i++) {
                            String nome = getUsername(i);
                            if (!nome.equals(this.MIONOME) && getUser(i).getDisponibilita()) {

                                out.println("prendiNome");
                                System.out.println("il nome che stai passando è: " + nome);
                                out.println(nome);
                            } else {
                                System.out.println("non passare il mio nome, non ha senso");
                            }

                        }
                        out.println("finito");

                    }
                    //------------------------------------
                    if (input.equals("Elimina")) {

                        System.out.println(this.MIONOME + " left");

                        for (int i = indice + 1; i < users.size() - 1; i++) {

                            users.get(i).diminuisciPos();
                        }
                        users.remove(getPos(this.MIONOME));
                        System.out.println(users);

                    }
                    //--per risolvere un bug, un caso specifico
                    if (input.equals("hopremutosignin")) {
                        if (killa == true) {
                            System.out.println(this.MIONOME + " left");

                            for (int i = indice + 1; i < users.size() - 1; i++) {
                                users.get(i).diminuisciPos();
                            }
                            users.remove(indice);
                            System.out.println(users);
                        }
                    }
                    //------------------------------------
                    if (input.equals("vogliomessaggiare")) {
                        String nickSorg = in.readLine();
                        String nickDest = in.readLine();
                        //--
                        User uSorg = getUser(nickSorg);
                        Task tSorg = uSorg.getTask();
                        //--
                        User uDest = getUser(nickDest);
                        Task tDest = uDest.getTask();
                        //---
                        out.println(nickDest);
                        tDest.out.println(nickSorg);

                        while (true) {
                            String inSorg = in.readLine();
                            System.out.println(nickSorg + ": " + inSorg);
                            if (inSorg.equals("finito")) {
                                System.out.println("chaweofiajweofwefijewfemwfewfawefwaefaw");
                                break;
                            } else {
                                //     System.out.println(nickDest + ": " + inDest);
                                tDest.out.println(nickSorg + ":\n " + inSorg);
                                //out.println(nickDest + ": " + inDest);
                            }
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println("LEFT???");

            } catch (SQLException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
