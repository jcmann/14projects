package com.jenmann.controller;

import com.jenmann.persistence.CharacterDao;

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
 * In final project, this will not return a JSP but just data.
 *
 * @author jcmann
 */

@WebServlet(
        urlPatterns = {"/characters"}
)

public class CharacterList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CharacterDao characterDao = new CharacterDao();
        req.setAttribute("users", characterDao.getAllCharacters());
        RequestDispatcher dispatcher = req.getRequestDispatcher("/results.jsp");
        dispatcher.forward(req, resp);
    }
}