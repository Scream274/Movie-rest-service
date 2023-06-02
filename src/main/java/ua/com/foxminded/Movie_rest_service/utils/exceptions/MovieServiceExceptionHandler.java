package ua.com.foxminded.Movie_rest_service.utils.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.com.foxminded.Movie_rest_service.utils.errors.ErrorResponse;

@RestControllerAdvice
public class MovieServiceExceptionHandler {
    @ExceptionHandler(MovieServiceException.class)
    private ResponseEntity<ErrorResponse> handleException(MovieServiceException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, ex.getStatus());
    }
}