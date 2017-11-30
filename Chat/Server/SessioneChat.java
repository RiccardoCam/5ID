package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

class SessioneChat {

    private static DbHelper database;

    private String username;

    SessioneChat() {
        if(database != null)return;
        try {
            database = new DbHelper("utenti.db");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Impossibile connettersi al database");
        }
    }

    public String getUsername(){
        return username;
    }

    boolean login(String username,String password){
        boolean ris = false;
        try {
           ResultSet resultSet = database.executeQuery("SELECT * FROM UTENTI WHERE username = '"+username+"' AND password = '"+password+"' LIMIT 1");
           if(resultSet.next()){
               aggiornaUltimoAccesso();
               ris = true;
               this.username = username;
           }
           resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return ris;
    }

    boolean registrazione(String username,String password){
        try {
            ResultSet controlloUtenteEsistente = database.executeQuery("SELECT * FROM UTENTI WHERE username = '"+username+"' LIMIT 1");
            if(!controlloUtenteEsistente.next()) {
                database.executeUpdate("INSERT INTO UTENTI (username,password) VALUES ('"+username+"','"+password+"')");
                login(username,password);
                return true;
            }
            controlloUtenteEsistente.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    void aggiornaUltimoAccesso(){
        try {
            database.executeUpdate("UPDATE UTENTI SET ultimoAccesso = dateTime('now') WHERE username = '"+username+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
