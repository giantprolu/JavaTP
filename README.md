# PetStore — TP Évaluation JPA / Hibernate

Projet pédagogique réalisé dans le cadre du cursus **SN3 — EPSI Nantes**.
Il illustre la mise en œuvre de JPA / Hibernate sur une base MariaDB à travers la modélisation d'une chaîne d'animaleries.

---

## Sommaire

- [Contexte](#contexte)
- [Stack technique](#stack-technique)
- [Modèle de données](#modèle-de-données)
- [Structure du projet](#structure-du-projet)
- [Prérequis](#prérequis)
- [Installation & lancement](#installation--lancement)
- [Fonctionnalités démontrées](#fonctionnalités-démontrées)

---

## Contexte

L'application modélise le domaine d'une **animalerie** (`PetStore`) capable de :

- gérer sa **localisation** (adresse postale),
- héberger des **animaux** de différentes espèces (chats, poissons…),
- référencer un catalogue de **produits** partagés entre plusieurs magasins.

---

## Stack technique

| Outil | Version | Rôle |
|---|---|---|
| Java | 21 | Langage |
| Maven | 3.x | Build & dépendances |
| Hibernate ORM | 7.2.3.Final | Implémentation JPA |
| MariaDB | 10.x+ | Base de données relationnelle |
| MariaDB JDBC | 3.5.7 | Connecteur JDBC |
| JUnit | 4.11 | Tests unitaires |

---

## Modèle de données

### Entités

```
PetStore ──1────1── Address
    │
    ├──1────N── Animal (JOINED inheritance)
    │               ├── Cat      (chipId)
    │               └── Fish     (livingEnv: FRESH_WATER | SEA_WATER)
    │
    └──N────N── Product (type: FOOD | ACCESSORY | CLEANING)
```

### Stratégies JPA notables

| Aspect | Choix | Justification |
|---|---|---|
| Héritage animaux | `JOINED` | Table dédiée par sous-classe, pas de colonnes nulles |
| Cascade Address / Animal | `ALL` + `orphanRemoval` | Cycle de vie lié à celui du magasin |
| Cascade Product | `PERSIST, MERGE` | Produit partagé entre magasins, non supprimé en cascade |
| Relations | Bidirectionnelles | Cohérence maintenue via helpers (`addAnimal`, `addProduct`…) |

### Tables générées

```
address         (id, number, street, zip_code, city)
pet_store       (id, name, manager_name, address_id)
animal          (id, birth, couleur, pet_store_id)
cat             (id ← animal.id, chip_id)
fish            (id ← animal.id, living_env)
product         (id, code, label, type, price)
pet_store_product  (pet_store_id, product_id)   ← table de jointure M:N
```

---

## Structure du projet

```
TPNote/
├── pom.xml
└── src/
    └── main/
        ├── java/fr/epsi/petstore/
        │   ├── app/
        │   │   └── PetStoreApp.java          # Point d'entrée
        │   ├── entity/
        │   │   ├── Address.java
        │   │   ├── Animal.java               # Classe parente (JOINED)
        │   │   ├── Cat.java
        │   │   ├── Fish.java
        │   │   ├── PetStore.java
        │   │   └── Product.java
        │   └── enums/
        │       ├── FishLivEnv.java           # FRESH_WATER, SEA_WATER
        │       └── ProdType.java             # FOOD, ACCESSORY, CLEANING
        └── resources/
            └── META-INF/
                └── persistence.xml           # Configuration JPA
```

---

## Prérequis

- **JDK 21+** installé et `JAVA_HOME` configuré
- **Maven 3.6+** disponible dans le `PATH`
- **MariaDB** en écoute sur `localhost:3307`
- Base de données `petstore` créée :

```sql
CREATE DATABASE petstore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> Les identifiants par défaut sont `root / root` (configurables dans [persistence.xml](src/main/resources/META-INF/persistence.xml)).

---

## Installation & lancement

```bash
# 1. Cloner le dépôt
git clone <url-du-repo>
cd TPNote

# 2. Compiler le projet
mvn compile

# 3. Lancer l'application
mvn exec:java -Dexec.mainClass="fr.epsi.petstore.app.PetStoreApp"
```

> Au démarrage, Hibernate recrée automatiquement le schéma (`drop-and-create`) et insère les données de démonstration.

---

## Fonctionnalités démontrées

- Persistance d'entités en cascade via une seule opération `persist`
- Héritage JPA avec la stratégie `JOINED`
- Relation `@ManyToMany` avec table de jointure implicite
- Requête JPQL avec polymorphisme (`instanceof` / `TREAT`)
- Enums persistés en base (`@Enumerated(EnumType.STRING)`)
- Méthodes helper pour maintenir la cohérence des relations bidirectionnelles

---

*Projet réalisé dans le cadre de l'évaluation JPA — EPSI Nantes, SN3.*
