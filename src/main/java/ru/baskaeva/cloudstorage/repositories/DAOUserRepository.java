package ru.baskaeva.cloudstorage.repositories;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DAOUserRepository {
    @PersistenceContext
    private EntityManager entityManager;

}
