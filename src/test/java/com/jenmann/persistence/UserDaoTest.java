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

public class UserDaoTest {

    GenericDao dao;
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Creating the dao.
     */
    @BeforeEach
    void setUp() {
        logger.info("Starting new UserDao Test.");
        dao = new GenericDao<User>(User.class);

        Database database = Database.getInstance();
        database.runSQL("cleandb.sql");
    }

    /**
     * Confirms that all three test users are correctly retrieved
     */
    @Test
    public void getAllUsersSuccess() {
        logger.info("In test: getAllUsersSuccess.");
        List<User> users = dao.getAll();
        assertEquals(3, users.size());
    }

    /**
     * Confirms that new users are properly inserted into the table
     */
    @Test
    public void insertNewUserSuccess() {
        logger.info("In test: InsertNewUserSuccess.");
        User newUser = new User("user4");
        int id = dao.insert(newUser);
        assertNotEquals(0,id);
        User insertedUser = (User) dao.getById(id);
        assertEquals("user4", insertedUser.getUsername());
    }

    /**
     * Confirms that a user entry can be updated
     */
    @Test
    public void updateUserSuccess() {
        logger.info("In test: updateUserSuccess.");
        User userToUpdate = (User) dao.getById(1);
        dao.saveOrUpdate(userToUpdate);

        User resultingUser = (User) dao.getById(1);
    }

    /**
     * Confirms that a user entry can be deleted
     */
    @Test
    public void deleteUserWithoutCharactersSuccess() {
        logger.info("In test: deleteUserWithoutCharactersSuccess.");
        int idToDelete = 2;
        User userToDelete = (User) dao.getById(idToDelete);
        dao.delete(userToDelete);

        assertNull(dao.getById(idToDelete));
    }

    /**
     * Confirms that a user with corresponding entries
     * in the characters table can be deleted, and that
     * all character entries are also deleted
     */
    @Test
    public void deleteUserWithCharactersSuccess() {
        logger.info("In test: deleteUserWithCharactersSuccess.");
        int idToDelete = 3;
        User userToDelete = (User) dao.getById(idToDelete);

        // Confirm characters exist belonging to this user
        CharactersDao charDao = new CharactersDao();
        List<Characters> characters = charDao.getByUser(userToDelete);
        assertEquals(1, characters.size());

        // Delete the user
        dao.delete(userToDelete);
        assertNull(dao.getById(idToDelete));

        // Confirm characters were also deleted
        List<Characters> charactersAfterDeletion = charDao.getByUser(userToDelete);
        assertEquals(0, charactersAfterDeletion.size());
    }

}
