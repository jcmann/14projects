package com.jenmann.persistence;

import com.jenmann.entity.Characters;
import com.jenmann.entity.Encounter;
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

public class EncounterDao {

    private final Logger logger = LogManager.getLogger(this.getClass());
    SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    /**
     * Gets all encounters currently in the database.
     *
     * @return a List of all encounters
     */
    public List<Encounter> getAllEncounters() {
        logger.info("Getting all encounters...");
        Session session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Encounter> query = builder.createQuery(Encounter.class);
        Root<Encounter> root = query.from(Encounter.class);
        List<Encounter> encounters = session.createQuery(query).getResultList();
        session.close();
        logger.info("Returning all encounters.");
        return encounters;
    }

    /**
     * Gets an encounter by ID
     *
     * @param id the encounter ID to search by
     * @return an encounter object
     */
    public Encounter getById(int id) {
        Session session = sessionFactory.openSession();
        Encounter encounter = session.get(Encounter.class, id);
        session.close();
        return encounter;
    }

    /**
     * Gets an encounter by the user's id
     * @param userId user ID to search by
     * @return a list of encounters
     */
    public List<Encounter> getByUserId(int userId) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Encounter> query = builder.createQuery(Encounter.class);
        Root<Encounter> root = query.from(Encounter.class);
        query.select(root).where(builder.equal(root.get("user_id"), userId));
        List<Encounter> encounters = session.createQuery(query).getResultList();
        session.close();

        return encounters;
    }

    /**
     * Gets all encounters in the database by the user to whom they belong
     *
     * @param user a User object representing the user whose encounters you're searching for
     * @return a List of all encounters belonging to that user
     */
    public List<Encounter> getByUser(User user) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Encounter> query = builder.createQuery(Encounter.class);
        Root<Encounter> root = query.from(Encounter.class);
        query.select(root).where(builder.equal(root.get("user"), user));
        List<Encounter> encounters = session.createQuery(query).getResultList();
        session.close();

        return encounters;
    }

    /**
     * Save or update an encounter in the database
     *
     * @param encounter the Encounter object representing all the encounter's new data
     */
    public void saveOrUpdate(Encounter encounter) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(encounter);
        transaction.commit();
        session.close();
    }

    /**
     * Insert a new encounter into the database
     *
     * @param encounter an Encounter to insert into the database
     * @return an int representing the id of the newly inserted encounter
     */
    public int insert(Encounter encounter) {
        int id = 0;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        id = (int)session.save(encounter);
        transaction.commit();
        session.close();
        return id;
    }

    /**
     * Delete an encounter in the database
     *
     * @param encounter an Encounter object representing the encounter to delete
     */
    public void delete(Encounter encounter) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(encounter);
        transaction.commit();
        session.close();
    }


}
