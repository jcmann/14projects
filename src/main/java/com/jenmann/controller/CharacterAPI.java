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

@Path("/characters")
public class CharacterAPI {

    @GET
    @Produces("application/json")
    public Response getAllCharacters() {
        CharactersDao characterDao = new CharactersDao();
        List<Characters> allCharacters = characterDao.getAllCharacters();
        ObjectMapper mapper = new ObjectMapper();
        String responseJSON = "";

        try {
            responseJSON = mapper.writeValueAsString(allCharacters);
        } catch (Exception e) {
            // TODO logger
        }

        return Response.status(200).entity(responseJSON).build();

    }
}
