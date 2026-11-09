package com.ecommerce.beans;

import com.ecommerce.dao.InternauteDAO;
import com.ecommerce.model.Internaute;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * Managed Bean JSF pour la gestion de l'Internaute (authentification, inscription, session).
 */
@ManagedBean(name = "internauteBean")
@SessionScoped
public class InternauteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private InternauteDAO internauteDAO = new InternauteDAO();

    // Propriétés pour l'inscription / connexion
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String adresse;

    // Utilisateur actuellement authentifié
    private Internaute internauteConnecte;

    public InternauteBean() {
    }

    /**
     * Méthode de connexion de l'internaute.
     */
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        Internaute user = internauteDAO.authenticate(email, motDePasse);
        if (user != null) {
            this.internauteConnecte = user;
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Bienvenue " + user.getNomComplet(), "Connexion réussie."));
            return "vitrine.xhtml?faces-redirect=true";
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Erreur d'authentification", "Email ou mot de passe incorrect."));
            return null;
        }
    }

    /**
     * Méthode d'inscription d'un nouvel internaute.
     */
    public String inscrire() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (internauteDAO.findByEmail(email) != null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Email déjà existant", "Veuillez choisir un autre email ou vous connecter."));
            return null;
        }
        Internaute nouveau = new Internaute(nom, prenom, email, motDePasse, adresse);
        internauteDAO.save(nouveau);
        this.internauteConnecte = nouveau;
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Compte créé", "Bienvenue parmi nous " + nouveau.getNomComplet()));
        return "vitrine.xhtml?faces-redirect=true";
    }

    /**
     * Déconnexion de la session.
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        this.internauteConnecte = null;
        return "vitrine.xhtml?faces-redirect=true";
    }

    public boolean isEstConnecte() {
        return internauteConnecte != null;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Internaute getInternauteConnecte() {
        return internauteConnecte;
    }

    public void setInternauteConnecte(Internaute internauteConnecte) {
        this.internauteConnecte = internauteConnecte;
    }
}
