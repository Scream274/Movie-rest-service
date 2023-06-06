package ua.com.foxminded.Movie_rest_service.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.Movie_rest_service.models.Movie;

import java.util.List;
import java.util.Optional;

@Service
public interface MovieService {
    @Transactional
    List<Movie> findAll(Pageable pageable);

    @Transactional
    Optional<Movie> findById(Long id);

    @Transactional
    void deleteById(Long id);

    @Transactional
    void add(Movie movie);

    @Transactional
    void update(Movie oldMovie, Movie newMovie);

    @Transactional
    boolean isExists(Long id);
}