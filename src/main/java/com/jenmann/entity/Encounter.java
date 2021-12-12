package com.jenmann.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

/**
 * The Encounter class represents a singular planned encounter in a Dungeons and Dragons game. An encounter is usually
 * a combat encounter between the players' characters (Characters class) and various monsters (Monster class). Originally
 * in the MVP for this project, an encounter would also track characters and monsters. However, due to complexity
 * with the frontend and issues building the Many to Many relationships, I didn't include this feature. All other
 * MVP criteria for encounters are met, though!
 *
 * @author jcmann
 */
@Entity(name = "Encounter")
@Table(name = "encounters")
public class Encounter {

    /**
     * The auto-incrementing primary key for the encounters table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    /**
     * For ease of access, each encounter is given a title by its creator.
     */
    @Column(name = "title")
    private String title;

    /**
     * Encounter difficulty can be calculated depending on the number and level of players and monsters involved.
     * Originally, this was supposed to be calculated and stored, but due to the issues with the frontend/M:M part
     * of the MVP, this instead remains a simple user-provided label. The calculated difficulty rating rules in
     * 5th edition are often debated, so this didn't feel bad to leave up to the user to define.
     */
    @Column(name = "difficulty")
    private String difficulty;

    /**
     * The long text description of the encounter.
     */
    @Column(name = "description")
    private String description;

    /**
     * One user can have many encounters, and this instance variable represents that connection. The user info
     * is also not returned in any JSON built from these entities.
     */
    @ManyToOne
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "user_id")
    )
    @JsonIgnore
    private User user;

    /**
     * No arg constructor.
     */
    public Encounter() {
    }

    /**
     * Create an encounter by providing the title and difficulty.
     *
     * @param title      The title of the encounter to assign to the instance variable
     * @param difficulty The difficulty of the encounter to assign to the instance variable
     */
    public Encounter(String title, String difficulty) {
        this.title = title;
        this.difficulty = difficulty;
    }

    /**
     * Create an encounter by providing the ID, title, and difficulty.
     *
     * @param id         the ID to create
     * @param title      The title of the encounter to assign to the instance variable
     * @param difficulty The difficulty of the encounter to assign to the instance variable
     */
    public Encounter(int id, String title, String difficulty) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
    }

    /**
     * Getter method for the id instance variable
     *
     * @return the id instance variable
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
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets difficulty.
     *
     * @return the difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Sets difficulty.
     *
     * @param difficulty the difficulty
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
