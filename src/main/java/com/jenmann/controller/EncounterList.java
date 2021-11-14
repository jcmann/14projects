package com.jenmann.controller;

import com.jenmann.entity.Encounter;
import com.jenmann.entity.GetAllResponse;
import com.jenmann.persistence.EncounterDao;
import com.jenmann.persistence.MonsterDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A testing/utility type class that just lists all encounters in the database. Nothing to see here! ;)
 *
 * @author jcmann
 */
@WebServlet(urlPatterns = {"/encounters"})
public class EncounterList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EncounterDao dao = new EncounterDao();
        List<Encounter> encounters = dao.getAllEncounters();
        req.setAttribute("encounters", encounters);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/encountersList.jsp");
        dispatcher.forward(req, resp);
    }
}
