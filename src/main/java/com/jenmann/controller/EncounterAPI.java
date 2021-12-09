package com.jenmann.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenmann.entity.Encounter;
import com.jenmann.persistence.EncounterDao;
import com.jenmann.persistence.GenericDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * This endpoint is responsible for handling all data related to encounters
 * for DMBook.
 *
 * @author jcmann
 */
@Path("/encounters")
public class EncounterAPI {

    /**
     * Represents and handles the database connectivity for all Characters-related data
     */
    private GenericDao dao = new GenericDao<Encounter>(Encounter.class);

    /**
     * A utility object, used for mapping POJOs to JSON strings here.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Log4J2 instance for all logging.
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * This endpoint returns all encounters in the database.
     *
     * @return stringified JSON of all encounters in the db
     */
    @GET
    @Produces("application/json")
    public Response getAllEncounters() {
        List<Encounter> allCharacters = dao.getAll();
        String responseJSON = "";

        try {
            responseJSON = objectMapper.writeValueAsString(allCharacters);
        } catch (Exception e) {
            logger.error("", e);
        }

        return Response.status(200).entity(responseJSON).build();
    }

    /**
     * This endpoint returns a single encounter by the ID provided.
     *
     * @param id the id of the encounter to search for
     * @return stringified JSON of that encounter, or a failed status
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getEncounterByID(@PathParam("id") int id) {
        Encounter encounter = (Encounter) getDao().getById(id);
        String responseJSON = "";

        try {
            responseJSON = getObjectMapper().writeValueAsString(encounter);
        } catch (Exception e) {
            logger.error("", e);
        }

        // TODO conditionally render to make sure null is a 500 or something
        return Response.status(200).entity(responseJSON).build();
    }

    /**
     * A getter for this API endpoint's dao, which is an EncounterDao
     *
     * @return the instance of an EncounterDao
     */
    public GenericDao getDao() {
        return dao;
    }

    /**
     * A getter method for the objectMapper instance variable
     * @return the objectMapper instance variable
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Setter method for the objectMapper instance variable
     *
     * @param objectMapper the instance variable objectMapper
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Returns the Log4J2 logger instance variable
     * @return
     */
    public Logger getLogger() {
        return logger;
    }
}
