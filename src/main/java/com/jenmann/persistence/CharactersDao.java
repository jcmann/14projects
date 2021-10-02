package com.jenmann.persistence;

import com.jenmann.entity.Characters;
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

    public void saveOrUpdate(Characters character) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(character);
        transaction.commit();
        session.close();
    }

    public int insert(Characters character) {
        int id = 0;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        id = (int)session.save(character);
        transaction.commit();
        session.close();
        return id;
    }

    public void delete(Characters character) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(character);
        transaction.commit();
        session.close();
    }


}
