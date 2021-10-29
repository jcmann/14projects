package com.jenmann.persistence;

import com.jenmann.entity.GetAllResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This tests the MonsterDao class, which both retrieves all monsters in the DND 5e API, and specific monsters.
 *
 * @author jcmann
 */
public class MonsterDaoTest {

    /**
     * An instance of a Log4J2 logger for info and debugging.
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * This tests the getAllMonsters method of the MonsterDao. This would hypothetically require
     * regular updates if the API were to be changed, as the count or alphabetical index may change.
     */
    @Test
    void getAllMonstersSuccess() {
        logger.info("Starting test getAllMonstersSuccess");
        MonsterDao dao = new MonsterDao();
        GetAllResponse gar = dao.getAllMonsters();
        assertEquals(332, gar.getCount());
        assertEquals("Aboleth", gar.getResults().get(0).getName());
    }
}
