package com.jenmann.entity;

import java.util.List;

/**
 * This class contains a list of all monsters (via the DND 5e API) as well as all characters and encounters
 * belonging to a validated user. The data is supplied via the UserAPI and appropriate DAOs. This class
 * exists solely to map them to something that can be converted to JSON easily.
 */
public class UserData {

    /**
     * A list of the user's characters
     */
    private List<Characters> characters;

    /**
     * A list of the user's encounters
     */
    private List<Encounter> encounters;

    /**
     * For the sake of the client, UserData also retrieves all monsters from the DND API.
     */
    private List<GetAllResponseItem> monsters;

    /**
     * Gets characters.
     *
     * @return the characters
     */
    public List<Characters> getCharacters() {
        return characters;
    }

    /**
     * Sets characters.
     *
     * @param characters the characters
     */
    public void setCharacters(List<Characters> characters) {
        this.characters = characters;
    }

    /**
     * Gets encounters.
     *
     * @return the encounters
     */
    public List<Encounter> getEncounters() {
        return encounters;
    }

    /**
     * Sets encounters.
     *
     * @param encounters the encounters
     */
    public void setEncounters(List<Encounter> encounters) {
        this.encounters = encounters;
    }

    /**
     * Gets monsters.
     *
     * @return the monsters
     */
    public List<GetAllResponseItem> getMonsters() {
        return monsters;
    }

    /**
     * Sets monsters.
     *
     * @param monsters the monsters
     */
    public void setMonsters(List<GetAllResponseItem> monsters) {
        this.monsters = monsters;
    }
}
