package ru.baskaeva.cloudstorage.repositories;

import org.hibernate.Criteria;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Repository;
import ru.baskaeva.cloudstorage.models.User;
import ru.baskaeva.cloudstorage.models.UserFile;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hibernate.cfg.AvailableSettings.PERSISTENCE_UNIT_NAME;

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

    @Transactional
    public Optional<UserFile> fileGet(String hash){
        UserFile file = entityManager.find(UserFile.class, hash);
        return file == null ? Optional.empty() : Optional.of(file);
    }

    @Transactional
    public void fileDelete(String fileName) {
        Query query = entityManager.createQuery("delete from UserFile u where u.fileName = :fileName");
        query.setParameter("fileName", fileName);
        query.executeUpdate();
    }

    @Transactional
    public void filePut(String fileName, String name) {
        Query query = entityManager.createQuery("select u from UserFile u where u.fileName = :fileName");
        query.setParameter("fileName", fileName);
        UserFile file = (UserFile) query.getSingleResult();

        file.setFileName(name);
        entityManager.persist(file);
    }

    @Transactional
    public List fileList(Integer limit) {
        Query query = entityManager.createQuery("select u from UserFile u ");
        query.setMaxResults(limit);

        return  query.getResultList();
    }
}
