package com.jenmann.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Monster class represents a singular monster from the DND 5e API. While the GetAllResponseItem currently only
 * represents a monster, it represents the parsed-down version of the entity returned by the getAll method. In contrast,
 * this entity represents the full monster. Some data provided by the API is purposefully left out of this entity
 * for brevity, and because not all data was needed.
 *
 * @author jcmann
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Monster {

    /**
     * The lower snake case unique identifier for the monster
     */
    @JsonProperty("index")
    private String index;

    /**
     * The monster's name
     */
    @JsonProperty("name")
    private String name;

    /**
     * The size classification for the monster
     */
    @JsonProperty("size")
    private String size;

    /**
     * The character's alignment (ex: "Chaotic Neutral")
     */
    @JsonProperty("alignment")
    private String alignment;

    /**
     * The armor class (or AC) value of the monster
     */
    @JsonProperty("armor_class")
    private int armorClass;

    /**
     * The maximum HP of the monster
     */
    @JsonProperty("hit_points")
    private int hitPoints;

    /**
     * Generic no-arg constructor
     */
    public Monster() {
    }

    /**
     * Constructs a Monster entity with all instance variables. This would be commonly used because the API
     * should, hypothetically, return all this data.
     *
     * @param index lower snake case index
     * @param name monster name
     * @param size 5e-type size category
     * @param alignment chaotic/lawful good/evil
     * @param armorClass integer armor class
     * @param hitPoints maximum health points
     * @param hitDice monster's hit dice
     */
    public Monster(String index, String name, String size, String alignment, int armorClass, int hitPoints, String hitDice) {
        this.index = index;
        this.name = name;
        this.size = size;
        this.alignment = alignment;
        this.armorClass = armorClass;
        this.hitPoints = hitPoints;
        this.hitDice = hitDice;
    }

    /**
     * The hit dice used by the monster in its attacks
     */
    @JsonProperty("hit_dice")
    private String hitDice;

    /**
     * Gets index.
     *
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * Sets index.
     *
     * @param index the index
     */
    public void setIndex(String index) {
        this.index = index;
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
     * Gets size.
     *
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Gets alignment.
     *
     * @return the alignment
     */
    public String getAlignment() {
        return alignment;
    }

    /**
     * Sets alignment.
     *
     * @param alignment the alignment
     */
    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    /**
     * Gets armor class.
     *
     * @return the armor class
     */
    public int getArmorClass() {
        return armorClass;
    }

    /**
     * Sets armor class.
     *
     * @param armorClass the armor class
     */
    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    /**
     * Gets hit points.
     *
     * @return the hit points
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Sets hit points.
     *
     * @param hitPoints the hit points
     */
    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    /**
     * Gets hit dice.
     *
     * @return the hit dice
     */
    public String getHitDice() {
        return hitDice;
    }

    /**
     * Sets hit dice.
     *
     * @param hitDice the hit dice
     */
    public void setHitDice(String hitDice) {
        this.hitDice = hitDice;
    }
}

/**
 * Helpful code sources:
 * https://stackoverflow.com/questions/63488348/jackson-json-only-convert-selected-fields-and-methods
 */