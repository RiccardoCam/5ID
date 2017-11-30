/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattcp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Sandro
 */
public class SQHelper {
    
    private Connection dbConnection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public SQHelper() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
    }
    
    public void register(String username, String password) throws SQLException {
        dbConnection = DriverManager.getConnection("jdbc:sqlite:database.db");
        preparedStatement = dbConnection.prepareStatement("INSERT INTO AccountDB (username, password) VALUES ('" + username + "', '" + password + "');");
        preparedStatement.execute();
        dbConnection.close();
    }
    
    public boolean isPresente(String username) throws SQLException {
        dbConnection = DriverManager.getConnection("jdbc:sqlite:database.db");
        preparedStatement = dbConnection.prepareStatement("select * from AccountDB");
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            if (username.equals(resultSet.getString("username"))) {
                dbConnection.close();
                return true;
            }
        }
        dbConnection.close();
        return false;
        
    }
    
    public String getPassword(String username) throws SQLException {
        dbConnection = DriverManager.getConnection("jdbc:sqlite:database.db");
        preparedStatement = dbConnection.prepareStatement("select * from AccountDB");
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            if (username.equals(resultSet.getString("username"))) {
                String ret= resultSet.getString("password");
                dbConnection.close();
                return ret;
            }
        }
        dbConnection.close();
        return "utente non esiste";
        
    }
    
    public void showResultSet() throws SQLException {
        dbConnection = DriverManager.getConnection("jdbc:sqlite:database.db");
        preparedStatement = dbConnection.prepareStatement("select * from AccountDB");
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String usr = resultSet.getString("username");
            String pw = resultSet.getString("password");
            System.out.println(usr + ", " + pw);
            
        }
        dbConnection.close();
        
    }
}
