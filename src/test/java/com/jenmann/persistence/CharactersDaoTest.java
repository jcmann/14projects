package com.jenmann.persistence;

import com.jenmann.entity.Characters;
import com.jenmann.entity.User;
import com.jenmann.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * This test suite utilizes the generic DAO to test all functionality
 * related to the characters database table
 *
 * @author jcmann
 */
public class CharactersDaoTest {

    /**
     * A generic dao object that's initialized to a fresh character dao
     * before each test.
     */
    GenericDao dao;

    /**
     * A Log4J2 logger.
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Creates the dao and cleans the test database before each test.
     */
    @BeforeEach
    void setUp() {
        logger.info("Starting new CharactersDao Test.");
        dao = new GenericDao<Characters>(Characters.class);

        Database database = Database.getInstance();
        database.runSQL("cleandb.sql");
    }

    /**
     * Confirms that all three test users are correctly retrieved
     */
    @Test
    public void getAllCharactersSuccess() {
        logger.info("In test: getAllCharactersSuccess.");
        List characters = dao.getAll();
        assertEquals(3, characters.size());
    }

    /**
     * Confirms that you can search by the user who the characters belong to
     */
    @Test
    public void getCharactersByUserId() {
        logger.info("In test: getCharactersByUserId.");
        int userId = 1;
        GenericDao userDao = new GenericDao<User>(User.class);
        User user = (User) userDao.getById(1);

        List<Characters> chars = dao.getByUser(user);

        assertEquals(2, chars.size());

    }

    /**
     * Confirms that new characters are properly inserted into the table
     */
    @Test
    public void insertNewCharacterSuccess() {
        logger.info("In test: insertNewCharacterSuccess.");
        // Get user
        GenericDao userDao = new GenericDao<User>(User.class);
        User user = (User) userDao.getById(1);

        Characters newChar = new Characters("Mozzy", 5, "Doggo", "Paladin", user);
        int id = dao.insert(newChar);
        assertNotEquals(0,id);
        Characters insertedChar = (Characters) dao.getById(id);
        assertEquals("Mozzy", insertedChar.getName());
    }

    /**
     * Confirms that a character entry can be updated
     */
    @Test
    public void updateCharacterSuccess() {
        logger.info("In test: updateCharacterSuccess.");
        int newLevel = 10;
        Characters charToUpdate = (Characters) dao.getById(1);
        charToUpdate.setLevel(newLevel);
        dao.saveOrUpdate(charToUpdate);

        Characters resultingCharacter = (Characters) dao.getById(1);
        assertEquals(newLevel, resultingCharacter.getLevel());

    }

    /**
     * Confirms that a character entry can be deleted
     */
    @Test
    public void deleteCharacterSuccess() {
        logger.info("In test: deleteCharacterSuccess.");
        int idToDelete = 1;
        Characters charToDelete = (Characters) dao.getById(idToDelete);
        dao.delete(charToDelete);

        assertNull(dao.getById(idToDelete));
    }

    /**
     * This tests the getByPropertyEquals method to verify the characters table can be searched
     * by name
     */
    @Test
    public void getByNameSuccess() {
        logger.info("In test: getByNameSuccess");
        String name = "Rigby";
        Characters character = (Characters) dao.getByPropertyEqual("name", name).get(0);
        assertNotNull(character);
        assertEquals(name, character.getName());
    }

}
