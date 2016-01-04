package io.acme.registration.persistence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Persistence Spring Boot Application Context
 */
@SpringBootApplication
@EnableMongoRepositories
public class PersistenceBootstrap implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PersistenceBootstrap.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {

    }
}
