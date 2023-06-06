package ua.com.foxminded.Movie_rest_service.services.impl;

import org.springframework.stereotype.Service;
import ua.com.foxminded.Movie_rest_service.models.Rating;
import ua.com.foxminded.Movie_rest_service.repositories.RatingRepository;
import ua.com.foxminded.Movie_rest_service.services.RatingService;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public void add(Rating rating) {
        ratingRepository.save(rating);
    }
}