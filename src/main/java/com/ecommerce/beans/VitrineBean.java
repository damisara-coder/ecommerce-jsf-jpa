package com.ecommerce.beans;

import com.ecommerce.dao.ProduitDAO;
import com.ecommerce.model.Categorie;
import com.ecommerce.model.Produit;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Managed Bean JSF pour la gestion de la vitrine (catalogue, catégories, produits).
 */
@ManagedBean(name = "vitrineBean")
@ViewScoped
public class VitrineBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProduitDAO produitDAO = new ProduitDAO();

    private List<Produit> produits;
    private List<Categorie> categories;
    private Long selectedCategorieId = 0L;
    private String motCleRecherche;

    @PostConstruct
    public void init() {
        // Initialise la BDD avec des données test si vide
        produitDAO.initDatabaseIfEmpty();
        chargerDonnees();
    }

    public void chargerDonnees() {
        this.categories = produitDAO.findAllCategories();
        if (selectedCategorieId == null || selectedCategorieId == 0L) {
            this.produits = produitDAO.findAll();
        } else {
            this.produits = produitDAO.findByCategorie(selectedCategorieId);
        }
    }

    public void filtrer() {
        chargerDonnees();
    }

    // Getters et Setters
    public List<Produit> getProduits() {
        if (produits == null || produits.isEmpty()) {
            chargerDonnees();
        }
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    public List<Categorie> getCategories() {
        if (categories == null || categories.isEmpty()) {
            this.categories = produitDAO.findAllCategories();
        }
        return categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }

    public Long getSelectedCategorieId() {
        return selectedCategorieId;
    }

    public void setSelectedCategorieId(Long selectedCategorieId) {
        this.selectedCategorieId = selectedCategorieId;
    }

    public String getMotCleRecherche() {
        return motCleRecherche;
    }

    public void setMotCleRecherche(String motCleRecherche) {
        this.motCleRecherche = motCleRecherche;
    }
}
