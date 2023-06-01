package ua.com.foxminded.Movie_rest_service.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class MoviePersonId implements Serializable {
    private Long movie;
    private Long person;
}