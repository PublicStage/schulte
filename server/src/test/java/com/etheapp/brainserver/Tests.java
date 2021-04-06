package com.etheapp.brainserver;

import com.etheapp.brainserver.logic.Score;
import com.etheapp.brainserver.ranger.Ranger;
import com.etheapp.brainserver.ranger.User;
import com.mysql.cj.jdbc.MysqlDataSource;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class Tests {

    @Test
    public void testRange() {

        String user1 = "1";
        String user2 = "2";
        String user3 = "3";

        com.etheapp.brainserver.ranger.Ranger r = new com.etheapp.brainserver.ranger.Ranger();
        r.addResult(user1, 10);
        r.addResult(user2, 9);
        r.addResult(user1, 9);


        com.etheapp.brainserver.ranger.Ranger ranger = new com.etheapp.brainserver.ranger.Ranger();
        assertEquals(100, ranger.getGrid(user1).getPercent(10));
        assertEquals(100, ranger.getGrid(user2).getPercent(10));

        ranger.addResult(user1, 10);
        assertEquals(100, ranger.getGrid(user1).getPercent(10));
        assertEquals(100, ranger.getGrid(user2).getPercent(9));
        assertEquals(0, ranger.getGrid(user2).getPercent(10));
        ranger.addResult(user2, 10);
        ranger.addResult(user2, 10);
        assertEquals(100, ranger.getGrid(user2).getPercent(10));
        assertEquals(50, ranger.getGrid(user1).getPercent(10));

        assertEquals(100, ranger.getGrid(user3).getPercent(9));
        assertEquals(0, ranger.getGrid(user3).getPercent(11));

        ranger = new Ranger();
        ranger.addResult("1", 10);
        ranger.addResult("2", 15);
        ranger.addResult("3", 20);
        ranger.addResult("4", 20);
        ranger.addResult("5", 30);

        assertEquals(100, ranger.getGrid("10").getPercent(9));
        assertEquals(80, ranger.getGrid("10").getPercent(12));
        assertEquals(60, ranger.getGrid("10").getPercent(18));
        assertEquals(20, ranger.getGrid("10").getPercent(25));


    }

    @Test
    public void testLogger() {

        Log.info("message");
        Log.info("message %s", 123);
        Log.info("message %s %d", "456", 600);
    }

    @Test
    public void userBestScore() {
        User user = new User("user");
        assertEquals(Long.MAX_VALUE, user.getBest().getValue());

        user.addScore(new Score(0, 5000));
        assertEquals(5000, user.getBest().getValue());

        user.addScore(new Score(0, 6000));
        assertEquals(5000, user.getBest().getValue());

        user.addScore(new Score(0, 1000));
        assertEquals(1000, user.getBest().getValue());
    }

    @Test
    public void testDBConnection() throws SQLException {


        MysqlDataSource dataSource = new MysqlDataSource();
        // Set dataSource Properties
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(3306);
        dataSource.setServerTimezone("UTC");
        dataSource.setDatabaseName("brain");
        dataSource.setUser("clicker");
        dataSource.setPassword("KHn3iHmBVSBDnc78CZ8b");

        Connection connection = dataSource.getConnection();
        Log.info("DB connection created [%s]", connection.getMetaData().getURL());

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT version()");
        if (rs.next()) {
            Log.info("Database Version : %s", rs.getString(1));
        }
        rs.close();
        stmt.close();
        connection.close();
    }

    @Test
    public void testClassForName() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/brain?useUnicode=true&serverTimezone=UTC",
                "clicker",
                "KHn3iHmBVSBDnc78CZ8b");

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT version()");
        if (rs.next()) {
            System.out.println("Database Version : " + rs.getString(1));
        }
        rs.close();
        stmt.close();
        connection.close();
    }
}
