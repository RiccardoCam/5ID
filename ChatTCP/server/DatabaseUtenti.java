package server;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

class DatabaseUtenti {

    private static DatabaseUtenti istanza;
    private Connection connessione;

    private DatabaseUtenti() throws SQLException {
        connessione = DriverManager.getConnection("jdbc:sqlite:server/Server.db");
        connessione.setAutoCommit(true);
        Statement statement = connessione.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Utenti(Username, Password);");
    }

    public static synchronized DatabaseUtenti getIstanza() {
        if (istanza == null) {
            try {
                istanza = new DatabaseUtenti();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return istanza;
    }

    public synchronized boolean loginUtente(String username, String password) {
        try {
            PreparedStatement ps = connessione.prepareStatement("SELECT * FROM Utenti WHERE Username = ? AND Password = ?");
            ps.setString(1, username);
            ps.setString(2, hashString(password));
            ResultSet r = ps.executeQuery();
            return r.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public synchronized boolean registraUtente(String username, String password) {
        try {
            PreparedStatement check = connessione.prepareStatement("SELECT Username FROM Utenti WHERE Username = ?");
            check.setString(1, username);
            ResultSet r = check.executeQuery();
            if (r.next()) {
                return false;
            } else {
                PreparedStatement prep = connessione.prepareStatement("INSERT INTO Utenti VALUES (?, ?);");
                prep.setString(1, username);
                prep.setString(2, hashString(password));
                prep.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private String hashString(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(s.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
