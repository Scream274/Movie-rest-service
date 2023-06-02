package ua.com.foxminded.Movie_rest_service.utils.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MovieServiceException extends RuntimeException{
    private final HttpStatus status;

    public MovieServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}