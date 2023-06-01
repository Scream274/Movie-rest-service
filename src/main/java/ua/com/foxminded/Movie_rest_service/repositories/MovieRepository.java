package ua.com.foxminded.Movie_rest_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.Movie_rest_service.models.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}