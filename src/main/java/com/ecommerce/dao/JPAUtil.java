package com.ecommerce.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe utilitaire pour initialiser et fournir l'EntityManagerFactory
 * et l'EntityManager JPA.
 */
public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "ecommercePU";
    private static EntityManagerFactory factory;

    static {
        try {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Throwable ex) {
            System.err.println("Échec de l'initialisation d'EntityManagerFactory : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static EntityManager getEntityManager() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return factory.createEntityManager();
    }

    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
