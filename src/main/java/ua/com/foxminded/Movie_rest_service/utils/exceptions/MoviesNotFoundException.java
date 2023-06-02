package ua.com.foxminded.Movie_rest_service.utils.exceptions;

import org.springframework.http.HttpStatus;

public class MoviesNotFoundException extends MovieServiceException {

    public MoviesNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}