package com.jenmann.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenmann.entity.Characters;
import com.jenmann.entity.GetAllResponse;
import com.jenmann.entity.Monster;
import com.jenmann.persistence.MonsterDao;
import com.jenmann.util.APIFormatUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Represents and organizes the endpoints related to Monsters. This mostly serves
 * as a middleman between the DMBook frontend and the DND 5e API online.
 *
 * @author jcmann
 */
@Path("/monsters")
public class MonsterAPI {

    /**
     * Represents and handles the database connectivity for all Monsters-related data
     */
    private MonsterDao dao = new MonsterDao();

    /**
     * A utility object, used for mapping POJOs to JSON strings here.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Log4J2 instance for all logging.
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Sends a response containing a JSON array of all monsters provided by the 5e API. This requires a specific
     * format because the API returns a JSON object shaped roughly as: {count: (int), results: [{monster}, {monster}].
     * Each monster item in the array is not a full monster object, but instead only contains the name, index,
     * and the full API URL to request the full monster data for that monster.
     *
     * @return all characters, sent as a JSON array in a 200 response
     */
    @GET
    @Produces("application/json")
    public Response getAllMonsters() {
        GetAllResponse allMonsters = getDao().getAllMonsters();
        String responseJSON = "";

        try {
            responseJSON = getObjectMapper().writeValueAsString(allMonsters.getResults());
        } catch (Exception e) {
            logger.error(e.getStackTrace());
        }

        return Response.status(200).entity(responseJSON).build();

    }

    /**
     * The DND 5e API doesn't use ID numbers, but instead indexes, to uniquely identify monsters in a searchable way.
     * The index is the full name of the monster in lower snake case. This is used to get the detailed
     * data about a specific monster, which is not returned by getting all monsters.
     *
     * @param index lower snake case identifier for the monster in question
     * @return stringified json representing a Monster object
     */
    @GET
    @Path("{index}")
    @Produces("application/json")
    public Response getMonsterByIndex(@PathParam("index") String index) {
        Monster monster = getDao().getMonsterByIndex(index);
        String responseJSON = "";
        try {
            responseJSON = getObjectMapper().writeValueAsString(monster);
        } catch (Exception e) {
            logger.error("", e);
            return Response.status(404).entity("Search failed.").build();

        }
        return Response.status(200).entity(responseJSON).build();
    }

    /**
     * Returns the instance variable for the monster DAO.
     *
     * @return MonsterDao instance variable for database work
     */
    public MonsterDao getDao() {
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
