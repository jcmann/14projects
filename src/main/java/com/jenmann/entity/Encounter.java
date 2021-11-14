package com.jenmann.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Encounter")
@Table(name = "encounters")
public class Encounter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "user_id")
    )
    private User user;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "characterEncounters")
    private Set<Characters> encounterCharacters = new HashSet<Characters>(0);

    public Encounter() {
    }

    public Encounter(String title, String difficulty, Set<Characters> encounterCharacters) {
        this.title = title;
        this.difficulty = difficulty;
        this.encounterCharacters = encounterCharacters;
    }

    public Encounter(int id, String title, String difficulty, Set<Characters> encounterCharacters) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.encounterCharacters = encounterCharacters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Set<Characters> getEncounterCharacters() {
        return encounterCharacters;
    }

    public void setEncounterCharacters(Set<Characters> encounterCharacters) {
        this.encounterCharacters = encounterCharacters;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
