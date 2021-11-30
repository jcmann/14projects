package com.jenmann.entity;

import java.util.List;

/**
 * This class contains a list of all monsters (via the DND 5e API) as well as all characters and encounters
 * belonging to a validated user. The data is supplied via the UserAPI and appropriate DAOs. This class
 * exists solely to map them to something that can be converted to JSON easily.
 */
public class UserData {

    private List<Characters> characters;
    private List<Encounter> encounters;
    private List<GetAllResponseItem> monsters;

    public List<Characters> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Characters> characters) {
        this.characters = characters;
    }

    public List<Encounter> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<Encounter> encounters) {
        this.encounters = encounters;
    }

    public List<GetAllResponseItem> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<GetAllResponseItem> monsters) {
        this.monsters = monsters;
    }
}
