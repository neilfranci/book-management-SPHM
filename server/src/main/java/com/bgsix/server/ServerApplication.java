package com.bgsix.server;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ServerApplication {

	private static final Logger Logger = org.slf4j.LoggerFactory.getLogger(ServerApplication.class);

	static Dotenv dotenv = Dotenv.load();
	static String serverUrl = dotenv.get("SERVER_URL");

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		Logger.info("Swagger UI: {}", serverUrl + "/swagger-ui.html");
	}

}
