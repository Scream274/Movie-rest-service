package ua.com.foxminded.Movie_rest_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.foxminded.Movie_rest_service.models.Movie;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = "SELECT * FROM movies ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Movie> findRandom();

}