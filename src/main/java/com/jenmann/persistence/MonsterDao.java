package com.jenmann.persistence;
;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenmann.entity.Monster;
import com.jenmann.entity.GetAllResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * This DAO specifically handles the retrieval of monster data from the DND API. This didn't really work
 * with the generic dao because it has two very specific, very not-database-based methods.
 *
 * @author jcmann
 */
public class MonsterDao {

    /**
     * A Log4J2 logger
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * The DND 5e API's generic endpoints that fetch all of a certain type of object do not just return
     * a collection of those objects. They return a JSON object with a count property, and a results property. So,
     * this DAO's getAll method returns a GetAllResponse object to allow the developer to use both pieces
     * of data if needed -- even though in this app, only the results are used.
     *
     * @return a GetAllResponseItem with all the data returned from the monsters endpoint
     */
    public GetAllResponse getAllMonsters() {
        logger.info("Getting all monsters...");
        Client client = ClientBuilder.newClient();
        WebTarget target =
                client.target("https://www.dnd5eapi.co/api/monsters");
        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);
        ObjectMapper mapper = new ObjectMapper();
        GetAllResponse gar = new GetAllResponse();

        try {
            gar = mapper.readValue(response, GetAllResponse.class);
        } catch (Exception e) {
            logger.error("", e);
        }

        return gar;
    }

    /**
     *
     * @param index The index, formatted for the API in lower kebab case
     * @return a Monster representing the data returned from the API
     */
    public Monster getMonsterByIndex(String index) {
        logger.info("Getting monster with the index: " + index );
        Client client = ClientBuilder.newClient();
        WebTarget target =
                client.target("https://www.dnd5eapi.co/api/monsters/" + index);
        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);

        ObjectMapper mapper = new ObjectMapper();
        Monster monster = new Monster();

        try {
            monster = mapper.readValue(response, Monster.class);
        } catch (Exception e) {
            logger.error("", e);
        }

        return monster;

    }

}
