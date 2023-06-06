package ua.com.foxminded.Movie_rest_service.services.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.Movie_rest_service.models.Movie;
import ua.com.foxminded.Movie_rest_service.repositories.MovieRepository;
import ua.com.foxminded.Movie_rest_service.services.MovieService;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public List<Movie> findAll(Pageable pageable) {
        return movieRepository.findAll(pageable).getContent();
    }

    @Override
    @Transactional
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void add(Movie movie) {
        movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void update(Movie oldMovie, Movie newMovie) {
        oldMovie.setGenres(newMovie.getGenres());
        oldMovie.setStartYear(newMovie.getStartYear());
        oldMovie.setEndYear(newMovie.getEndYear());
        oldMovie.setType(newMovie.getType());
        oldMovie.setIsAdult(newMovie.getIsAdult());
        oldMovie.setRuntimeMinutes(newMovie.getRuntimeMinutes());
        oldMovie.setPrimaryTitle(newMovie.getPrimaryTitle());
        oldMovie.setOriginalTitle(newMovie.getOriginalTitle());

        movieRepository.save(oldMovie);
    }

    @Override
    @Transactional
    public boolean isExists(Long id) {
        return movieRepository.existsById(id);
    }
}