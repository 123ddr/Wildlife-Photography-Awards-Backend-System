package com.wildlifebackend.wildlife.seeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Autowired
    private UserRoleSeeder userRoleSeeder;

    @Override
    public void run(String... args) throws Exception {
        try {
            initDatabase();
        } catch (Exception e) {
            logger.error("Failed to initialize database with default configuration records", e);
        }
    }

    public void initDatabase() {
        userRoleSeeder.seed();
    }
}
