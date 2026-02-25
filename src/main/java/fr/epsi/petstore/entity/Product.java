package fr.epsi.petstore.entity;

import fr.epsi.petstore.enums.ProdType;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un produit vendu en animalerie.
 * Relation ManyToMany bidirectionnelle avec PetStore (côté inverse).
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "label")
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProdType type;

    @Column(name = "price")
    private double price;

    @ManyToMany(mappedBy = "products")
    private Set<PetStore> petStores = new HashSet<>();

    public Product() {
    }

    public Product(String code, String label, ProdType type, double price) {
        this.code = code;
        this.label = label;
        this.type = type;
        this.price = price;
    }

    // --- Méthodes utilitaires bidirectionnelles ---

    /** Associe ce produit à une animalerie (des deux côtés). */
    public void addPetStore(PetStore petStore) {
        this.petStores.add(petStore);
        petStore.getProducts().add(this);
    }

    /** Dissocie ce produit d'une animalerie (des deux côtés). */
    public void removePetStore(PetStore petStore) {
        this.petStores.remove(petStore);
        petStore.getProducts().remove(this);
    }

    // --- Getters / Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ProdType getType() {
        return type;
    }

    public void setType(ProdType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Set<PetStore> getPetStores() {
        return petStores;
    }

    public void setPetStores(Set<PetStore> petStores) {
        this.petStores = petStores;
    }

    @Override
    public String toString() {
        return "Product{code='" + code + "', label='" + label + "', type=" + type + ", price=" + price + "}";
    }
}
