package com.jenmann.persistence;

import com.jenmann.entity.Character;
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

public class CharacterDao {

    private final Logger logger = LogManager.getLogger(this.getClass());
    SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    public List<Character> getAllCharacters() {
        logger.info("Getting all characters...");
        Session session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Character> query = builder.createQuery(Character.class);
        Root<Character> root = query.from(Character.class);
        List<Character> characters = session.createQuery(query).getResultList();
        session.close();
        logger.info("Returning all characters.");
        return characters;
    }

    public void saveOrUpdate(Character character) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(character);
        transaction.commit();
        session.close();
    }

    public int insert(Character character) {
        int id = 0;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        id = (int)session.save(character);
        transaction.commit();
        session.close();
        return id;
    }

    public void delete(Character character) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(character);
        transaction.commit();
        session.close();
    }


}
