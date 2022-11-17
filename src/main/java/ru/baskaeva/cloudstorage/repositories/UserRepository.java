package ru.baskaeva.cloudstorage.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.baskaeva.cloudstorage.models.User;

import java.util.List;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);
    List<User> findAll();
    boolean existsByLogin(String login);
}
