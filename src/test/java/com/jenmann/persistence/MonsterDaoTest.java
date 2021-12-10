package com.jenmann.persistence;

import com.jenmann.entity.GetAllResponse;
import com.jenmann.entity.Monster;
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
     * A MonsterDao that is re-initialized before each test.
     */
    private MonsterDao dao = new MonsterDao();

    /**
     * This tests the getAllMonsters method of the MonsterDao. This would hypothetically require
     * regular updates if the API were to be changed, as the count or alphabetical index may change.
     */
    @Test
    public void getAllMonstersSuccess() {
        logger.info("Starting test getAllMonstersSuccess");
        GetAllResponse gar = dao.getAllMonsters();
        assertEquals(332, gar.getCount());
        assertEquals("Aboleth", gar.getResults().get(0).getName());
    }

    @Test
    public void getMonsterByIndexSuccess() {
        logger.info("Starting test getMonsterByIndexSuccess.");
        Monster aboleth = dao.getMonsterByIndex("aboleth");
        assertEquals("Aboleth", aboleth.getName());
        assertEquals("lawful evil", aboleth.getAlignment());
    }
}
