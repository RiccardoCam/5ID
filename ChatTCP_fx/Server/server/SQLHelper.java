/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 *
 * @author cristian.boldrin
 */
public class SQLHelper {

    private Connection conn;

    public SQLHelper(String dbName) {

        Logger logger = Logger.getLogger("DB");
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".sqlite");

        } catch (ClassNotFoundException e) {
            logger.severe("Driver NON trovato: " + e.getMessage());
            System.exit(1);

        } catch (SQLException e) {
            logger.severe("SQLException: " + e.getMessage());
            System.exit(1);
        }
    }
    

    public boolean isOpen() throws SQLException {
        return !conn.isClosed();
    }
    
    public void executeUpdate(String query) throws SQLException {
        Statement st;
        st = conn.createStatement();
        st.executeUpdate(query);
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement st;
        st = conn.createStatement();
        return st.executeQuery(query);
    }

    public void close() throws SQLException {
        conn.close();
    }
}
