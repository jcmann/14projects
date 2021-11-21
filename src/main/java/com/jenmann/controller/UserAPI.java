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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
                username = validate(tokenResponse);
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
    private String validate(TokenResponse tokenResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CognitoTokenHeader tokenHeader = mapper.readValue(CognitoJWTParser.getHeader(tokenResponse.getIdToken()).toString(), CognitoTokenHeader.class);

        // Header should have kid and alg- https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-the-id-token.html
        String keyId = tokenHeader.getKid();
        String alg = tokenHeader.getAlg();

        // todo pick proper key from the two - it just so happens that the first one works for my case
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
        DecodedJWT jwt = verifier.verify(tokenResponse.getIdToken());
        String userName = jwt.getClaim("cognito:username").asString();
        logger.debug("here's the username: " + userName);

        logger.debug("here are all the available claims: " + jwt.getClaims());

        return userName;
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
