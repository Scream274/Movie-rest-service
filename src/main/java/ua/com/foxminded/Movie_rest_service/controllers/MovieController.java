package ua.com.foxminded.Movie_rest_service.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.Movie_rest_service.DTO.MovieDTO;
import ua.com.foxminded.Movie_rest_service.models.Movie;
import ua.com.foxminded.Movie_rest_service.services.MovieService;
import ua.com.foxminded.Movie_rest_service.utils.DTOconverters.MovieDTOConverter;
import ua.com.foxminded.Movie_rest_service.utils.exceptions.MovieDataException;
import ua.com.foxminded.Movie_rest_service.utils.exceptions.MoviesNotFoundException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ua.com.foxminded.Movie_rest_service.utils.DTOconverters.MovieDTOConverter.convertFromDTO;
import static ua.com.foxminded.Movie_rest_service.utils.DTOconverters.MovieDTOConverter.convertToDTO;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;
    private final int PAGE_SIZE = 5;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDTO> getAllMovies(@RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        List<Movie> movies = movieService.findAll(pageable);

        if (movies.isEmpty()) {
            throw new MoviesNotFoundException("Movies was not found!");
        }

        return movies.stream().map(MovieDTOConverter::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MovieDTO getMovieByNumber(@PathVariable("id") String id) {
        return convertToDTO(movieService.findById(Long.valueOf(id))
                .orElseThrow(() -> new MoviesNotFoundException("Movie with id " + id + " was not found!")));
    }

    @PostMapping
    public ResponseEntity<?> addNewMovie(@RequestBody MovieDTO movieDTO) {
        Movie movie = convertFromDTO(movieDTO);

        try {
            movieService.add(movie);
        } catch (DataIntegrityViolationException e) {
            throw new MovieDataException("Error! Movie was not saved to DB!");
        }

        return ResponseEntity.created(URI.create("/movies/" + movie.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable("id") String id, @RequestBody MovieDTO movieDTO) {
        Movie oldMovie = movieService.findById(Long.valueOf(id))
                .orElseThrow(() -> new MoviesNotFoundException("Movie with id " + id + " was not found!"));
        Movie newMovie = convertFromDTO(movieDTO);

        movieService.update(oldMovie, newMovie);
        return ResponseEntity.ok(oldMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable("id") String id) {
        if (!movieService.isExists(Long.valueOf(id))) {
            throw new MoviesNotFoundException("Movie with id " + id + " was not found!");
        }
        
        movieService.deleteById(Long.valueOf(id));

        if (movieService.isExists(Long.valueOf(id))) {
            return ResponseEntity.badRequest().body("Movie with id " + id + " was not deleted!");
        }

        return ResponseEntity.noContent().build();
    }
}