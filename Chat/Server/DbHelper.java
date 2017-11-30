package Server;

import java.sql.*;

class DbHelper{

    private Connection connection;

    DbHelper(String dbName) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
    }

    ResultSet executeQuery(String query) throws SQLException{
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    void executeUpdate(String query) throws SQLException{
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    void close(){
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        }catch (Exception ignored){}
    }

    @Override
    protected void finalize(){
        close();
    }

}
