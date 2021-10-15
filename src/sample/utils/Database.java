package sample.utils;

import java.sql.*;

public class Database {
    public Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/whatsapp-clone", "root", "");
            return conn;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void executeUpdateQuery(String q) {
        Connection connection = getConnection();
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(q);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public ResultSet executeQuery(String q) throws SQLException {
        Statement st = getConnection().createStatement();
        return st.executeQuery(q);
    }
}
