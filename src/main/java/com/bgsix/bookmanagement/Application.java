package com.bgsix.bookmanagement;

import java.io.File;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class Application {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
        String dbUrl = "jdbc:postgresql://localhost:5432/book-management";
        String dbUsername = null;
        String dbPassword = null;
        String serverUrl = "http://localhost:8080";

        // Check if .env file exists
        File dotenvFile = new File(".env");
        if (dotenvFile.exists()) {
            // Load environment variables from .env
            Dotenv dotenv = Dotenv.load();
            dbUrl = dotenv.get("DB_URL");
            dbUsername = dotenv.get("DB_USERNAME");
            dbPassword = dotenv.get("DB_PASSWORD");
            serverUrl = dotenv.get("SERVER_URL");

            logger.info(".env file found and loaded successfully.");
        } else {
            logger.warn(".env file not found. Attempting to use command-line arguments.");

            // Check for command-line arguments
            for (String arg : args) {
                if (arg.startsWith("--DB_URL=")) {
                    dbUrl = arg.split("=", 2)[1];
                } else if (arg.startsWith("--DB_USERNAME=")) {
                    dbUsername = arg.split("=", 2)[1];
                } else if (arg.startsWith("--DB_PASSWORD=")) {
                    dbPassword = arg.split("=", 2)[1];
                } else if (arg.startsWith("--SERVER_URL=")) {
                    serverUrl = arg.split("=", 2)[1];
                }
            }
        }

        // Check if required variables are still null
        if (dbUrl == null || dbUsername == null || dbPassword == null) {
            logger.error("Missing required configuration! Please provide DB_URL, DB_USERNAME, DB_PASSWORD, and SERVER_URL.");
            logger.error("Either include a .env file with these variables or provide them as command-line arguments.");
            System.exit(1); // Terminate the application with an error status
        }

        // Set system properties to make them accessible in Spring context
        System.setProperty("DB_URL", dbUrl);
        System.setProperty("DB_USERNAME", dbUsername);
        System.setProperty("DB_PASSWORD", dbPassword);
        System.setProperty("SERVER_URL", serverUrl);

        SpringApplication.run(Application.class, args);
        logger.info("Swagger UI: {}", serverUrl + "/swagger-ui/index.html");
        logger.info("DB_URL: {}", dbUrl);
        logger.info("DB_USERNAME: {}", dbUsername);
        logger.info("DB_PASSWORD: {}", dbPassword);
        logger.info("Server URL: {}", serverUrl);
    }
}
