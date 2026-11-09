package com.ecommerce.dao;

import com.ecommerce.model.Commande;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * DAO pour la persistance des commandes et de leurs lignes associées.
 */
public class CommandeDAO {

    public Commande save(Commande commande) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(commande);
            tx.commit();
            return commande;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
