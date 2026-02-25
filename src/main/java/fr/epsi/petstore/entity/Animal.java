package fr.epsi.petstore.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entité parente représentant un animal.
 * Stratégie d'héritage JOINED : chaque sous-classe (Fish, Cat) a sa propre table.
 * Relation ManyToOne bidirectionnelle avec PetStore.
 */
@Entity
@Table(name = "animal")
@Inheritance(strategy = InheritanceType.JOINED)
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "couleur")
    private String couleur;

    @ManyToOne
    @JoinColumn(name = "pet_store_id")
    private PetStore petStore;

    public Animal() {
    }

    public Animal(LocalDate birth, String couleur) {
        this.birth = birth;
        this.couleur = couleur;
    }

    // --- Getters / Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public PetStore getPetStore() {
        return petStore;
    }

    public void setPetStore(PetStore petStore) {
        this.petStore = petStore;
    }

    @Override
    public String toString() {
        return "Animal{id=" + id + ", couleur='" + couleur + "', birth=" + birth + "}";
    }
}
