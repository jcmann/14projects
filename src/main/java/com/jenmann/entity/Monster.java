package com.jenmann.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Monster {

    @JsonProperty("index")
    private String index;

    @JsonProperty("name")
    private String name;

    @JsonProperty("size")
    private String size;

    @JsonProperty("alignment")
    private String alignment;

    @JsonProperty("armor_class")
    private int armorClass;

    @JsonProperty("hit_points")
    private int hitPoints;

    @JsonProperty("hit_dice")
    private String hitDice;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public String getHitDice() {
        return hitDice;
    }

    public void setHitDice(String hitDice) {
        this.hitDice = hitDice;
    }
}

/**
 * Helpful code sources:
 * https://stackoverflow.com/questions/63488348/jackson-json-only-convert-selected-fields-and-methods
 */