package chattcp;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author luca.modolo
 */

public class SQLHelper {
    //ArrayList<String> nomeUtenti = new ArrayList<>();

    public void creaUtente(String utente, String password) {
        if (!esisteUtente(utente)) {
            Connection connection = null;
            try {
                // create a database connection
                connection = getConnection();
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                statement.executeUpdate("insert into Utente(Nome, Password) values(\"" + utente + "\", \"" + password + "\")");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e);
                }
            }
            //nomeUtenti.add(utente);
        }
    }

    public boolean esisteUtente(String utente) {
        Connection connection = null;
        try {
            // create a database connection
            connection = getConnection();
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from Utente where Nome like \"" + utente + "\"");
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return false;
    }

    public boolean passwordCorretta(String utente, String password) {
        Connection connection = null;
        try {
            // create a database connection
            connection = getConnection();
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from Utente where Nome like \"" + utente + "\" AND Password like \"" + password + "\"");
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return false;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:db.sqlite");
    }

}
