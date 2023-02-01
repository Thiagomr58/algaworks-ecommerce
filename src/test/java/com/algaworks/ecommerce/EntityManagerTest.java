package com.algaworks.ecommerce;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Thiago Rodrigues
 * @version 1.0.0
 * @since 31/01/2023
 */
public class EntityManagerTest {

    protected static EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;

    @BeforeClass // Executa antes de tudo
    public static void setUpBeforeClass(){
        entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
    }

    @AfterClass // Executa depois de tudo
    public static void tearDownAfterClass() {
        entityManagerFactory.close();
    }

    @Before // Será executado antes de cada teste
    public void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @After // Será executado ao final de cada teste
    public void tearDown() {
        entityManager.close();
    }

}
