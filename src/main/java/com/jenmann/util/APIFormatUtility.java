package com.jenmann.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * This utility class helps standardize the formatting of API responses into
 * stringified versions of certain response types.
 *
 * @author jcmann
 */
public class APIFormatUtility {


    /**
     * A utility object, used for mapping POJOs to JSON strings here.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Log4J2 instance for all logging.
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * This method is used to turn any passed in object into stringified JSON using the object mapper. Because
     * it expects an Object typed parameter, it handles both collections and individual entities.
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
     * This helper method updates the status code in the event the object mapping process failed.
     *
     * @param responseJSON the objectMapper's produced String, which will either be a valid object or a fail text
     * @return the status code as an integer
     */
    public int determineStatusCode(String responseJSON) {
        return (responseJSON.equals("Object mapping failed.")) ? 404 : 200;
    }

}
