package ua.com.foxminded.Movie_rest_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MovieRestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieRestServiceApplication.class, args);
	}

}
