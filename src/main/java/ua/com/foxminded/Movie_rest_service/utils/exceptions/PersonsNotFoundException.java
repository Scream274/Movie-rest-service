package ua.com.foxminded.Movie_rest_service.utils.exceptions;

import org.springframework.http.HttpStatus;

public class PersonsNotFoundException extends MovieServiceException {

    public PersonsNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}