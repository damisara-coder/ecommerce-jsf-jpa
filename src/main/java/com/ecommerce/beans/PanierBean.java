package com.ecommerce.beans;

import com.ecommerce.dao.CommandeDAO;
import com.ecommerce.model.Commande;
import com.ecommerce.model.Internaute;
import com.ecommerce.model.LignePanier;
import com.ecommerce.model.Produit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * Managed Bean JSF pour la gestion du panier et de la passation de commande.
 */
@ManagedBean(name = "panierBean")
@SessionScoped
public class PanierBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<LignePanier> lignes = new ArrayList<>();
    private CommandeDAO commandeDAO = new CommandeDAO();
    private Commande derniereCommande;

    @ManagedProperty(value = "#{internauteBean}")
    private InternauteBean internauteBean;

    public PanierBean() {
    }

    /**
     * Ajoute un produit dans le panier. Si déjà présent, incrémente la quantité.
     */
    public String ajouterAuPanier(Produit produit) {
        if (produit == null) {
            return null;
        }

        boolean existe = false;
        for (LignePanier ligne : lignes) {
            if (ligne.getProduit().getId().equals(produit.getId())) {
                ligne.setQuantite(ligne.getQuantite() + 1);
                existe = true;
                break;
            }
        }

        if (!existe) {
            lignes.add(new LignePanier(produit, 1));
        }

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Produit ajouté !", produit.getDesignation() + " a été ajouté à votre panier."));

        return null;
    }

    /**
     * Supprime une ligne du panier.
     */
    public void supprimerLigne(LignePanier ligne) {
        lignes.remove(ligne);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Article retiré", "L'article a été retiré du panier."));
    }

    /**
     * Vide complètement le panier.
     */
    public void viderPanier() {
        lignes.clear();
    }

    /**
     * Calcule le total général du panier.
     */
    public Double getMontantTotal() {
        double total = 0.0;
        for (LignePanier ligne : lignes) {
            total += ligne.getSousTotal();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Calcule le nombre total d'articles.
     */
    public Integer getNombreArticles() {
        int total = 0;
        for (LignePanier ligne : lignes) {
            total += ligne.getQuantite();
        }
        return total;
    }

    /**
     * Valide la commande courante avec persistance JPA.
     */
    public String validerCommande() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (lignes.isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Panier vide", "Vous ne pouvez pas valider un panier vide."));
            return null;
        }

        // Vérification de l'authentification de l'internaute
        if (internauteBean == null || !internauteBean.isEstConnecte()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Connexion requise", "Veuillez vous connecter pour finaliser votre commande."));
            return "login.xhtml?faces-redirect=true";
        }

        Internaute client = internauteBean.getInternauteConnecte();

        // Création et persistance de la commande
        Commande cmd = new Commande(client);
        cmd.setDateCommande(new Date());
        cmd.setMontantTotal(getMontantTotal());
        cmd.setStatut("CONFIRMEE");

        for (LignePanier lp : lignes) {
            cmd.ajouterLigne(lp);
        }

        derniereCommande = commandeDAO.save(cmd);

        // Vider le panier après validation
        lignes = new ArrayList<>();

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Commande validée", "Votre commande n°" + derniereCommande.getId() + " a été enregistrée avec succès."));

        return "confirmation.xhtml?faces-redirect=true";
    }

    // Getters et Setters
    public List<LignePanier> getLignes() {
        return lignes;
    }

    public void setLignes(List<LignePanier> lignes) {
        this.lignes = lignes;
    }

    public Commande getDerniereCommande() {
        return derniereCommande;
    }

    public void setDerniereCommande(Commande derniereCommande) {
        this.derniereCommande = derniereCommande;
    }

    public InternauteBean getInternauteBean() {
        return internauteBean;
    }

    public void setInternauteBean(InternauteBean internauteBean) {
        this.internauteBean = internauteBean;
    }
}
