package com.etheapp.brainserver.db;

import com.bear.brain.ChatMessage;
import com.bear.brain.GameResult;
import com.bear.brain.SendResult;
import com.etheapp.brainserver.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBService {
    private final String LOGINS = "logins";
    private final String RESULTS = "results";
    private final String STATE = "state";
    private final String CHAT_RU = "chat_ru";
    private final String CHAT_EN = "chat_en";
    private final String BOTS = "bots";

    static protected DBService instance;

    public static void createInstance() {
        instance = new DBService();
    }

    public static DBService getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        InitialContext initContext = null;
        try {
            initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/brain");
            return ds.getConnection();
        } catch (NamingException | SQLException e) {
            throw new SQLException(e);
        }
    }

    public boolean connectionCheck() {
        try (Connection connection = getConnection();) {
            Log.info("DB connection created [%s]", connection.getMetaData().getURL());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String escapeString(String s, int maxsize) {
        if (s == null) return null;

        if (s.length() > maxsize) {
            s = s.substring(0, maxsize);
        }
        return escapeString(s);
    }

    public static String escapeString(String s) {
        if (s == null) return null;
        return s.replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\\x1A", "\\Z")
                .replace("\\x00", "\\0")
                .replace("'", "\\'")
                .replace("\"", "\\\"");
    }

    public static String escapeWildcards(String s) {
        return escapeString(s)
                .replaceAll("%", "\\%")
                .replaceAll("_", "\\_");
    }

    private static CharSequence dbStrOrNull(String str) {
        return str == null ? "NULL" : ("'" + str + "'");
    }

    public void writeLogin(String uuid, int version, String ip) {
        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            stm.executeUpdate("INSERT INTO " + LOGINS + " SET " +
                    " `date` = NOW(), " +
                    " uuid = '" + uuid + "', " +
                    " vers = " + version + ", " +
                    " ip = '" + ip + "' " +
                    " ");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void writeChatMessage(String uuid, String locale, ChatMessage chatMessage) {
        try (Connection connection = getConnection()) {
            Statement stm = connection.createStatement();
            stm.executeUpdate("INSERT INTO " + (locale.equals("ru") ? CHAT_RU : CHAT_EN) + " SET " +
                    " date = '" + new Timestamp(chatMessage.getTime()) + "', " +
                    " uuid = '" + uuid + "', " +
                    " name = '" + escapeString(chatMessage.getName(), 100) + "', " +
                    " place = " + chatMessage.getPlace() + ", " +
                    " message = '" + escapeString(chatMessage.getMessage(), 1024) + "'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void writeResult(SendResult result) {
        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            for (GameResult gameResult : result.list) {
                stm.executeUpdate("INSERT INTO " + RESULTS + " SET " +
                        " uuid = '" + result.uuid + "', " +
                        " date = '" + new Timestamp(gameResult.getDateTime()) + "', " + // TODO server times
                        " level = " + gameResult.getLevel() + ", " +
                        " result = " + gameResult.getDuration() + " " +
                        " ");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void writeResult(String uuid, GameResult result) {
        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            stm.executeUpdate("INSERT INTO " + RESULTS + " SET " +
                    " uuid = '" + uuid + "', " +
                    " date = '" + new Timestamp(result.getDateTime()) + "', " +
                    " level = " + result.getLevel() + ", " +
                    " result = " + result.getDuration() + " " +
                    " ");

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    public void writeName(String uuid, String name) {
        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            stm.executeUpdate("INSERT INTO " + STATE + " SET " +
                    " `uuid` = '" + uuid + "', " +
                    " `name` = '" + escapeString(name, 100) + "', " +
                    " registration = NOW(), " +
                    " last_login = NOW() " +
                    " ON DUPLICATE KEY UPDATE " +
                    " `name` = '" + name + "', " +
                    " last_login = NOW()");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void writeStateLogin(String uuid, String ip) {
        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            stm.executeUpdate("INSERT INTO " + STATE + " SET " +
                    " `uuid` = '" + uuid + "', " +
                    " registration = NOW(), " +
                    " last_login = NOW(), " +
                    " ip = '" + ip + "' " +
                    " ON DUPLICATE KEY UPDATE " +
                    " last_login = NOW(), " +
                    " ip = '" + ip + "' ");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    public List<ResultRecord> loadResults() {
        List<ResultRecord> list = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT * FROM " + RESULTS + " WHERE `date`>NOW() - INTERVAL 1 DAY");
            while (rs.next()) {
                list.add(new ResultRecord(rs.getString("uuid"), rs.getTimestamp("date").getTime(), rs.getInt("level"), rs.getLong("result")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    public List<ResultRecord> loadUuidResults(String uuid) {
        List<ResultRecord> list = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT * FROM " + RESULTS + " WHERE `uuid`='" + uuid + "' AND `date`>NOW() - INTERVAL 1 DAY");
            while (rs.next()) {
                list.add(new ResultRecord(rs.getString("uuid"), rs.getTimestamp("date").getTime(), rs.getInt("level"), rs.getLong("result")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }


    public String loadNameOrNull(String uuid) {
        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT * FROM " + STATE + " WHERE `uuid`='" + uuid + "'");
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }


    public void clearDB() {
        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            stm.executeUpdate("DELETE FROM " + RESULTS);
            stm.executeUpdate("DELETE FROM " + LOGINS);
            stm.executeUpdate("DELETE FROM " + STATE);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public List<ChatMessage> loadChat(String locale) {
        List<ChatMessage> list = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT * FROM " + (locale.equals("ru") ? CHAT_RU : CHAT_EN) + " ORDER BY id");
            while (rs.next()) {
                list.add(new ChatMessage(rs.getTimestamp("date").getTime(), rs.getString("name"), rs.getString("message"), rs.getInt("place")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return list;
    }

    public boolean nameExists(String name) {
        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT `uuid` FROM " + STATE + " WHERE `name`='" + escapeString(name) + "'");
            if (rs.next()) {
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public List<BotRecord> readBots() {
        List<BotRecord> list = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement stm = connection.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT * FROM " + BOTS + " ORDER BY `uuid`");
            while (rs.next()) {
                list.add(new BotRecord(rs.getString("uuid"), rs.getString("name"), rs.getFloat("effect")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return list;
    }
}
