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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "characters_encounters", joinColumns = {
            @JoinColumn(name = "encounter_id")},
            inverseJoinColumns = {@JoinColumn(name = "character_id")})
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
}
