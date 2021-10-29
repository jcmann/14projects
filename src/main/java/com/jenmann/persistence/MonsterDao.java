package com.jenmann.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenmann.entity.Monster;
import com.jenmann.entity.GetAllResponse;
import com.jenmann.entity.GetAllResponseItem;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class MonsterDao {

    private final Logger logger = LogManager.getLogger(this.getClass());
    SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    public GetAllResponse getAllMonsters() {
        logger.info("Getting all monsters...");
        Client client = ClientBuilder.newClient();
        WebTarget target =
                client.target("https://www.dnd5eapi.co/api/monsters");
        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);
        ObjectMapper mapper = new ObjectMapper();
        GetAllResponse gar = new GetAllResponse();

        try {
            logger.debug("++++++++++++++++++++++++++++++++++++++++++++++");
            gar = mapper.readValue(response, GetAllResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return gar;
    }



}
