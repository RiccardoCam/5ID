
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author leonardo
 */
public class Client {
    
    private BufferedReader in;
    private BufferedReader inSystem;
    private PrintWriter out;
    private String nome;
    private String serverAddress="127.0.0.1";
    
    
    public void connect() throws IOException{
        System.out.println("Connecting to server...");
        
        
        
        String userInput;
        
        Socket s=new Socket(serverAddress,1234);
        
        System.out.println("Client connected");
        
        in=new BufferedReader(new InputStreamReader(s.getInputStream()));
        
        inSystem=new BufferedReader(new InputStreamReader(System.in));
        
        out=new PrintWriter(s.getOutputStream(),true);
        
        String app="";
        while (true) {
            System.out.println("ok");
            app=in.readLine();
            
            if(app.equals("#"))break;
            
            System.out.println(app);
            
            userInput=inSystem.readLine();
            
            out.println(userInput);
            
            System.out.println(in.readLine());
        }
        
        System.out.println("Price: "+in.readLine());
        
        
        
        
        
       
    }
    

    public static void main(String[] args) throws IOException {
        Client client=new Client();
        client.connect();
    }
}
