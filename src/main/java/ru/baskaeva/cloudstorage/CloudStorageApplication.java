package ru.baskaeva.cloudstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.baskaeva.cloudstorage.models.User;
import ru.baskaeva.cloudstorage.repositories.UserRepository;

import java.util.ArrayList;

@SpringBootApplication
public class CloudStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudStorageApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(UserRepository repository) {
        return args -> {
            User userMari = new User(null,"Mari", new BCryptPasswordEncoder().encode("1234"));
            User userYaroslav = new User(null,"Yaroslav", new BCryptPasswordEncoder().encode("1234"));

            //repository.deleteAll();
            repository.save(userMari);
            repository.save(userYaroslav);
        };
    }
}
