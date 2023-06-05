package ua.com.foxminded.Movie_rest_service.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.Movie_rest_service.DTO.MovieDTO;
import ua.com.foxminded.Movie_rest_service.models.Movie;
import ua.com.foxminded.Movie_rest_service.services.MovieService;
import ua.com.foxminded.Movie_rest_service.utils.DTOconverters.MovieDTOConverter;
import ua.com.foxminded.Movie_rest_service.utils.exceptions.MoviesNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ua.com.foxminded.Movie_rest_service.utils.DTOconverters.MovieDTOConverter.convertToDTO;

@CrossOrigin
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
        return convertToDTO(movieService.findById(Long.valueOf(id)));
    }
}