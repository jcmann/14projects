package com.jenmann.controller;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the entire DMBook API.
 *
 * @author jcmann
 */
@ApplicationPath("/api")
public class API extends Application {

    /**
     * Establishes endpoints for the API
     *
     * @return hashset of endpoints
     */
    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        h.add(CharacterAPI.class);
        h.add(MonsterAPI.class);
        return h;
    }

}
