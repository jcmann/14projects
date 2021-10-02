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

    UserDao dao;
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Creating the dao.
     */
    @BeforeEach
    void setUp() {
        dao = new UserDao();

        Database database = Database.getInstance();
        database.runSQL("cleandb.sql");
    }

    /**
     * Confirms that all three test users are correctly retrieved
     */
    @Test
    public void getAllUsersSuccess() {
        List<User> users = dao.getAllUsers();
        assertEquals(3, users.size());
    }

    /**
     * Confirms that new users are properly inserted into the table
     */
    @Test
    public void insertNewUserSuccess() {

        User newUser = new User("user4", "pass4");
        int id = dao.insert(newUser);
        assertNotEquals(0,id);
        User insertedUser = dao.getById(id);
        assertEquals("user4", insertedUser.getUsername());
    }

    /**
     * Confirms that a character entry can be updated
     */
    @Test
    public void updateCharacterSuccess() {
        String newPassword = "newPassword";
        User userToUpdate = dao.getById(1);
        userToUpdate.setPassword(newPassword);
        dao.saveOrUpdate(userToUpdate);

        User resultingUser = dao.getById(1);
        assertEquals(newPassword, resultingUser.getPassword());

    }

    /**
     * Confirms that a character entry can be deleted
     */
    @Test
    public void deleteCharacterSuccess() {
        int idToDelete = 1;
        User userToDelete = dao.getById(idToDelete);
        dao.delete(userToDelete);

        assertNull(dao.getById(idToDelete));
    }

}
