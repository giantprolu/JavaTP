package fr.epsi.petstore.entity;

import fr.epsi.petstore.enums.FishLivEnv;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entité représentant un poisson (hérite de Animal).
 * Table dédiée "fish" liée à "animal" via héritage JOINED.
 */
@Entity
@Table(name = "fish")
public class Fish extends Animal {

    @Enumerated(EnumType.STRING)
    @Column(name = "living_env")
    private FishLivEnv livingEnv;

    public Fish() {
    }

    public Fish(LocalDate birth, String couleur, FishLivEnv livingEnv) {
        super(birth, couleur);
        this.livingEnv = livingEnv;
    }

    // --- Getters / Setters ---

    public FishLivEnv getLivingEnv() {
        return livingEnv;
    }

    public void setLivingEnv(FishLivEnv livingEnv) {
        this.livingEnv = livingEnv;
    }

    @Override
    public String toString() {
        return "Fish{id=" + getId() + ", couleur='" + getCouleur() + "', livingEnv=" + livingEnv + "}";
    }
}
