package com.jenmann.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

/**
 * The Characters class is really supposed to be the Character class, but Character is also a significant type in Java,
 * so I made this classname plural. Each instance represents an individual Dungeons and Dragons character. One user
 * can have many characters.
 *
 * @author jcmann
 */
@Entity(name = "Characters")
@Table(name = "characters")
public class Characters {

    /**
     * The auto-incrementing primary key for the characters table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    /**
     * The name of the character
     */
    @Column(name = "name")
    private String name;

    /**
     * The level of the character
     */
    @Column(name = "level")
    private int level;

    /**
     * The character's DND race -- not validated against any API, can be custom
     */
    @Column(name = "race")
    private String race;

    /**
     * The character's class. Not validated or restricted to allow multiclassing, etc.
     */
    @Column(name = "character_class")
    private String characterClass;

    /**
     * The character's strength stat. This can be null in the database.
     */
    @Column(name = "strength")
    private Integer strength;

    /**
     * The character's dexterity stat. This can be null in the database.
     */
    @Column(name = "dexterity")
    private Integer dexterity;

    /**
     * The character's constitution stat. This can be null in the database.
     */
    @Column(name = "constitution")
    private Integer constitution;

    /**
     * The character's intelligence stat. This can be null in the database.
     */
    @Column(name = "intelligence")
    private Integer intelligence;

    /**
     * The character's wisdom stat. This can be null in the database.
     */
    @Column(name = "wisdom")
    private Integer wisdom;

    /**
     * The character's charisma stat. This can be null in the database.
     */
    @Column(name = "charisma")
    private Integer charisma;

    /**
     * Each user can have many characters, and this links the two entities together via the user's ID.
     * The character's user info is also explicitly not returned in any JSON created by these entities.
     */
    @ManyToOne
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "user_id")
    )
    @JsonIgnore
    private User user;

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
     */
    public Characters(int id, String name, int level, String race, String characterClass, int strength, int dexterity,
                      int constitution, int intelligence, int wisdom, int charisma, User user) {
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
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets strength.
     *
     * @return the strength
     */
    public Integer getStrength() {
        return strength;
    }

    /**
     * Sets strength.
     *
     * @param strength the strength
     */
    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    /**
     * Gets dexterity.
     *
     * @return the dexterity
     */
    public Integer getDexterity() {
        return dexterity;
    }

    /**
     * Sets dexterity.
     *
     * @param dexterity the dexterity
     */
    public void setDexterity(Integer dexterity) {
        this.dexterity = dexterity;
    }

    /**
     * Gets constitution.
     *
     * @return the constitution
     */
    public Integer getConstitution() {
        return constitution;
    }

    /**
     * Sets constitution.
     *
     * @param constitution the constitution
     */
    public void setConstitution(Integer constitution) {
        this.constitution = constitution;
    }

    /**
     * Gets intelligence.
     *
     * @return the intelligence
     */
    public Integer getIntelligence() {
        return intelligence;
    }

    /**
     * Sets intelligence.
     *
     * @param intelligence the intelligence
     */
    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    /**
     * Gets wisdom.
     *
     * @return the wisdom
     */
    public Integer getWisdom() {
        return wisdom;
    }

    /**
     * Sets wisdom.
     *
     * @param wisdom the wisdom
     */
    public void setWisdom(Integer wisdom) {
        this.wisdom = wisdom;
    }

    /**
     * Gets charisma.
     *
     * @return the charisma
     */
    public Integer getCharisma() {
        return charisma;
    }

    /**
     * Sets charisma.
     *
     * @param charisma the charisma
     */
    public void setCharisma(Integer charisma) {
        this.charisma = charisma;
    }
}