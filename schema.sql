-- ============================================
-- SCRIPT SQL - GESTION DE PHARMACIE
-- Logins et mots de passe simples
-- ============================================

CREATE DATABASE IF NOT EXISTS gestion_pharmacie;
USE gestion_pharmacie;

-- ============================================
-- TABLES
-- ============================================

CREATE TABLE IF NOT EXISTS Pharmacien (
    id_pharmacien INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    login VARCHAR(100) UNIQUE NOT NULL,
    pwd VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Gestionnaire (
    id_gestionnaire INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    login VARCHAR(100) UNIQUE NOT NULL,
    pwd VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Client (
    id_client INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    adresse VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Medicament (
    id_medicament INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(150) NOT NULL,
    dosage VARCHAR(50),
    stock INT NOT NULL DEFAULT 0,
    prix_unitaire DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS Vente (
    id_vente INT PRIMARY KEY AUTO_INCREMENT,
    id_pharmacien INT NOT NULL,
    id_client INT NOT NULL,
    id_medicament INT NOT NULL,
    quantite INT NOT NULL,
    date_vente DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pharmacien) REFERENCES Pharmacien(id_pharmacien),
    FOREIGN KEY (id_client) REFERENCES Client(id_client),
    FOREIGN KEY (id_medicament) REFERENCES Medicament(id_medicament)
);

CREATE TABLE IF NOT EXISTS Commande (
    id_commande INT PRIMARY KEY AUTO_INCREMENT,
    id_gestionnaire INT NOT NULL,
    id_medicament INT NOT NULL,
    quantite INT NOT NULL,
    date_commande DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_gestionnaire) REFERENCES Gestionnaire(id_gestionnaire),
    FOREIGN KEY (id_medicament) REFERENCES Medicament(id_medicament)
);

CREATE TABLE IF NOT EXISTS StockHistorique (
    id_historique INT PRIMARY KEY AUTO_INCREMENT,
    id_medicament INT NOT NULL,
    quantite INT NOT NULL,
    date_historique DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_medicament) REFERENCES Medicament(id_medicament)
);

-- ============================================
-- DONNÉES DE TEST SIMPLES
-- ============================================

-- Pharmacien (login = p1, p2 ; pwd = 123, 456)
INSERT INTO Pharmacien (nom, prenom, login, pwd) VALUES
('Al-Mansouri', 'Ahmed', 'p1', '123'),
('Al-Fassi', 'Fatma', 'p2', '456');

-- Gestionnaire (login = g1, g2 ; pwd = 111, 222)
INSERT INTO Gestionnaire (nom, prenom, login, pwd) VALUES
('Al-Hakim', 'Mohamed', 'g1', '111'),
('Al-Karim', 'Aisha', 'g2', '222');

-- Clients
INSERT INTO Client (nom, prenom, email, adresse) VALUES
('Haddad', 'Youssef', 'youssef@email.com', 'Tunis Centre'),
('Mansouri', 'Salma', 'salma@email.com', 'Sfax Ville'),
('Gharbi', 'Karim', 'karim@email.com', 'Sousse'),
('Ayari', 'Nour', 'nour@email.com', 'Ariana'),
('Jebali', 'Sami', 'sami@email.com', 'Bizerte');

-- Médicaments
INSERT INTO Medicament (nom, dosage, stock, prix_unitaire) VALUES
('Paracetamol', '500mg', 50, 2.50),
('Ibuprofen', '200mg', 30, 3.75),
('Aspirine', '100mg', 8, 1.99),
('Amoxicilline', '250mg', 15, 5.50),
('Metformine', '500mg', 3, 4.25),
('Doliprane', '1000mg', 40, 3.20),
('Ventoline', '2mg', 12, 7.80),
('Augmentin', '625mg', 10, 12.50),
('Spasfon', '80mg', 25, 4.10),
('Efferalgan', '500mg', 35, 2.90),
('Insuline', '10ml', 5, 18.00),
('Smecta', '3g', 20, 3.60),
('Gaviscon', '10ml', 18, 6.40),
('Zyrtec', '10mg', 22, 5.75),
('Panadol', '500mg', 60, 2.80);

-- Ventes
INSERT INTO Vente (id_pharmacien, id_client, id_medicament, quantite) VALUES
(1, 1, 1, 5),
(1, 2, 3, 2),
(2, 3, 5, 1),
(2, 4, 2, 3),
(1, 5, 6, 4);

-- Commandes
INSERT INTO Commande (id_gestionnaire, id_medicament, quantite) VALUES
(1, 5, 20),
(1, 6, 15),
(2, 3, 30),
(2, 12, 25);

-- StockHistorique
INSERT INTO StockHistorique (id_medicament, quantite) VALUES
(1, 50),
(2, 30),
(3, 8),
(4, 15),
(5, 3),
(6, 40),
(7, 12),
(8, 10),
(9, 25),
(10, 35),
(11, 5),
(12, 20),
(13, 18),
(14, 22),
(15, 60);
