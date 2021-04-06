package com.etheapp.brainserver;

import com.etheapp.brainserver.bot.BotScheduler;
import com.etheapp.brainserver.db.DBService;
import com.etheapp.brainserver.logic.GGame;
import com.etheapp.brainserver.logic.Reload;
import com.etheapp.brainserver.logic.ServerTime;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Log.initLogFile();
            Log.info("SERVER STARTED (version of " + Version.version() + ")");

            InitialContext initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/brain");
            Connection connection = ds.getConnection();
            Log.info("Connection created " + (connection != null ? "ok" : "null"));
            connection.close();

            GGame game = new GGame(new ServerTime());
            sce.getServletContext().setAttribute("Game", game);

            DBService.createInstance();
            Reload.loadFromDB(game);

            BotScheduler botScheduler = new BotScheduler(DBService.getInstance().readBots(), game);
            botScheduler.start();
            sce.getServletContext().setAttribute("Bot", botScheduler);

        } catch (IOException | NamingException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LoginSaveQueue.instance.waitFinish();
        GGame game = (GGame) sce.getServletContext().getAttribute("Game");
        game.dispose();
        BotScheduler botScheduler = (BotScheduler) sce.getServletContext().getAttribute("Bot");
        botScheduler.stop();
        Log.info("SERVER stopped");
    }
}
