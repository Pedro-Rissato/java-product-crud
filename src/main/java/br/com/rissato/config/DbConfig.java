package br.com.rissato.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public record DbConfig(String url, String user, String password) {

    public static DbConfig load() {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (isFilled(url) && isFilled(user) && isFilled(password)) {
            return new DbConfig(url, user, password);
        }

        Properties props = new Properties();

        System.out.println(System.getProperty("user.dir"));
        System.out.println(DbConfig.class.getClassLoader().getResource("config.properties"));
        try (InputStream input = DbConfig.class.getClassLoader()
        
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "Database configuration not found. Set DB_URL, DB_USER and DB_PASSWORD or provide config.properties in classpath."
                );
            }

            props.load(input);

            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");

            if (!isFilled(url) || !isFilled(user) || !isFilled(password)) {
                throw new IllegalStateException(
                        "config.properties must contain db.url, db.user and db.password."
                );
            }

            return new DbConfig(url, user, password);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties.", e);
        }
    }

    private static boolean isFilled(String value) {
        return value != null && !value.isBlank();
    }
}