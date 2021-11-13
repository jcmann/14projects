package com.jenmann.persistence;

import com.jenmann.entity.Characters;
import com.jenmann.entity.Encounter;
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
