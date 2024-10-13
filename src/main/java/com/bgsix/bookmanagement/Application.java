package com.bgsix.bookmanagement;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class Application {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		// Default values from .env
		String dbUrl = dotenv.get("DB_URL");
		String dbUsername = dotenv.get("DB_USERNAME");
		String dbPassword = dotenv.get("DB_PASSWORD");
		String serverUrl = dotenv.get("SERVER_URL");

		// Check for command-line arguments and override the defaults if necessary
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
