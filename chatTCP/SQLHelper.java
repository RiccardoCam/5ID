/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author bigetti
 */
public class SQLHelper {

    private Connection connection;
    private Statement statement;

    public SQLHelper() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:chat.sqlite");
            this.statement = connection.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public boolean login(String nome, String password) {
        if (!nome.equals("")) {
            try {
                String query = "select * from chat where nome='" + nome + "';";
                ResultSet rs = statement.executeQuery(query);
                if (rs.next()) {
                    String name = rs.getString("nome");
                    String pass = rs.getString("password");
                    return name.equals(nome) && password.equals(pass);
                } else {
                    return false;
                }

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                closeConnection();
            }
        }
        return false;
    }

    public boolean exist(String nome) {
        if (!nome.equals("")) {
            try {
                String query = "select * from chat where nome='" + nome + "';";
                ResultSet result = statement.executeQuery(query);
                return result.next();

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                closeConnection();
            }
        }
        return false;
    }

    public void addUser(String nome, String password) {
        try {
            String sql = "insert into chat values('" + nome + "','" + password + "')";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
