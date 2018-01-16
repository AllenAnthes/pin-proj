package edu.ucmo.fightingmongeese.pinapp;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PinApp {

	public static void main(String[] args) {
		SpringApplication.run(PinApp.class, args);
	}

//	@Bean
//	public CommandLineRunner loadData(PinRepository repository) {
//		return (args) -> {
//			// save a couple of customers
//			Pin pin = new Pin();
//			repository.save(new Pin("Jack", "Bauer"));
//			repository.save(new Customer("Chloe", "O'Brian"));
//			repository.save(new Customer("Kim", "Bauer"));
//		};
//	}
}
