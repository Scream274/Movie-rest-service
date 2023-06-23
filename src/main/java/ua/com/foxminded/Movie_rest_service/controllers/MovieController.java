package ua.com.foxminded.Movie_rest_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.Movie_rest_service.DTO.MovieDTO;
import ua.com.foxminded.Movie_rest_service.models.Movie;
import ua.com.foxminded.Movie_rest_service.models.Rating;
import ua.com.foxminded.Movie_rest_service.services.MovieService;
import ua.com.foxminded.Movie_rest_service.services.RatingService;
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
    private final RatingService ratingService;
    private final int PAGE_SIZE = 5;

    public MovieController(MovieService movieService, RatingService ratingService) {
        this.movieService = movieService;
        this.ratingService = ratingService;
    }

    @Operation(summary = "Get all movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all movies",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MovieDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Movies not found",
                    content = @Content(mediaType = "application/json"))})
    @GetMapping
    public List<MovieDTO> getAllMovies(@RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        List<Movie> movies = movieService.findAll(pageable);

        if (movies.isEmpty()) {
            throw new MoviesNotFoundException("Movies was not found!");
        }

        return movies.stream().map(MovieDTOConverter::convertToDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Get movie by its Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the movie",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Movies not found",
                    content = @Content(mediaType = "application/json"))})
    @GetMapping("/{id}")
    public MovieDTO getMovieByNumber(@PathVariable("id") String id) {
        return convertToDTO(movieService.findById(Long.valueOf(id))
                .orElseThrow(() -> new MoviesNotFoundException("Movie with id " + id + " was not found!")));
    }

    @Operation(summary = "Add new movie", security = @SecurityRequirement(name = "bearerAuth"))
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

    @Operation(summary = "Update movie by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable("id") String id, @RequestBody MovieDTO movieDTO) {
        Movie oldMovie = movieService.findById(Long.valueOf(id))
                .orElseThrow(() -> new MoviesNotFoundException("Movie with id " + id + " was not found!"));
        Movie newMovie = convertFromDTO(movieDTO);

        movieService.update(oldMovie, newMovie);
        return ResponseEntity.ok(newMovie);
    }

    @Operation(summary = "Delete movie by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete the movie",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Movie was not deleted",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = @Content(mediaType = "application/json"))})
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

    @Operation(summary = "Update movies rating")
    @PutMapping("/{id}/rating")
    public ResponseEntity<?> updateMovieRating(@PathVariable("id") String id, @RequestBody Integer rate) {
        Movie movie = movieService.findById(Long.valueOf(id)).orElseThrow(() -> new MoviesNotFoundException("Movie with id " + id + " was not found!"));

        Rating rating = movie.getRating();

        if (rating == null) {
            rating = new Rating(Long.valueOf(id), Double.valueOf(rate), 1);
            movie.setRating(rating);
            movieService.add(movie);
        } else {
            ratingService.update(rating, rate);
        }

        return ResponseEntity.ok(rating);
    }
}