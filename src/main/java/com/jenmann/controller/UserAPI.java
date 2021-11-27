package com.jenmann.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import com.jenmann.entity.Characters;
import com.jenmann.entity.Encounter;
import com.jenmann.entity.User;
import com.jenmann.persistence.CharactersDao;
import com.jenmann.persistence.EncounterDao;
import com.jenmann.persistence.UserDao;
import com.jenmann.util.PropertiesLoader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
 * for DMBook.
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

    @GET
    @Path("username/{username}")
    @Produces("application/json")
    public Response getByUsername(@PathParam("username") String username) {
        User user = dao.getByUsername(username);
        String responseJSON = "";

        try {
            responseJSON = objectMapper.writeValueAsString(user);
        } catch (Exception e) {
            logger.error(e.getStackTrace()); // TODO clean up logs
        }

        return Response.status(200).entity(responseJSON).build();
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

        String responseJSON = "";

        // Get the username
        String username = processJWT(jwt);

        if (username == null) {
            // return error
        } else {
            // This means the user is legit
            User user = dao.getByUsername(username);
            if (user == null) {
                // If the user does not exist, add their record and return an appropriate message
            } else {
                // If the user exists get their encounters
                List<Encounter> encounters = encounterDao.getByUser(user);

                try {
                    responseJSON = objectMapper.writeValueAsString(encounters);
                } catch (Exception e) {
                    logger.error("", e);
                }

            }

        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(responseJSON).build();

    }

    /**
     * This endpoint expects a user ID, and will return the characters associated with that user.
     *
     * @param id a user ID
     * @return stringified JSON representing all characters found belonging to the user
     */
    @GET
    @Path("{jwt}/characters")
    @Produces("application/json")
    public Response getUserCharacters(@PathParam("jwt") String jwt) {

        String responseJSON = "";

        // Get the username
        String username = processJWT(jwt);

        if (username == null) {
            // return error
        } else {
            // This means the user is legit
            User user = dao.getByUsername(username);
            if (user == null) {
                // If the user does not exist, add their record and return an appropriate message
            } else {
                // If the user exists get their encounters
                List<Characters> characters = charactersDao.getByUser(user);

                try {
                    responseJSON = objectMapper.writeValueAsString(characters);
                } catch (Exception e) {
                    logger.error("", e);
                }

            }

        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(responseJSON).build();
    }

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
        loadProperties();
        loadKey();

        if (jwt == null) {
            // TODO figure out error handling
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
}
