package com.jenmann.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenmann.entity.Characters;
import com.jenmann.entity.Encounter;
import com.jenmann.entity.User;
import com.jenmann.persistence.CharactersDao;
import com.jenmann.persistence.EncounterDao;
import com.jenmann.persistence.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * This endpoint is responsible for handling all data related to users
 * for DMBook.
 *
 * @author jcmann
 */
@Path("/users")
public class UserAPI {

    /**
     * Represents and handles the database connectivity for all User-related data
     */
    private UserDao dao = new UserDao();

    /**
     * Users are associated with encounters, so the API contains a DAO for this.
     */
    private EncounterDao encounterDao = new EncounterDao();

    /**
     * Users are associated with characters, so the API contains a DAO for this.
     */
    private CharactersDao charactersDao = new CharactersDao();

    /**
     * A utility object, used for mapping POJOs to JSON strings here.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Log4J2 instance for all logging.
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * This endpoint expects a user ID, and will return the encounters
     * associated with that user ID.
     *
     * @param id a user ID
     * @return stringified JSON representing all encounters found
     */
    @GET
    @Path("{id}/encounters")
    @Produces("application/json")
    public Response getUserEncounters(@PathParam("id") int id) {

        User user = dao.getById(id);
        List<Encounter> encounters = encounterDao.getByUser(user);
        String responseJSON = "";

        try {
            responseJSON = objectMapper.writeValueAsString(encounters);
        } catch (Exception e) {
            logger.error("", e);
        }

        return Response.status(200).entity(responseJSON).build();

    }

}
