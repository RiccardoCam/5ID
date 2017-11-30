package client;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DatabaseConversazioni {

    private static DatabaseConversazioni istanza;
    private final Connection connessione;
    private final String username;
    private final Map<String, String> conversazioni;

    private DatabaseConversazioni(String username) throws SQLException {
        this.username = username;
        connessione = DriverManager.getConnection("jdbc:sqlite:client/Client.db");
        connessione.setAutoCommit(true);
        Statement statement = connessione.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS DatabaseConversazioni(Username, Contatto, Messaggi);");
        conversazioni = new HashMap<>();
        PreparedStatement ps = connessione.prepareStatement("SELECT Contatto, Messaggi FROM DatabaseConversazioni WHERE Username = ?");
        ps.setString(1, username);
        ResultSet s = ps.executeQuery();
        while (s.next()) {
            String utente = s.getString("Contatto");
            String messaggi = s.getString("Messaggi");
            conversazioni.put(utente, messaggi);
        }
    }

    public static DatabaseConversazioni getIstanza(String username) {
        if (istanza == null) {
            try {
                istanza = new DatabaseConversazioni(username);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return istanza;
    }

    public void creaNuovaConversazione(String utente) {
        if (!conversazioni.containsKey(utente)) {
            conversazioni.put(utente, "");
        }
    }

    public void aggiungiAConversazione(String utente, String messaggio, boolean inviato) {
        String conversazione = conversazioni.get(utente);
        if (inviato) {
            conversazione += "<div class=\"message sent\">" + escapeHtml(messaggio) + "</div>";
        } else {
            conversazione += "<div class=\"message received\">" + escapeHtml(messaggio) + "</div>";
        }
        conversazioni.put(utente, conversazione);
    }

    public String getConversazione(String utente) {
        return conversazioni.get(utente);
    }

    public List<String> getConversazioni() {
        return new ArrayList<>(conversazioni.keySet());
    }

    public void salva() throws SQLException {
        for (String contatto : conversazioni.keySet()) {
            String messaggi = conversazioni.get(contatto);
            PreparedStatement ex = connessione.prepareStatement("SELECT * FROM DatabaseConversazioni WHERE Username = ? AND Contatto = ?");
            ex.setString(1, username);
            ex.setString(2, contatto);
            ResultSet e = ex.executeQuery();
            if (e.next()) {
                PreparedStatement ps = connessione.prepareStatement("UPDATE DatabaseConversazioni SET Messaggi = ? WHERE Username = ? AND Contatto = ?");
                ps.setString(1, messaggi);
                ps.setString(2, username);
                ps.setString(3, contatto);
                ps.executeUpdate();
            } else if (!messaggi.isEmpty()) {
                PreparedStatement ps = connessione.prepareStatement("INSERT INTO DatabaseConversazioni VALUES (?, ?, ?)");
                ps.setString(1, username);
                ps.setString(2, contatto);
                ps.setString(3, messaggi);
                ps.executeUpdate();
            }
        }
        connessione.close();
    }

    private String escapeHtml(String string) {
        StringBuilder escapedTxt = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char tmp = string.charAt(i);
            switch (tmp) {
                case '<':
                    escapedTxt.append("&lt;");
                    break;
                case '>':
                    escapedTxt.append("&gt;");
                    break;
                case '&':
                    escapedTxt.append("&amp;");
                    break;
                case '"':
                    escapedTxt.append("&quot;");
                    break;
                case '\'':
                    escapedTxt.append("&#x27;");
                    break;
                case '/':
                    escapedTxt.append("&#x2F;");
                    break;
                default:
                    escapedTxt.append(tmp);
            }
        }
        return escapedTxt.toString();
    }
}
