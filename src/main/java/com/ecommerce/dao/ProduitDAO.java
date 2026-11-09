package com.ecommerce.dao;

import com.ecommerce.model.Categorie;
import com.ecommerce.model.Produit;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 * DAO pour la gestion des produits et des catégories dans la vitrine.
 */
public class ProduitDAO {

    public List<Produit> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Produit> query = em.createQuery("SELECT p FROM Produit p", Produit.class);
            List<Produit> list = query.getResultList();
            return list != null ? list : new ArrayList<Produit>();
        } catch (Exception e) {
            System.err.println("Erreur ProduitDAO.findAll() : " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Produit findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Produit.class, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Produit> findByCategorie(Long categorieId) {
        if (categorieId == null || categorieId == 0L) {
            return findAll();
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Produit> query = em.createQuery(
                "SELECT p FROM Produit p WHERE p.categorie.id = :catId", Produit.class);
            query.setParameter("catId", categorieId);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Erreur findByCategorie : " + e.getMessage());
            return findAll();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Categorie> findAllCategories() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Categorie> query = em.createQuery("SELECT c FROM Categorie c", Categorie.class);
            List<Categorie> list = query.getResultList();
            return list != null ? list : new ArrayList<Categorie>();
        } catch (Exception e) {
            System.err.println("Erreur findAllCategories : " + e.getMessage());
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void save(Produit produit) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (produit.getId() == null) {
                em.persist(produit);
            } else {
                em.merge(produit);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Initialise la base avec quelques données de démonstration si elle est vide.
     */
    public synchronized void initDatabaseIfEmpty() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Long count = 0L;
            try {
                count = em.createQuery("SELECT COUNT(p) FROM Produit p", Long.class).getSingleResult();
            } catch (Exception ex) {
                count = 0L;
            }

            if (count == null || count == 0) {
                tx.begin();

                // Récupérer ou créer les catégories
                List<Categorie> cats = em.createQuery("SELECT c FROM Categorie c", Categorie.class).getResultList();
                Categorie catInfo = null;
                Categorie catTele = null;
                Categorie catAudio = null;

                for (Categorie c : cats) {
                    if ("Informatique".equalsIgnoreCase(c.getNom())) catInfo = c;
                    else if ("Téléphonie".equalsIgnoreCase(c.getNom())) catTele = c;
                    else if ("Audio & Vidéo".equalsIgnoreCase(c.getNom()) || "Audio".equalsIgnoreCase(c.getNom())) catAudio = c;
                }

                if (catInfo == null) {
                    catInfo = new Categorie("Informatique", "Ordinateurs, composants et périphériques");
                    em.persist(catInfo);
                }
                if (catTele == null) {
                    catTele = new Categorie("Téléphonie", "Smartphones et accessoires mobiles");
                    em.persist(catTele);
                }
                if (catAudio == null) {
                    catAudio = new Categorie("Audio & Vidéo", "Casques, écouteurs et enceintes");
                    em.persist(catAudio);
                }

                em.persist(new Produit("PC Portable Gamer Lenovo Legion", "Intel Core i7, 16Go RAM, RTX 4060, SSD 512Go", 12500.0, 15, "laptop.jpg", catInfo));
                em.persist(new Produit("MacBook Air M2", "Apple M2, 8Go RAM, 256Go SSD, Écran 13.6 pouces Retina", 13900.0, 10, "macbook.jpg", catInfo));
                em.persist(new Produit("iPhone 15 Pro", "Écran Super Retina XDR 6.1 pouces, Puce A17 Pro, 128Go", 11800.0, 20, "iphone.jpg", catTele));
                em.persist(new Produit("Samsung Galaxy S24", "Écran Dynamic AMOLED 6.2 pouces, 8Go RAM, 256Go", 8900.0, 25, "galaxy.jpg", catTele));
                em.persist(new Produit("Casque Sony WH-1000XM5", "Casque Bluetooth à réduction de bruit active premium", 3600.0, 30, "sony.jpg", catAudio));
                em.persist(new Produit("AirPods Pro 2", "Écouteurs intra-auriculaires sans fil avec boîtier MagSafe", 2800.0, 40, "airpods.jpg", catAudio));

                tx.commit();
                System.out.println("Base de données e-commerce initialisée avec succès !");
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
