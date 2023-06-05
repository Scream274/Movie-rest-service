package ua.com.foxminded.Movie_rest_service.utils.DTOconverters;

import org.springframework.stereotype.Service;
import ua.com.foxminded.Movie_rest_service.DTO.MovieDTO;
import ua.com.foxminded.Movie_rest_service.models.Movie;

@Service
public class MovieDTOConverter {

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
        movieDTO.setRate(movie.getRating().getRate());

        return movieDTO;
    }

    public static Movie convertFromDTO(MovieDTO movieDTO) {

        //TODO
        Movie movie = new Movie();
        return movie;
    }
}