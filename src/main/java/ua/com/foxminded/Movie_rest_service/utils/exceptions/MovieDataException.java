package ua.com.foxminded.Movie_rest_service.utils.exceptions;

import org.springframework.http.HttpStatus;

public class MovieDataException extends MovieServiceException {

    public MovieDataException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}