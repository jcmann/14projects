package com.jenmann.persistence;

import com.jenmann.entity.Character;
import com.jenmann.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CharacterDaoTest {

    CharacterDao dao;
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Creating the dao.
     */
    @BeforeEach
    void setUp() {
        dao = new CharacterDao();

        Database database = Database.getInstance();
        database.runSQL("cleandb.sql");
    }

    /**
     * Confirms that all three test users are correctly retrieved
     */
    @Test
    public void getAllCharactersSuccess() {
        List<Character> characters = dao.getAllCharacters();
        assertEquals(3, characters.size());
    }

    /**
     * Confirms that new characters are properly inserted into the table
     */
    @Test
    public void insertNewCharacterSuccess() {
        Character newChar = new Character("Mozzy", 5, "Doggo", "Paladin");
        int id = dao.insert(newChar);
        assertNotEquals(0,id);
        Character insertedChar = dao.getById(id);
        assertEquals("Mozzy", insertedChar.getName());
    }

    /**
     * Confirms that a character entry can be updated
     */
    @Test
    public void updateCharacterSuccess() {
        int newLevel = 10;
        Character charToUpdate = dao.getById(1);
        charToUpdate.setLevel(newLevel);
        dao.saveOrUpdate(charToUpdate);

        Character resultingCharacter = dao.getById(1);
        assertEquals(newLevel, resultingCharacter.getLevel());

    }

    /**
     * Confirms that a character entry can be deleted
     */
    @Test
    public void deleteCharacterSuccess() {
        int idToDelete = 1;
        Character charToDelete = dao.getById(idToDelete);
        dao.delete(charToDelete);

        assertNull(dao.getById(idToDelete));
    }

}
