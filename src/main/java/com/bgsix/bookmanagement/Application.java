package com.bgsix.bookmanagement;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class Application {

	private static final Logger Logger = org.slf4j.LoggerFactory.getLogger(Application.class);

	static Dotenv dotenv = Dotenv.load();
	static String serverUrl = dotenv.get("SERVER_URL");

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		Logger.info("Swagger UI: {}", serverUrl + "/swagger-ui/index.html");
		Logger.info("Server URL: {}", serverUrl);
	}

}
