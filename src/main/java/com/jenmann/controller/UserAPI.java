package com.jenmann.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenmann.auth.Keys;
import com.jenmann.auth.TokenResponse;
import com.jenmann.entity.Characters;
import com.jenmann.entity.Encounter;
import com.jenmann.entity.User;
import com.jenmann.persistence.CharactersDao;
import com.jenmann.persistence.EncounterDao;
import com.jenmann.persistence.UserDao;
import com.jenmann.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * This endpoint is responsible for handling all data related to users
 * for DMBook.
 *
 * @author jcmann
 */
@Path("/users")
public class UserAPI implements PropertiesLoader {
    // TODO hopefully an API implementing PL isn't an issue

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

    private Properties properties;

    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String OAUTH_URL;
    private String LOGIN_URL;
    private String REDIRECT_URL;
    private String REGION;
    private String POOL_ID;
    Keys jwks;

    /**
     * This is a helper method used for all Users API endpoints. A JWT that contains a valid, logged-in
     * user must be passed as the first parameter to access any DMBook data. Each endpoint must, then, validate
     * the JWT provided from the client before providing any data.
     *
     * @param jwt The JWT, passed from the endpoint, to validate
     * @return username of the validated user
     */
    public String processJWT(String jwt) {
        String username = null;

        if (jwt == null) {
            // TODO figure out error handling
        } else {
            HttpRequest authRequest = buildAuthRequest(jwt);
            try {
                TokenResponse tokenResponse = getToken(authRequest);
            } catch (IOException e) {
                logger.error("There was an IO Exception while trying to process the auth request.");
                logger.error("", e);
                // TODO error handling
            } catch (InterruptedException e) {
                logger.error("There was an InterruptedException while trying to process the auth request.");
                logger.error("", e);
                // TODO error handling
            }
        }

        return username;
    }

    // TODO comment
    private TokenResponse getToken(HttpRequest authRequest) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<?> response = null;

        response = client.send(authRequest, HttpResponse.BodyHandlers.ofString());


        logger.debug("Response headers: " + response.headers().toString());
        logger.debug("Response body: " + response.body().toString());

        ObjectMapper mapper = new ObjectMapper();
        TokenResponse tokenResponse = mapper.readValue(response.body().toString(), TokenResponse.class);
        logger.debug("Id token: " + tokenResponse.getIdToken());

        return tokenResponse;
    }

    // TODO comment
    private HttpRequest buildAuthRequest(String jwt) {
        String keys = CLIENT_ID + ":" + CLIENT_SECRET;

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "authorization_code");
        parameters.put("client-secret", CLIENT_SECRET);
        parameters.put("client_id", CLIENT_ID);
        parameters.put("code", jwt);
        parameters.put("redirect_uri", REDIRECT_URL);

        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String encoding = Base64.getEncoder().encodeToString(keys.getBytes());

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OAUTH_URL))
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + encoding)
                .POST(HttpRequest.BodyPublishers.ofString(form)).build();
        return request;
    }

    private void loadProperties() {
        try {
            properties = loadProperties("/cognito.properties");
            CLIENT_ID = properties.getProperty("client.id");
            LOGIN_URL = properties.getProperty("loginURL");
            REDIRECT_URL = properties.getProperty("redirectURL");
        } catch (IOException e) {
            logger.error("Could not load properties...");
            logger.error("", e);
        } catch (Exception e) {
            logger.error("There was an exception loading properties...");
            logger.error("", e);
        }
    }

    /**
     * This endpoint expects a user ID, and will return the encounters
     * associated with that user ID.
     *
     * @param jwt a JWT from the client of a user
     * @return stringified JSON representing all encounters found
     */
    @GET
    @Path("{jwt}/encounters")
    @Produces("application/json")
    public Response getUserEncounters(@PathParam("jwt") String jwt) {

        //

        return Response.status(200).entity("Hello World").build();

    }

    /**
     * This endpoint expects a user ID, and will return the characters associated with that user.
     *
     * @param id a user ID
     * @return stringified JSON representing all characters found belonging to the user
     */
    @GET
    @Path("{id}/characters")
    @Produces("application/json")
    public Response getUserCharacters(@PathParam("id") int id) {

        User user = dao.getById(id);
        List<Characters> characters = charactersDao.getByUser(user);
        String responseJSON = "";

        try {
            responseJSON = objectMapper.writeValueAsString(characters);
        } catch (Exception e) {
            logger.error("", e);
        }

        return Response.status(200).entity(responseJSON).build();

    }

}
