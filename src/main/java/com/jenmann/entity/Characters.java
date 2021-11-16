package com.jenmann.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Character.
 */
@Entity(name = "Characters")
@Table(name = "characters")
public class Characters {

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

    @Column(name = "strength")
    private int strength;

    @Column(name = "dexterity")
    private int dexterity;

    @Column(name = "constitution")
    private int constitution;

    @Column(name = "intelligence")
    private int intelligence;

    @Column(name = "wisdom")
    private int wisdom;

    @Column(name = "charisma")
    private int charisma;

    @ManyToOne
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "user_id")
    )
    private User user;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "characters_encounters", joinColumns = {
            @JoinColumn(name = "character_id")},
        inverseJoinColumns = {@JoinColumn(name = "encounter_id")})
    @JsonIgnore
    private Set<Encounter> characterEncounters = new HashSet<Encounter>(0);

    /**
     * Instantiates a new Character.
     */
    public Characters() {
    }

    /**
     * Create a user without providing an ID
     *
     * @param name           the name
     * @param level          the level
     * @param race           the race
     * @param characterClass the character class
     */
    public Characters(String name, int level, String race, String characterClass) {
        this.name = name;
        this.level = level;
        this.race = race;
        this.characterClass = characterClass;
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
    public Characters(int id, String name, int level, String race, String characterClass) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.race = race;
        this.characterClass = characterClass;
    }

    /**
     * Instantiates a new Character.
     *
     * @param name           the name
     * @param level          the level
     * @param race           the race
     * @param characterClass the character class
     * @param user           the user this character belongs to
     */
    public Characters(String name, int level, String race, String characterClass, User user) {
        this.name = name;
        this.level = level;
        this.race = race;
        this.characterClass = characterClass;
        this.user = user;
    }


    /**
     * Instantiates a new Characters.
     *
     * @param id                  the id
     * @param name                the name
     * @param level               the level
     * @param race                the race
     * @param characterClass      the character class
     * @param strength            the strength
     * @param dexterity           the dexterity
     * @param constitution        the constitution
     * @param intelligence        the intelligence
     * @param wisdom              the wisdom
     * @param charisma            the charisma
     * @param user                the user
     * @param characterEncounters the character encounters
     */
    public Characters(int id, String name, int level, String race, String characterClass, int strength, int dexterity,
                      int constitution, int intelligence, int wisdom, int charisma, User user, Set<Encounter> characterEncounters) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.race = race;
        this.characterClass = characterClass;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.user = user;
        this.characterEncounters = characterEncounters;
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

    /**
     * Gets the user to whom these characters belong
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the user to whom these characters belong
     *
     * @param user the user
     * @return the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the set of encounters to which this character has been added.
     *
     * @return the set of encounters, the instance variable
     */
    public Set<Encounter> getCharacterEncounters() {
        return characterEncounters;
    }

    /**
     * Sets the set of encounters to which this character has been added
     *
     * @param characterEncounters the new set to assign to the instance variable
     */
    public void setCharacterEncounters(Set<Encounter> characterEncounters) {
        this.characterEncounters = characterEncounters;
    }

    /**
     * Gets strength.
     *
     * @return the strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Sets strength.
     *
     * @param strength the strength
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * Gets dexterity.
     *
     * @return the dexterity
     */
    public int getDexterity() {
        return dexterity;
    }

    /**
     * Sets dexterity.
     *
     * @param dexterity the dexterity
     */
    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    /**
     * Gets constitution.
     *
     * @return the constitution
     */
    public int getConstitution() {
        return constitution;
    }

    /**
     * Sets constitution.
     *
     * @param constitution the constitution
     */
    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    /**
     * Gets intelligence.
     *
     * @return the intelligence
     */
    public int getIntelligence() {
        return intelligence;
    }

    /**
     * Sets intelligence.
     *
     * @param intelligence the intelligence
     */
    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    /**
     * Gets wisdom.
     *
     * @return the wisdom
     */
    public int getWisdom() {
        return wisdom;
    }

    /**
     * Sets wisdom.
     *
     * @param wisdom the wisdom
     */
    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    /**
     * Gets charisma.
     *
     * @return the charisma
     */
    public int getCharisma() {
        return charisma;
    }

    /**
     * Sets charisma.
     *
     * @param charisma the charisma
     */
    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }
}