package com.jenmann.persistence;

import com.jenmann.entity.Characters;
import com.jenmann.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.List;

public class CharactersDao {

    private final Logger logger = LogManager.getLogger(this.getClass());
    SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    /**
     * Gets all characters currently in the database.
     *
     * @return a List of all characters
     */
    public List<Characters> getAllCharacters() {
        logger.info("Getting all characters...");
        Session session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Characters> query = builder.createQuery(Characters.class);
        Root<Characters> root = query.from(Characters.class);
        List<Characters> characters = session.createQuery(query).getResultList();
        session.close();
        logger.info("Returning all characters.");
        return characters;
    }

    /**
     * Gets a character by id
     * @param id character's id to search by
     * @return a character
     */
    public Characters getById(int id) {
        Session session = sessionFactory.openSession();
        Characters character = session.get(Characters.class, id);
        session.close();
        return character;
    }

    /**
     * Gets a character by the user's id
     * @param userId user ID to search by
     * @return a list of characters
     */
    public List<Characters> getByUserId(int userId) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Characters> query = builder.createQuery(Characters.class);
        Root<Characters> root = query.from(Characters.class);
        query.select(root).where(builder.equal(root.get("user_id"), userId));
        List<Characters> characters = session.createQuery(query).getResultList();
        session.close();

        return characters;
    }

    /**
     * Gets all characters in the database by the user to whom they belong
     *
     * @param user a User object representing the user whose characters you're searching for
     * @return a List of all characters belonging to that user
     */
    public List<Characters> getByUser(User user) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Characters> query = builder.createQuery(Characters.class);
        Root<Characters> root = query.from(Characters.class);
        query.select(root).where(builder.equal(root.get("user"), user));
        List<Characters> characters = session.createQuery(query).getResultList();
        session.close();

        return characters;
    }

    /**
     * Save or update a character in the database
     *
     * @param character the Characters object representing all the character's new data
     */
    public void saveOrUpdate(Characters character) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(character);
        transaction.commit();
        session.close();
    }

    /**
     * Insert a new character into the database
     *
     * @param character a Characters (character) to insert into the database
     * @return an int representing the id of the newly inserted character
     */
    public int insert(Characters character) {
        int id = 0;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        id = (int)session.save(character);
        transaction.commit();
        session.close();
        return id;
    }

    /**
     * Delete a character in the database
     *
     * @param character a Characters object representing the character to delete
     */
    public void delete(Characters character) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(character);
        transaction.commit();
        session.close();
    }


}
