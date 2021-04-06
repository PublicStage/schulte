package com.etheapp.brainserver.servlets;

import com.bear.brain.logic.coding.Coder;
import com.etheapp.brainserver.CommandController;
import com.etheapp.brainserver.Commands;
import com.etheapp.brainserver.Log;
import com.etheapp.brainserver.logic.GGame;

import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/s"}, loadOnStartup = 1)
public class CommandServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String json = Coder.decode(req.getQueryString());
            Log.info("CommandServlet %s [%s]", json, Commands.getClientIp(req));
            GGame game = (GGame) getServletContext().getAttribute("Game");
            String answer = CommandController.process(game, json, Commands.getClientIp(req));
            Log.info(Coder.decode(answer));
            resp.getWriter().print(answer);
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}