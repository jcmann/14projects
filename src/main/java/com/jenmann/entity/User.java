package com.jenmann.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

import com.jenmann.entity.Characters;

/**
 * The User object
 *
 * @author jcmann
 */
@Entity(name = "User")
@Table(name = "users")
public class User {

    /**
     * The auto-incrementing ID for the user table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    /**
     * The username, also used to log in via AWS
     */
    @Column(name = "username")
    private String username;

    /**
     * One user can have many characters, so the User object is able to maintain a list of its characters if needed.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Characters> charactersList = new ArrayList<>();

    /**
     * One user can have many encounters, so the User object is able to maintain a list of its encounters if needed.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Encounter> encounterList = new ArrayList<>();

    /**
     * Instantiates a new User.
     */
    public User() {
    }

    /**
     * Instantiates a new User.
     *
     * @param username the username
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Instantiates a new User.
     *
     * @param id       the id
     * @param username the username
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
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
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

}
