package Client;

import utility.Pacchetto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client{

    private Socket server;
    private BufferedReader in;
    private PrintWriter out;
    private final InetAddress INDIRIZZO_IP;
    private final int PORTA;
    private String username;

    public Client(String indirizzoIp,int porta) throws IOException {
        INDIRIZZO_IP = InetAddress.getByName(indirizzoIp);
        PORTA = porta;
        server = new Socket(INDIRIZZO_IP, porta);
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            out = new PrintWriter(server.getOutputStream(), true);
        }catch (Exception e){
            throw new IllegalStateException("Impossibile comunicare con il server");
        }
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public Pacchetto riceviPacchetto() {
        try {
            return Pacchetto.decodificaPacchetto(in.readLine());
        } catch (IOException e) {
            return new Pacchetto(Pacchetto.Tipo.ERRORE);
        }
    }

    public void inviaPacchetto(Pacchetto pacchetto){
        out.println(pacchetto.toString());
    }

    public void start(){
        out.println(new Pacchetto(Pacchetto.Tipo.INIZIO));
    }

    public void stop(){
        out.println(new Pacchetto(Pacchetto.Tipo.FINE));
    }



}