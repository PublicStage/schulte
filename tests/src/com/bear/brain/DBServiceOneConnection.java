package com.bear.brain;

import com.etheapp.brainserver.db.DBService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBServiceOneConnection extends DBService {

    public static void createInstance() {
        instance = new DBServiceOneConnection();
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost/brain?useUnicode=true&serverTimezone=Europe/Kiev",
                    "clicker",
                    "KHn3iHmBVSBDnc78CZ8b");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }
}
