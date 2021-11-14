package com.jenmann.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenmann.entity.Characters;
import com.jenmann.entity.Encounter;
import com.jenmann.persistence.EncounterDao;
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
    private EncounterDao dao = new EncounterDao();

    /**
     * A utility object, used for mapping POJOs to JSON strings here.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Log4J2 instance for all logging.
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    @GET
    @Produces("application/json")
    public Response getAllEncounters() {
        List<Encounter> allCharacters = dao.getAllEncounters();
        String responseJSON = "";

        try {
            responseJSON = objectMapper.writeValueAsString(allCharacters);
        } catch (Exception e) {
            logger.error(e.getStackTrace());
        }

        return Response.status(200).entity(responseJSON).build();
    }

}
