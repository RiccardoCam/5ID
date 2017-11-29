/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattcp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author alvise.carraro
 */
public class SQLHelperUsers {

    private static String dbName = "./users.db";

    private Connection connection = null;

    public SQLHelperUsers() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);// create a database connection (side effect create DB)

        } catch (SQLException e) {
            System.err.println(e.getMessage());// if the error message is "out of memory", it probably means no database file is found
        }
    }

    public ResultSet executeQuery(String s) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(s);
    }

    public void executeUpdate(String s) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(s);
    }

}
