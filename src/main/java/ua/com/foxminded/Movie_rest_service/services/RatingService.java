package ua.com.foxminded.Movie_rest_service.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.Movie_rest_service.models.Rating;

@Service
public interface RatingService {

    @Transactional
    void add(Rating rating);
}