package fr.epsi.petstore.app;

import fr.epsi.petstore.entity.*;
import fr.epsi.petstore.enums.FishLivEnv;
import fr.epsi.petstore.enums.ProdType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe principale de l'application PetStore.
 * Insère des données de test et effectue des requêtes JPQL.
 */
public class PetStoreApp {

    public static void main(String[] args) {

        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("petstore");
            EntityManager em = emf.createEntityManager()) {

            // =============================================
            // INSERTION DES DONNÉES
            // =============================================
            em.getTransaction().begin();

            // --- Création des adresses (3 enregistrements) ---
            Address addr1 = new Address("12", "Rue des Lilas", "44000", "Nantes");
            Address addr2 = new Address("5", "Avenue Jean Jaurès", "75013", "Paris");
            Address addr3 = new Address("28", "Boulevard de la Liberté", "69003", "Lyon");

            // --- Création des animaleries (3 enregistrements) ---
            PetStore store1 = new PetStore("Animalia", "Jean Dupont");
            store1.setAddress(addr1);

            PetStore store2 = new PetStore("PetWorld", "Marie Martin");
            store2.setAddress(addr2);

            PetStore store3 = new PetStore("Au Bonheur des Bêtes", "Pierre Durand");
            store3.setAddress(addr3);

            // --- Création des produits (4 enregistrements) ---
            Product p1 = new Product("FOOD-001", "Croquettes chat premium", ProdType.FOOD, 29.99);
            Product p2 = new Product("FOOD-002", "Flocons pour poisson", ProdType.FOOD, 8.50);
            Product p3 = new Product("ACC-001", "Arbre à chat", ProdType.ACCESSORY, 59.99);
            Product p4 = new Product("CLEAN-001", "Shampoing pour chat", ProdType.CLEANING, 12.90);

            // Association ManyToMany : produits <-> animaleries
            store1.addProduct(p1);
            store1.addProduct(p2);
            store1.addProduct(p3);

            store2.addProduct(p1);
            store2.addProduct(p4);

            store3.addProduct(p2);
            store3.addProduct(p3);
            store3.addProduct(p4);

            // --- Création des poissons (3 enregistrements dans fish + animal) ---
            Fish fish1 = new Fish(LocalDate.of(2024, 3, 15), "Orange", FishLivEnv.FRESH_WATER);
            Fish fish2 = new Fish(LocalDate.of(2024, 6, 20), "Bleu", FishLivEnv.SEA_WATER);
            Fish fish3 = new Fish(LocalDate.of(2025, 1, 10), "Rouge", FishLivEnv.FRESH_WATER);

            // --- Création des chats (3 enregistrements dans cat + animal) ---
            Cat cat1 = new Cat(LocalDate.of(2023, 9, 1), "Noir", "CHIP-001");
            Cat cat2 = new Cat(LocalDate.of(2024, 2, 14), "Blanc", "CHIP-002");
            Cat cat3 = new Cat(LocalDate.of(2024, 12, 25), "Roux", "CHIP-003");

            // Association OneToMany : animaux -> animaleries
            store1.addAnimal(fish1);
            store1.addAnimal(cat1);
            store1.addAnimal(cat2);

            store2.addAnimal(fish2);
            store2.addAnimal(fish3);

            store3.addAnimal(cat3);

            // Persistance : le cascade propage aux adresses, animaux et produits
            em.persist(store1);
            em.persist(store2);
            em.persist(store3);

            em.getTransaction().commit();

            System.out.println("=== Données insérées avec succès ===\n");

            // =============================================
            // REQUÊTE JPQL : tous les animaux d'une animalerie
            // =============================================
            String storeName = "Animalia";
            TypedQuery<Animal> query = em.createQuery(
                    "SELECT a FROM Animal a JOIN a.petStore ps WHERE ps.name = :storeName",
                    Animal.class
            );
            query.setParameter("storeName", storeName);
            List<Animal> animals = query.getResultList();

            System.out.println("=== Animaux de l'animalerie '" + storeName + "' ===");
            for (Animal animal : animals) {
                if (animal instanceof Fish fish) {
                    System.out.println("  Poisson : " + fish);
                } else if (animal instanceof Cat cat) {
                    System.out.println("  Chat    : " + cat);
                }
            }
            System.out.println("Nombre total d'animaux : " + animals.size());
        }
    }
}
