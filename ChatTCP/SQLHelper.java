package chattcp;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Camillo
 */
public class SQLHelper {

    public void creaUtente(String utente, String password) {
        if (!esisteUtente(utente)) {
            Connection connection = null;
            try {
                connection = getConnection();
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                statement.executeUpdate("insert into users(nome, password) values(\"" + utente + "\", \"" + password + "\")");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    System.err.println(e);
                }
            }
        }
    }

    public boolean esisteUtente(String utente) {
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from users where nome like \"" + utente + "\"");
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
                System.err.println(e);
            }
        }
        return false;
    }

    public boolean passwordCorretta(String utente, String password) {
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from users where nome like \"" + utente + "\" AND password like \"" + password + "\"");
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
                System.err.println(e);
            }
        }
        return false;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:ChatTcp.sqlite");
    }

}
