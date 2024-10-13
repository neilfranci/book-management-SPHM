package com.bgsix.bookmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

	@Value("${DB_URL}")
	private String dbUrl;

	@Value("${DB_USERNAME}")
	private String dbUsername;

	@Value("${DB_PASSWORD}")
	private String dbPassword;

	@Bean
	DataSource dataSource() {
		// Log the loaded environment variables
		System.out.println("DB_URL: " + dbUrl);
		System.out.println("DB_USERNAME: " + dbUsername);
		System.out.println("DB_PASSWORD: " + dbPassword);

		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.url(dbUrl);
		dataSourceBuilder.username(dbUsername);
		dataSourceBuilder.password(dbPassword);
		dataSourceBuilder.driverClassName("org.postgresql.Driver");

		return dataSourceBuilder.build();
	}
}
