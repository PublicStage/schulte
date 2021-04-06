package com.etheapp.brainserver.servlets;

import com.etheapp.brainserver.logic.GGame;
import com.etheapp.brainserver.logic.Reload;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/restart"}, loadOnStartup = 1)
public class RestartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GGame game = (GGame) getServletContext().getAttribute("Game");
        Reload.loadFromDB(game);
        resp.sendRedirect("list.jsp");
    }
}

