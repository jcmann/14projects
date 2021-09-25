package com.jenmann.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * The type Character.
 */
@Entity(name = "Character")
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private int level;

    @Column(name = "race")
    private String race;

    @Column(name = "character_class")
    private String characterClass;

    /**
     * Instantiates a new Character.
     */
    public Character() {
    }

    /**
     * Instantiates a new Character.
     *
     * @param id             the id
     * @param name           the name
     * @param level          the level
     * @param race           the race
     * @param characterClass the character class
     */
    public Character(int id, String name, int level, String race, String characterClass) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.race = race;
        this.characterClass = characterClass;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets level.
     *
     * @param level the level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Gets race.
     *
     * @return the race
     */
    public String getRace() {
        return race;
    }

    /**
     * Sets race.
     *
     * @param race the race
     */
    public void setRace(String race) {
        this.race = race;
    }

    /**
     * Gets character class.
     *
     * @return the character class
     */
    public String getCharacterClass() {
        return characterClass;
    }

    /**
     * Sets character class.
     *
     * @param characterClass the character class
     */
    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }
}