package fr.epsi.petstore.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entité représentant un chat (hérite de Animal).
 * Table dédiée "cat" liée à "animal" via héritage JOINED.
 */
@Entity
@Table(name = "cat")
public class Cat extends Animal {

    @Column(name = "chip_id")
    private String chipId;

    public Cat() {
    }

    public Cat(LocalDate birth, String couleur, String chipId) {
        super(birth, couleur);
        this.chipId = chipId;
    }

    // --- Getters / Setters ---

    public String getChipId() {
        return chipId;
    }

    public void setChipId(String chipId) {
        this.chipId = chipId;
    }

    @Override
    public String toString() {
        return "Cat{id=" + getId() + ", couleur='" + getCouleur() + "', chipId='" + chipId + "'}";
    }
}
