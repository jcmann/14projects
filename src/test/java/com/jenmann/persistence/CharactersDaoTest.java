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

public class CharactersDaoTest {

    CharactersDao dao;
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Creating the dao.
     */
    @BeforeEach
    void setUp() {
        logger.info("Starting new CharactersDao Test.");
        dao = new CharactersDao();

        Database database = Database.getInstance();
        database.runSQL("cleandb.sql");
    }

    /**
     * Confirms that all three test users are correctly retrieved
     */
    @Test
    public void getAllCharactersSuccess() {
        logger.info("In test: getAllCharactersSuccess.");
        List<Characters> characters = dao.getAllCharacters();
        assertEquals(3, characters.size());
    }

    /**
     * Confirms that you can search by the user who the characters belong to
     */
    @Test
    public void getCharactersByUserId() {
        logger.info("In test: getCharactersByUserId.");
        int userId = 1;
        UserDao userDao = new UserDao();
        User user = userDao.getById(1);

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
        UserDao userDao = new UserDao();
        User user = userDao.getById(1);

        Characters newChar = new Characters("Mozzy", 5, "Doggo", "Paladin", user);
        int id = dao.insert(newChar);
        assertNotEquals(0,id);
        Characters insertedChar = dao.getById(id);
        assertEquals("Mozzy", insertedChar.getName());
    }

    /**
     * Confirms that a character entry can be updated
     */
    @Test
    public void updateCharacterSuccess() {
        logger.info("In test: updateCharacterSuccess.");
        int newLevel = 10;
        Characters charToUpdate = dao.getById(1);
        charToUpdate.setLevel(newLevel);
        dao.saveOrUpdate(charToUpdate);

        Characters resultingCharacter = dao.getById(1);
        assertEquals(newLevel, resultingCharacter.getLevel());

    }

    /**
     * Confirms that a character entry can be deleted
     */
    @Test
    public void deleteCharacterSuccess() {
        logger.info("In test: deleteCharacterSuccess.");
        int idToDelete = 1;
        Characters charToDelete = dao.getById(idToDelete);
        dao.delete(charToDelete);

        assertNull(dao.getById(idToDelete));
    }

}
