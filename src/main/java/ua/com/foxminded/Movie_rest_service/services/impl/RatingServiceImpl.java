package ua.com.foxminded.Movie_rest_service.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.Movie_rest_service.models.Rating;
import ua.com.foxminded.Movie_rest_service.repositories.RatingRepository;
import ua.com.foxminded.Movie_rest_service.services.RatingService;

import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    @Transactional
    public void add(Rating rating) {
        System.out.println("Rate id: " + rating.getId());

        ratingRepository.save(rating);
    }

    @Override
    @Transactional
    public void update(Rating rating, Integer rate) {
        Double newTotalRate = rating.getRate() * rating.getVotes() + rate;
        Integer newVotesCount = rating.getVotes() + 1;
        Double newRate = newTotalRate / newVotesCount;

        rating.setRate(newRate);
        rating.setVotes(newVotesCount);

        ratingRepository.save(rating);
    }

    @Override
    public Optional<Rating> findById(Long id) {
        return ratingRepository.findById(id);
    }
}