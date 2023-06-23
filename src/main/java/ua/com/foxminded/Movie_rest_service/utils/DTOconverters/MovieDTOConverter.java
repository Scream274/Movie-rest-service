package ua.com.foxminded.Movie_rest_service.utils.DTOconverters;

import org.springframework.stereotype.Service;
import ua.com.foxminded.Movie_rest_service.DTO.MovieDTO;
import ua.com.foxminded.Movie_rest_service.models.Movie;
import ua.com.foxminded.Movie_rest_service.models.Rating;
import ua.com.foxminded.Movie_rest_service.services.RatingService;

@Service
public class MovieDTOConverter {

    private static RatingService ratingService;

    public MovieDTOConverter(RatingService ratingService) {
        MovieDTOConverter.ratingService = ratingService;
    }

    public static MovieDTO convertToDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setOriginalTitle(movieDTO.getOriginalTitle());
        movieDTO.setPrimaryTitle(movie.getPrimaryTitle());
        movieDTO.setGenres(movie.getGenres());
        movieDTO.setIsAdult(movie.getIsAdult());
        movieDTO.setType(movie.getType());
        movieDTO.setStartYear(movie.getStartYear());
        movieDTO.setEndYear(movie.getEndYear());
        movieDTO.setRuntimeMinutes(movie.getRuntimeMinutes());

        if (movie.getRating() != null) {
            movieDTO.setRate(movie.getRating().getRate());
        }

        return movieDTO;
    }

    public static Movie convertFromDTO(MovieDTO movieDTO) {

        Movie movie = new Movie();
        movie.setOriginalTitle(movieDTO.getOriginalTitle());
        movie.setPrimaryTitle(movieDTO.getPrimaryTitle());
        movie.setType(movieDTO.getType());
        movie.setGenres(movieDTO.getGenres());
        movie.setIsAdult(movieDTO.getIsAdult());
        movie.setRuntimeMinutes(movieDTO.getRuntimeMinutes());
        movie.setStartYear(movieDTO.getStartYear());
        movie.setEndYear(movieDTO.getEndYear());

        Rating rating = new Rating();
        rating.setVotes(0);
        rating.setRate(0D);
        ratingService.add(rating);

        movie.setRating(rating);

        return movie;
    }

    public static void setRatingService(RatingService ratingService){
        MovieDTOConverter.ratingService = ratingService;
    }
}