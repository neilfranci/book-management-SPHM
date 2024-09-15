package com.bgsix.bookmanagement.config;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

	@Bean
	public DataSource dataSource() {

		// String projectPath = System.getProperty("user.dir", ".");

		// // Fix issue with the path with spring boot and graalvm native image
		// if (!projectPath.contains("bookmanagement")) {
		// 	projectPath = projectPath + "\\bookmanagement\\";
		// }
		// System.out.println("Current resource path: " + projectPath);

		// Load environment variables from the .env file
		Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().ignoreIfMissing().load();
		// Dotenv dotenv = Dotenv.configure().directory(projectPath).ignoreIfMalformed().ignoreIfMissing().load();


		// Print the loaded environment variables
		System.out.println("DB_URL: " + dotenv.get("DB_URL"));
		System.out.println("DB_USERNAME: " + dotenv.get("DB_USERNAME"));
		System.out.println("DB_PASSWORD: " + dotenv.get("DB_PASSWORD"));

		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		// Set the DataSource properties using the loaded environment variables
		dataSourceBuilder.url(dotenv.get("DB_URL"));
		dataSourceBuilder.username(dotenv.get("DB_USERNAME"));
		dataSourceBuilder.password(dotenv.get("DB_PASSWORD"));
		dataSourceBuilder.driverClassName("org.postgresql.Driver");

		// Build the DataSource
		return dataSourceBuilder.build();
	}
}
