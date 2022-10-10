package ru.baskaeva.cloudstorage.repositories;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Repository;
import ru.baskaeva.cloudstorage.models.UserFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class DAOUserFileRepository implements CommandLineRunner {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void run(String... args) {}

    @Transactional
    public void filePost(UserFile file){
        entityManager.persist(file);
    }

    public UserFile fileGet(String url){
        return new UserFile();
    }

    public void fileDelete(String url) {
    }

    public void filePut(String fileName) {
    }
}
