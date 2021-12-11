package com.jenmann.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenmann.entity.Characters;
import com.jenmann.persistence.GenericDao;
import com.jenmann.util.APIFormatUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * This endpoint is responsible for handling all data related to characters
 * for DMBook.
 *
 * @author jcmann
 */
@Path("/characters")
public class CharacterAPI {

    /**
     * Represents and handles the database connectivity for all Characters-related data
     */
    private GenericDao dao = new GenericDao<Characters>(Characters.class);

    /**
     * A utility object, used for mapping POJOs to JSON strings here.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Log4J2 instance for all logging.
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * A utility to standardize how objects are mapped in the API. Handles object mapping.
     */
    private APIFormatUtility formatUtility = new APIFormatUtility();

    /**
     * Sends a response containing a JSON array of all characters stored in DMBook currently.
     *
     * @return all characters, sent as a JSON array
     */
    @GET
    @Produces("application/json")
    public Response getAllCharacters() {
        logger.info("Received request to getAllCharacters() in CharacterAPI.");
        List<Characters> allCharacters = getDao().getAll();
        String responseJSON = "";
        int statusCode = 0;

        try {
            responseJSON = formatUtility.jsonFormatter(allCharacters);
            statusCode = formatUtility.determineStatusCode(responseJSON);
        } catch (Exception e) {
            logger.error("", e);
        }

        return Response.status(statusCode).entity(responseJSON).build();

    }

    /**
     * Using a passed-in ID parameter, returns the character that matches that ID.
     *
     * @param id ID for the intended character
     * @return a JSON object representing that character
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getCharacterByID(@PathParam("id") int id) {
        logger.info("Received request to getCharacterByID() in CharacterAPI.");
        Characters character = (Characters) getDao().getById(id);
        String responseJSON = "";
        int statusCode = 0;

        try {
            responseJSON = formatUtility.jsonFormatter(character);
            statusCode = formatUtility.determineStatusCode(responseJSON);
        } catch (Exception e) {
            logger.error("", e);
        }

        return Response.status(statusCode).entity(responseJSON).build();

    }

    /**
     * Returns the instance variable for the character DAO.
     *
     * @return charactersDao instance variable for database work
     */
    public GenericDao getDao() {
        return dao;
    }

    /**
     * Returns the objectMapper instance variable for converting POJOs to JSON.
     *
     * @return an ObjectMapper
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
