package com.jenmann.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * A servlet to handle CRUD for the character list.
 * For exercise 4, intended to also forward to a JSP to display said results/searches.
 *
 * @author jcmann
 */
@WebServlet(
        urlPatterns = {"/characters"}
)
public class CharacterList extends HttpServlet {

    /**
     * This handles a GET request to this servlet URL. Forwards to a JSP to display characters in a table.
     *
     * @param req a general http request to this servlet
     * @param resp our response to send back
     * @throws ServletException
     * @throws IOException
     *
     */
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        CharactersDao characterDao = new CharactersDao();
//        req.setAttribute("characters", characterDao.getAllCharacters());
//        RequestDispatcher dispatcher = req.getRequestDispatcher("/characterList.jsp");
//        dispatcher.forward(req, resp);
//    }
}