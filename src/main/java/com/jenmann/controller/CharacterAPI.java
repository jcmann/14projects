package com.jenmann.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenmann.entity.Characters;
import com.jenmann.persistence.CharactersDao;

import java.util.List;

/**
 * This endpoint is responsible for handling all data related to characters
 * for DMBook.
 *
 * @author jcmann
 */
@Path("/characters")
public class CharacterAPI {

    private CharactersDao dao = new CharactersDao();
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Sends a response containing a JSON array of all characters stored in DMBook currently.
     *
     * @return all characters, sent as a JSON array
     */
    @GET
    @Produces("application/json")
    public Response getAllCharacters() {
        List<Characters> allCharacters = getDao().getAllCharacters();
        String responseJSON = "";

        try {
            responseJSON = getObjectMapper().writeValueAsString(allCharacters);
        } catch (Exception e) {
            // TODO logger
        }

        return Response.status(200).entity(responseJSON).build();

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

        Characters character = getDao().getById(id);
        String responseJSON = "";

        try {
            responseJSON = getObjectMapper().writeValueAsString(character);
        } catch (Exception e) {
            // TODO logger
        }

        return Response.status(200).entity(responseJSON).build();

    }

    public CharactersDao getDao() {
        return dao;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
