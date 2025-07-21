package com.micro.config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

// @EnableConfigServer enables this Spring Boot application to act as a Config Server.
// It allows the app to read configuration properties from a remote Git repository (or file system),
// and serve them to other microservices in the system.
// Without this annotation, Spring Cloud Config Server won't be activated.
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
