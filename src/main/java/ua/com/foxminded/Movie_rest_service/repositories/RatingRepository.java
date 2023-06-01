package ua.com.foxminded.Movie_rest_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.Movie_rest_service.models.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}