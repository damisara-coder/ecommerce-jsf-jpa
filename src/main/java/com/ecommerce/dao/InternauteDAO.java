package com.ecommerce.dao;

import com.ecommerce.model.Internaute;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 * DAO pour la gestion des internautes (inscription, authentification, profil).
 */
public class InternauteDAO {

    public Internaute save(Internaute internaute) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (internaute.getId() == null) {
                em.persist(internaute);
            } else {
                internaute = em.merge(internaute);
            }
            tx.commit();
            return internaute;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Internaute findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Internaute> query = em.createQuery(
                "SELECT i FROM Internaute i WHERE i.email = :email", Internaute.class);
            query.setParameter("email", email);
            List<Internaute> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    public Internaute authenticate(String email, String motDePasse) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Internaute> query = em.createQuery(
                "SELECT i FROM Internaute i WHERE i.email = :email AND i.motDePasse = :pwd", Internaute.class);
            query.setParameter("email", email);
            query.setParameter("pwd", motDePasse);
            List<Internaute> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }
}
