package com.jenmann.controller;

import com.jenmann.entity.GetAllResponse;
import com.jenmann.persistence.MonsterDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * A testing/utility type class that just lists all monsters in the DND 5e API. Nothing to see here! :)
 *
 * @author jcmann
 */
@WebServlet(
        urlPatterns = {"/monsters"}
)
public class MonsterList extends HttpServlet {

    /**
     * Handles a simple get request to this servlet and routes to a JSP that displays all monsters in a table
     *
     * @param req a general http request to this servlet
     * @param resp our response to send back
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        MonsterDao monsterDao = new MonsterDao();
        GetAllResponse gar = monsterDao.getAllMonsters();
        req.setAttribute("monstersCount", gar.getCount());
        req.setAttribute("monstersResults", gar.getResults());
        RequestDispatcher dispatcher = req.getRequestDispatcher("/monstersList.jsp");
        dispatcher.forward(req, resp);
    }
}