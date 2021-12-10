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
 * This test suite tests the User class using a generic dao
 *
 * @author jcmann
 */
public class UserDaoTest {

    /**
     * A generic dao initialized to a user dao before each test
     */
    GenericDao dao;

    /**
     * A Log4J2 logger.
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Creates the dao and runs the sql reset script before each test
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
     * The generic dao has a method to get a User object based on username, specifically
     * for when it's used for the User class. This verifies that it works.
     */
    @Test
    public void getUserByUsernameSuccess() {
        logger.info("In test: getUserByUsernameSuccess.");
        String username = "user1";
        User user = dao.getByUsername(username);

        assertNotNull(user);
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
     * Confirms that a user entry can be updated. There probably
     * isn't a use-case for this, but just in case it's here.
     */
    @Test
    public void updateUserSuccess() {
        logger.info("In test: updateUserSuccess.");
        User userToUpdate = (User) dao.getById(1);
        String originalUsername = userToUpdate.getUsername();
        String newUsername = "newUsername";

        userToUpdate.setUsername(newUsername);

        dao.saveOrUpdate(userToUpdate);
        User resultingUser = (User) dao.getById(1);
        assertNotNull(resultingUser);
        assertNotEquals(originalUsername, resultingUser.getUsername());
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
        GenericDao charDao = new GenericDao<Characters>(Characters.class);
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
