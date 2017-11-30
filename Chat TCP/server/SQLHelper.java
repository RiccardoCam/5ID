/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Filippo
 */
public class SQLHelper {

    static Connection conn = null;
    static String url = "jdbc:sqlite:./DBChat.db";

    public static Connection getConnection() {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public static void inserisciUtente(String username, String password) {
        Connection conn = getConnection();
        String query = "INSERT INTO UTENTE (Username,Password)"
                + "     VALUES(?,?)";
        PreparedStatement st;
        try {
            st = conn.prepareStatement(query);
            st.setString(1, username);
            st.setString(2, password);
            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean esistonoCredenziali(String username, String password) {
        Statement st = null;
        boolean trovatoUsername = false;
        ResultSet rs = null;
        try {
            Connection con = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM UTENTE");
            while (rs.next()) {
                if (rs.getString("Username").equals(username)) {
                    if (rs.getString("Password").equals(password)) {
                        trovatoUsername=true;
                    }
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trovatoUsername;

        }

    

    public static boolean esisteUsername(String username) {
        boolean ris = false;
        try {
            Connection con = getConnection();
            String q = "SELECT * FROM UTENTE WHERE Username = ?";
            PreparedStatement st = con.prepareStatement(q);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            ris = rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ris;
    }

}
