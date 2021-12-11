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
public class APIFormatUtility <T> {

    private Class<T> type;

    /**
     * A utility object, used for mapping POJOs to JSON strings here.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Log4J2 instance for all logging.
     */
    private final Logger logger = LogManager.getLogger(this.type.getClass());

    public APIFormatUtility(Class<T> type) {
        this.type = type;
    }

    // TODO this is a stretch goal for cleaner code!
    public String jsonFormatHelper(T entity) {
        logger.info("In singular format helper");
        String responseJSON = "";

        try {
            responseJSON = objectMapper.writeValueAsString(entity);
        } catch (Exception e) {
            logger.error("", e);
        }

        return responseJSON;
    }

    public String jsonFormatHelper(List<T> listOfEntities) {
        logger.info("In List format helper");
        String responseJSON = "";

        try {
            responseJSON = objectMapper.writeValueAsString(listOfEntities);
        } catch (Exception e) {
            logger.error("", e);
        }

        return responseJSON;
    }

}
