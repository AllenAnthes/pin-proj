package edu.ucmo.fightingmongeese.pinapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableJpaAuditing
@EnableResourceServer
public class PinApp {
	public static void main(String[] args) {
		SpringApplication.run(PinApp.class, args);
	}

}
