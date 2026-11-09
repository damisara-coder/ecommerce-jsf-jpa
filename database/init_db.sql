-- ==========================================================
-- Atelier JEE : Application Web JSF & JPA - Site E-Commerce
-- Université Abdelmalek Essaadi - FST Tanger - LSI
-- Pr. ELAACHAK LOTFI
-- Script de création et d'initialisation de la Base de Données
-- ==========================================================

CREATE DATABASE IF NOT EXISTS `ecommerce_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `ecommerce_db`;

-- 1. Table Internaute (Gestion de l'Internaute)
DROP TABLE IF EXISTS `ligne_panier`;
DROP TABLE IF EXISTS `commande`;
DROP TABLE IF EXISTS `produit`;
DROP TABLE IF EXISTS `categorie`;
DROP TABLE IF EXISTS `internaute`;

CREATE TABLE `internaute` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `nom` VARCHAR(50) NOT NULL,
  `prenom` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `motDePasse` VARCHAR(100) NOT NULL,
  `adresse` VARCHAR(255) DEFAULT NULL,
  `date_inscription` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Table Categorie (Gestion de la Vitrine)
CREATE TABLE `categorie` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `nom` VARCHAR(100) NOT NULL UNIQUE,
  `description` VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB;

-- 3. Table Produit (Gestion de la Vitrine)
CREATE TABLE `produit` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `designation` VARCHAR(150) NOT NULL,
  `description` TEXT DEFAULT NULL,
  `prix` DOUBLE NOT NULL,
  `quantite_stock` INT NOT NULL,
  `image_url` VARCHAR(255) DEFAULT NULL,
  `categorie_id` BIGINT NOT NULL,
  CONSTRAINT `fk_produit_categorie` FOREIGN KEY (`categorie_id`) REFERENCES `categorie` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4. Table Commande
CREATE TABLE `commande` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `date_commande` DATETIME NOT NULL,
  `montant_total` DOUBLE NOT NULL,
  `statut` VARCHAR(30) DEFAULT 'VALIDEE',
  `internaute_id` BIGINT NOT NULL,
  CONSTRAINT `fk_commande_internaute` FOREIGN KEY (`internaute_id`) REFERENCES `internaute` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Table Ligne Panier / Ligne Commande (Gestion du Panier & Commande)
CREATE TABLE `ligne_panier` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `produit_id` BIGINT NOT NULL,
  `quantite` INT NOT NULL,
  `prix_unitaire` DOUBLE NOT NULL,
  `commande_id` BIGINT DEFAULT NULL,
  CONSTRAINT `fk_ligne_produit` FOREIGN KEY (`produit_id`) REFERENCES `produit` (`id`),
  CONSTRAINT `fk_ligne_commande` FOREIGN KEY (`commande_id`) REFERENCES `commande` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- Jeu de données de test (Categories, Produits, Internaute)
-- ----------------------------------------------------------
INSERT INTO `categorie` (`id`, `nom`, `description`) VALUES
(1, 'Informatique', 'Ordinateurs portables, périphériques et accessoires'),
(2, 'Téléphonie', 'Smartphones nouvelle génération et tablettes'),
(3, 'Audio & Son', 'Casques, enceintes connectées et écouteurs');

INSERT INTO `produit` (`id`, `designation`, `description`, `prix`, `quantite_stock`, `image_url`, `categorie_id`) VALUES
(1, 'PC Portable Gamer Lenovo Legion', 'Intel Core i7 13700H, 16Go RAM DDR5, RTX 4060, SSD 512Go NVMe', 12500.00, 15, 'laptop.jpg', 1),
(2, 'Apple MacBook Air M2', 'Puce Apple M2, 8Go RAM unifiée, SSD 256Go, Écran Liquid Retina 13.6', 13900.00, 10, 'macbook.jpg', 1),
(3, 'Apple iPhone 15 Pro', 'Écran Super Retina XDR 6.1, Puce A17 Pro, 128Go Titane Naturel', 11800.00, 20, 'iphone.jpg', 2),
(4, 'Samsung Galaxy S24', 'Écran Dynamic AMOLED 2X 6.2, Exynos 2400, 8Go RAM, 256Go', 8900.00, 25, 'galaxy.jpg', 2),
(5, 'Casque Sony WH-1000XM5', 'Casque sans fil arceau à réduction active de bruit, autonomie 30h', 3600.00, 30, 'sony.jpg', 3),
(6, 'Apple AirPods Pro 2', 'Écouteurs sans fil avec réduction active de bruit et audio spatial', 2800.00, 40, 'airpods.jpg', 3);

INSERT INTO `internaute` (`id`, `nom`, `prenom`, `email`, `motDePasse`, `adresse`, `date_inscription`) VALUES
(1, 'El Idrissi', 'Karim', 'karim@gmail.com', '123456', 'Tanger, Val Fleuri', NOW());
