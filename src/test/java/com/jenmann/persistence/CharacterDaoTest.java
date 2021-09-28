package com.jenmann.persistence;

import com.jenmann.entity.Character;
import com.jenmann.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterDaoTest {

    CharacterDao dao;

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

}
