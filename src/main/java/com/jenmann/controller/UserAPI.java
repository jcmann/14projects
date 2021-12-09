package com.jenmann.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenmann.auth.CognitoJWTParser;
import com.jenmann.auth.CognitoTokenHeader;
import com.jenmann.auth.Keys;
import com.jenmann.auth.TokenResponse;
import com.jenmann.entity.*;
import com.jenmann.persistence.CharactersDao;
import com.jenmann.persistence.EncounterDao;
import com.jenmann.persistence.MonsterDao;
import com.jenmann.persistence.UserDao;
import com.jenmann.util.PropertiesLoader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This endpoint is responsible for handling all data related to users
 * for DMBook. I chose to include the data related to the user's encounters and characters
 * here in this slightly long class on purpose to centralize that processing around the JWTs. Additionally,
 * I was curious about the ability to create an admin interface that would handle all users' data through
 * the decentralized/resource-specific APIs.
 *
 * @author jcmann
 */
@Path("/users")
public class UserAPI implements PropertiesLoader {

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
     * The users API also contains an endpoint that also retrieves monster data to send to the client.
     */
    private MonsterDao monsterDao = new MonsterDao();

    /**
     * A utility object, used for mapping POJOs to JSON strings here.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Log4J2 instance for all logging.
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * A Properties object intended to represent cognito.properties for AWS Cognito integration
     */
    private Properties properties;

    /**
     * The following are all instance variables to be filled in via cognito.properties as it's fed into the
     * properties instance variable, and a laodKeys method for jwks.
     */
    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String OAUTH_URL;
    private String LOGIN_URL;
    private String REDIRECT_URL;
    private String REGION;
    private String POOL_ID;
    Keys jwks;

    /**
     * This helper method is called by each endpoint to reduce code duplication in getting the user. If the user
     * is validated in AWS but not found, it calls a helper method to update the database to include the user, effectively
     * synchronizing AWS/the database.
     *
     * @param jwt The JWT received by the endpoint, an ID token
     * @return null if the user was not found, or a user object if a user was found
     */
    public User userFetcher (String jwt) {
        String username = processJWT(jwt);
        User user = null;

        if (username != null) {
            // this means the JWT was valid so the user should be in the database
            user = dao.getByUsername(username);

            if (user == null) {
                // This means the user's db record was not created yet, so fix that
                user = createNewUser(username);
            }
        }

        // After all that, the user will be validated + in the db, or null if they don't exist
        return user;
    }

    /**
     * This helper method updates the status code in the event the object mapping process failed.
     *
     * @param responseJSON the objectMapper's produced String, which will either be a valid object or a fail text
     * @return the status code as an integer
     */
    public int determineStatusCode(String responseJSON) {
        return (responseJSON.equals("Object mapping failed.")) ? 404 : 200;
    }

    /**
     * This method is used to turn any passed in object into stringified JSON using the object mapper.
     *
     * @param objectToMap any object that needs to be run through the objectmapper
     * @return a String of the json response after being mapped, or an error message if the objectMapper fails
     */
    public String jsonFormatter(Object objectToMap) {
        String responseJSON = "";
        try {
            responseJSON = objectMapper.writeValueAsString(objectToMap);
        } catch (Exception e) {
            logger.error("", e);
            responseJSON = "Object mapping failed.";
        }

        return responseJSON;

    }

    /**
     * If a user exists in AWS, but not in the database, the user should be created, which is done in this method.
     *
     * @param username the username, processed out of the original jwt, of the user to create
     * @return the User object created
     */
    public User createNewUser(String username) {
        User newUser = new User();
        newUser.setUsername(username);
        int newUserID = dao.insert(newUser);
        return newUser;
    }

    /**
     * This endpoint is sort of tailor-made for my React frontend, which initializes all data belonging to a user
     * when the app is first opened. Async JS makes it tricky to request several endpoints' worth of data in one
     * handler method, so I wrote an endpoint to return all the necessary data in one go.
     *
     * @param jwt a JWT representing the AWS Amplify/Cognito ID Token
     * @return If successful, a 200 and a JSON object containing all the user's characters and encounters, as well as all monsters.
     *      Otherwise, 404s represent failures at this endpoint.
     */
    @GET
    @Path("{jwt}/all")
    @Produces("application/json")
    public Response getAllUserData(@PathParam("jwt") String jwt) {
        String responseJSON = "";
        int statusCode = 0;
        User user = null;
        user = userFetcher(jwt);

        if (user != null) {
            List<Encounter> encounters = encounterDao.getByUser(user);
            List<Characters> characters = charactersDao.getByUser(user);

            /* This is slightly different because the 5E API returns different data for their /monsters endpoint
                // than for each individual monster. This /:jwt/all endpoint only requires the name for its intended
                 client purposes.*/
            List<GetAllResponseItem> monsters = monsterDao.getAllMonsters().getResults();

            // Format response into AllUserData entity for object mapping purposes
            UserData userData = new UserData();
            userData.setEncounters(encounters);
            userData.setCharacters(characters);
            userData.setMonsters(monsters);

            // send this to the objectMapper
            responseJSON = jsonFormatter(userData);
            statusCode = determineStatusCode(responseJSON);
        } else {
            // send an error
            responseJSON = "The user was not found.";
            statusCode = 404;
        }

        return Response.status(statusCode).entity(responseJSON).build();

    }

    /**
     * This endpoint expects a user ID, and will return the encounters
     * associated with that user ID.
     *
     * @param jwt a JWT from the client of a user
     * @return A 200 response with stringified JSON representing all encounters found, or a 404 if any step fails.
     */
    @GET
    @Path("{jwt}/encounters")
    @Produces("application/json")
    public Response getUserEncounters(@PathParam("jwt") String jwt) {
        String responseJSON = "";
        int statusCode = 0;
        User user = null;
        user = userFetcher(jwt);

        if (user != null) {
            List<Encounter> encounters = encounterDao.getByUser(user);
            responseJSON = jsonFormatter(encounters);
            statusCode = determineStatusCode(responseJSON);
        } else {
            responseJSON = "The user was not found";
            statusCode = 404;
        }

        return Response.status(statusCode).entity(responseJSON).build();

    }

    /**
     * This endpoint handles post requests that seek to add a new character belonging to a given user. It expects
     * the body to be sent as a JSON String, which is then parsed here.
     *
     * @param jwt the ID Token JWT for the currently logged in user
     * @param body a JSON String containing all form data for the new character (except user id)
     * @return a 200 response if post was successful, 404 if it failed at any point
     */
    @POST
    @Path("{jwt}/encounters")
    @Consumes("application/json")
    public Response addNewEncounter(@PathParam("jwt") String jwt,  String body) {
        String responseJSON = "";
        int statusCode = 0;
        User user = null;
        user = userFetcher(jwt);

        if (user != null) {
            // This means the user is validated and exists in the database, so work can be done
            Encounter encounter = null;
            try {
                encounter = objectMapper.readValue(body, Encounter.class);
                encounter.setUser(user);
            } catch (Exception e) {
                logger.error("", e);
            }

            int newEncounterID = encounterDao.insert(encounter);
            encounter.setId(newEncounterID); // reformat to return

            responseJSON = jsonFormatter(encounter);
            statusCode = determineStatusCode(responseJSON);

        } else {
            responseJSON = "The user was not found";
            statusCode = 404;
        }

        return Response.status(statusCode).entity(responseJSON).build();

    }

    /**
     * This endpoint handles all PUT requests for a user's encounter.
     *
     * @param jwt An AWS Amplify/Cognito ID Token for a currently logged in user
     * @param idToUpdate The ID of the encounter to perform the update on
     * @param body Request body should contain all data for the encounter
     * @return An int, 1, if update was successful, or a 404 if it failed at any step
     */
    @PUT
    @Path("{jwt}/encounters/{idToUpdate}")
    public Response editEncounter(@PathParam("jwt") String jwt, @PathParam("idToUpdate") int idToUpdate, String body) {
        String responseJSON = "";
        int statusCode = 0;
        User user = userFetcher(jwt);

        if (user != null) {
            // This means the user is validated and exists in the database, so work can be done
            Encounter encounter = readEncounterValue(body, user);

            if (encounter == null) {
                responseJSON = "Could not update encounter.";
                statusCode = 404;
            } else {
                responseJSON = "Successfully edited encounter";
                statusCode = 200;
            }

        } else {
            responseJSON = "The user was not found.";
            statusCode = 404;
        }

        return Response.status(statusCode).entity(responseJSON).build();

    }

    /**
     * This endpoint handles all requests to delete a user's given encounter given an ID.
     *
     * Sources:
     * - https://stackoverflow.com/questions/12695268/verify-create-update-delete-successfully-executed-in-hibernate
     * @param jwt An AWS Cognito/Amplify ID Token for a currently signed in user
     * @param idToDelete An ID of an encounter belonging to the user matching the JWT
     * @return a 200 if successfully deleted, a 404 if not
     */
    @DELETE
    @Path("{jwt}/encounters/{id}")
    public Response deleteEncounterByID(@PathParam("jwt") String jwt, @PathParam("id") int idToDelete) {

        // TODO implement jwt validation

        // Run a select to get the encounter from the database if it exists
        Encounter encounterToDelete = encounterDao.getById(idToDelete);

        // If the encounter is null it was not found, return a 404
        if (encounterToDelete == null) {
            return Response.status(404).entity("Encounter not found. Invalid ID.").build();
        } else {
            try {
                encounterDao.delete(encounterToDelete);
            } catch (HibernateException e) {
                return Response.status(404).entity("Encounter was found, but could not be deleted").build();
            }

            return Response.status(200).entity("Successfully deleted encounter.").build();
        }

    }

    /**
     * This endpoint expects a user ID token as a JWT, and will return the characters associated with that user.
     *
     * @param jwt the ID token for a logged in user
     * @return A 200 response with stringified JSON representing all characters found belonging to the user, or a 404
     */
    @GET
    @Path("{jwt}/characters")
    @Produces("application/json")
    public Response getUserCharacters(@PathParam("jwt") String jwt) {
        String responseJSON = "";
        int statusCode = 0;
        User user = null;
        user = userFetcher(jwt);

        if (user != null) {
            List<Characters> characters = charactersDao.getByUser(user);
            responseJSON = jsonFormatter(characters);
            statusCode = determineStatusCode(responseJSON);
        } else {
            responseJSON = "The user was not found";
            statusCode = 404;
        }

        return Response.status(statusCode).entity(responseJSON).build();
    }

    /**
     * This endpoint handles post requests that seek to add a new character belonging to a given user. It expects
     * the body to be sent as a JSON String, which is then parsed here.
     *
     * @param jwt the ID Token JWT for the currently logged in user
     * @param body a JSON String containing all form data for the new character (except user id)
     * @return A 200 if successfully added, or a 404 if not
     */
    @POST
    @Path("{jwt}/characters")
    @Consumes("application/json")
    public Response addNewCharacter(@PathParam("jwt") String jwt,  String body) {
        String responseJSON = "";
        int statusCode = 0;
        User user = null;
        user = userFetcher(jwt);

        if (user != null) {
            // This means the user is validated and exists in the database, so work can be done
            Characters character = null;
            try {
                character = objectMapper.readValue(body, Characters.class);
                character.setUser(user);
            } catch (Exception e) {
                logger.error("", e);
            }

            int newEncounterID = charactersDao.insert(character);
            character.setId(newEncounterID); // reformat to return

            responseJSON = jsonFormatter(character);
            statusCode = determineStatusCode(responseJSON);

        } else {
            responseJSON = "The user was not found";
            statusCode = 404;
        }

        return Response.status(statusCode).entity(responseJSON).build();
    }

    /**
     * This endpoint handles all PUT requests to update a user's character, specified by ID.
     *
     * @param jwt jwt the ID Token JWT for the currently logged in user
     * @param idToUpdate The ID for the Character to be updated
     * @param body Stringified JSON representing all data about the character to be updated
     * @return a 200 if the update was successful, otherwise a 404
     */
    @PUT
    @Path("{jwt}/characters/{idToUpdate}")
    public Response editCharacter(@PathParam("jwt") String jwt, @PathParam("idToUpdate") int idToUpdate, String body) {
        String responseJSON = "";
        int statusCode = 0;
        User user = userFetcher(jwt);

        if (user != null) {
            // This means the user is validated and exists in the database, so work can be done
            Characters characterToEdit = readCharacterValue(body, user);

            if (characterToEdit == null) {
                responseJSON = "Could not update encounter.";
                statusCode = 404;
            } else {
                responseJSON = "Successfully edited encounter";
                statusCode = 200;
            }

        } else {
            responseJSON = "The user was not found.";
            statusCode = 404;
        }

        return Response.status(statusCode).entity(responseJSON).build();

    }

    /**
     * This endpoint is responsible for deleting a given character by ID that belongs to a given user.
     *
     * @param jwt the ID Token JWT for the currently logged in user
     * @param idToDelete the ID of the character to delete
     * @return a 200 response if the delete was successful, or a 404
     */
    @DELETE
    @Path("{jwt}/characters/{id}")
    public Response deleteCharacterByID(@PathParam("jwt") String jwt, @PathParam("id") int idToDelete) {

        // TODO add jwt validation

        // Run a select to get the encounter from the database if it exists
        Characters characterToDelete = charactersDao.getById(idToDelete);

        // If the encounter is null it was not found, return a 404
        if (characterToDelete == null) {
            return Response.status(404).entity("Character not found. Invalid ID.").build();
        } else {
            try {
                charactersDao.delete(characterToDelete);
            } catch (HibernateException e) {
                return Response.status(404).entity("Character was found, but could not be deleted").build();
            }

            return Response.status(200).entity("Successfully deleted character.").build();
        }

    }

    /**
     * This is a helper method used for all Users API endpoints. A JWT that contains a valid, logged-in
     * user must be passed as the first parameter to access any DMBook data. Each endpoint must, then, validate
     * the JWT provided from the client before providing any data.
     *
     * @param jwt The JWT, passed from the endpoint, to validate
     * @return username of the validated user, or null if no validated user was found
     */
    public String processJWT(String jwt) {
        String username = null;
        loadProperties();
        loadKey();

        if (jwt == null) {
            logger.error("The JWT provided was null.");
        } else {
            try {
                username = validate(jwt);
            } catch (IOException e) {
                logger.error("There was an IO Exception while trying to process the auth request.");
                logger.error("", e);
                // TODO error handling
            } catch (Exception e) {
                logger.error("Something went wrong trying to process the auth request.");
                logger.error("", e);
            }
        }

        return username;
    }

    /**
     * This method takes in a JWT ID Token, intended to be received from SPA frontend client. It handles the
     * parsing and validation of the JWT. If the validation is successful, it will return the username of the
     * validated user. If it is unsuccessful, the backend currently catches an exception. This is a // TODO as well.
     *
     * @param jwtString a JWT ID Token from AWS Cognito
     * @return if user is validated and logged in, will return a username.
     * @throws IOException
     */
    private String validate(String jwtString) throws IOException {
        CognitoTokenHeader tokenHeader = objectMapper.readValue(CognitoJWTParser.getHeader(jwtString).toString(), CognitoTokenHeader.class);

        // Header should have kid and alg- https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-the-id-token.html
        logger.debug("TOKEN HEADER KID: " + tokenHeader.getKid());
        logger.debug("TOKEN HEADER ALG: " + tokenHeader.getAlg());
        String keyId = tokenHeader.getKid();
        String alg = tokenHeader.getAlg();

        // Use Key's N and E
        BigInteger modulus = new BigInteger(1, org.apache.commons.codec.binary.Base64.decodeBase64(jwks.getKeys().get(0).getN()));
        BigInteger exponent = new BigInteger(1, org.apache.commons.codec.binary.Base64.decodeBase64(jwks.getKeys().get(0).getE()));

        // TODO the following is "happy path", what if the exceptions are caught?
        // Create a public key
        PublicKey publicKey = null;
        try {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exponent));
        } catch (InvalidKeySpecException e) {
            logger.error("Invalid Key Error " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Algorithm Error " + e.getMessage(), e);
        }

        // get an algorithm instance
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);

        // Verify ISS field of the token to make sure it's from the Cognito source
        String iss = String.format("https://cognito-idp.%s.amazonaws.com/%s", REGION, POOL_ID);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(iss)
                .withClaim("token_use", "id") // make sure you're verifying id token
                .build();

        // Verify the token
        DecodedJWT jwt = verifier.verify(jwtString);
        String userName = jwt.getClaim("cognito:username").asString();
        logger.debug("here's the username: " + userName);

        logger.debug("here are all the available claims: " + jwt.getClaims());

        return userName;
    }

    /**
     * Gets the JSON Web Key Set (JWKS) for the user pool from cognito and loads it
     * into objects for easier use.
     *
     * JSON Web Key Set (JWKS) location: https://cognito-idp.{region}.amazonaws.com/{userPoolId}/.well-known/jwks.json
     * Demo url: https://cognito-idp.us-east-2.amazonaws.com/us-east-2_XaRYHsmKB/.well-known/jwks.json
     *
     */
    private void loadKey() {

        try {
            URL jwksURL = new URL(String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", REGION, POOL_ID));
            File jwksFile = new File("jwks.json");
            FileUtils.copyURLToFile(jwksURL, jwksFile);
            jwks = objectMapper.readValue(jwksFile, Keys.class);
            logger.debug("Keys are loaded. Here's e: " + jwks.getKeys().get(0).getE());
        } catch (IOException ioException) {
            logger.error("Cannot load json..." + ioException.getMessage(), ioException);
        } catch (Exception e) {
            logger.error("Error loading json" + e.getMessage(), e);
        }
    }

    /**
     * Utility method to load properties from /cognito.properties (in Resource directory) for use
     * in the API.
     */
    private void loadProperties() {
        try {
            properties = loadProperties("/cognito.properties");
            CLIENT_ID = properties.getProperty("client.id");
            LOGIN_URL = properties.getProperty("loginURL");
            REDIRECT_URL = properties.getProperty("redirectURL");
            CLIENT_SECRET = properties.getProperty("client.secret");
            OAUTH_URL = properties.getProperty("oauthURL");
            LOGIN_URL = properties.getProperty("loginURL");
            REGION = properties.getProperty("region");
            POOL_ID = properties.getProperty("poolId");
        } catch (IOException e) {
            logger.error("Could not load properties...");
            logger.error("", e);
        } catch (Exception e) {
            logger.error("There was an exception loading properties...");
            logger.error("", e);
        }
    }

    public Encounter readEncounterValue(String body, User user) {
        Encounter encounter = null;
        try {
            encounter = objectMapper.readValue(body, Encounter.class);
            encounter.setUser(user);
            encounterDao.saveOrUpdate(encounter);
        } catch (HibernateException e) {
            logger.info("Failed to save or update.");
            logger.error("", e);
        } catch (Exception e) {
            logger.error("", e);
        }

        return encounter;
    }

    public Characters readCharacterValue(String body, User user) {
        Characters character = null;

        try {
            character = objectMapper.readValue(body, Characters.class);
            character.setUser(user);
            charactersDao.saveOrUpdate(character);
        } catch (HibernateException e) {
            logger.info("Failed to save or update.");
            logger.error("", e);
        } catch (Exception e) {
            logger.error("", e);
        }

        return character;
    }

}
