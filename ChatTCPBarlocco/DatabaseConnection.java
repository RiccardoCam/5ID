package chattcp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author robertobarlocco
 */
public class DatabaseConnection {

    private Connection connection;
    private Statement statement;

    public DatabaseConnection() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:ChatTCP.sqlite");
            this.statement = connection.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public boolean login(String username, String password) {
        if (!username.equals("")) {
            try {
                String query = "select * from CredenzialiAccesso where username='" + username + "';";
                ResultSet rs = statement.executeQuery(query);
                if (rs.next()) {
                    String name = rs.getString("username");
                    String pass = rs.getString("password");
                    return name.equals(username) && password.equals(pass);
                } else {
                    return false;
                }

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                chiudiConnessione();
            }
        }
        return false;
    }

    public boolean exist(String username) {
        if (!username.equals("")) {
            try {
                String query = "select * from CredenzialiAccesso where username='" + username + "';";
                ResultSet rs = statement.executeQuery(query);
                return rs.next();

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                chiudiConnessione();
            }
        }
        return false;
    }

    public void addUser(String username, String password) {
        try {
            String sql = "insert into CredenzialiAccesso values('" + username + "','" + password + "')";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void chiudiConnessione() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
