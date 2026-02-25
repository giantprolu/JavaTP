package fr.epsi.petstore.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entité centrale représentant une animalerie.
 * Relations :
 *  - OneToOne avec Address (cascade ALL)
 *  - OneToMany avec Animal (cascade ALL, orphanRemoval)
 *  - ManyToMany avec Product (côté propriétaire, cascade PERSIST/MERGE)
 */
@Entity
@Table(name = "pet_store")
public class PetStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "manager_name")
    private String managerName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "petStore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animals = new ArrayList<>();
    
    // On utilise uniquement PERSIST et MERGE (et non ALL) pour éviter qu'un DELETE
    // sur un PetStore ne supprime en cascade les Product, qui peuvent être partagés
    // par plusieurs PetStore. PERSIST propage l'insertion et MERGE propage la mise à jour.
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "pet_store_product",
            joinColumns = @JoinColumn(name = "pet_store_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    public PetStore() {
    }

    public PetStore(String name, String managerName) {
        this.name = name;
        this.managerName = managerName;
    }

    // --- Méthodes utilitaires bidirectionnelles ---

    /** Associe une adresse à cette animalerie (des deux côtés). */
    public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            address.setPetStore(this);
        }
    }

    /** Ajoute un animal à cette animalerie (des deux côtés). */
    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.setPetStore(this);
    }

    /** Retire un animal de cette animalerie (des deux côtés). */
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
        animal.setPetStore(null);
    }

    /** Ajoute un produit à cette animalerie (des deux côtés). */
    public void addProduct(Product product) {
        products.add(product);
        product.getPetStores().add(this);
    }

    /** Retire un produit de cette animalerie (des deux côtés). */
    public void removeProduct(Product product) {
        products.remove(product);
        product.getPetStores().remove(this);
    }

    // --- Getters / Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Address getAddress() {
        return address;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "PetStore{name='" + name + "', manager='" + managerName + "'}";
    }
}
