package ua.com.foxminded.Movie_rest_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.foxminded.Movie_rest_service.DTO.MovieDTO;
import ua.com.foxminded.Movie_rest_service.models.Movie;
import ua.com.foxminded.Movie_rest_service.models.Rating;
import ua.com.foxminded.Movie_rest_service.security.SecurityConfig;
import ua.com.foxminded.Movie_rest_service.services.MovieService;
import ua.com.foxminded.Movie_rest_service.services.RatingService;
import ua.com.foxminded.Movie_rest_service.utils.DTOconverters.MovieDTOConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(SecurityConfig.class)
public class MovieControllerTest {
    @MockBean
    private MovieService movieService;

    @MockBean
    private RatingService ratingService;

    @Autowired
    private MockMvc mockMvc;

    private Movie movie1;
    private Movie movie2;

    @BeforeAll
    public void setUp() {
        MovieDTOConverter.setRatingService(ratingService);
        movie1 = new Movie(1L,"Movie 1 - Title");
        movie2 = new Movie(2L,"Movie 2 - Super title");
    }

    @Test
    public void whenGetAllMoviesShouldReturnListOfAllDTOMovies() throws Exception {
        List<Movie> movies = Arrays.asList(movie1, movie2);
        when(movieService.findAll(any())).thenReturn(movies);

        mockMvc.perform(get("/api/v1/movies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].primaryTitle", is(movie1.getPrimaryTitle())));
    }

    @Test
    public void whenGetOneMovieShouldReturnMovieDTOById() throws Exception {
        Long movieId = movie1.getId();
        when(movieService.findById(movieId)).thenReturn(Optional.ofNullable(movie1));

        mockMvc.perform(get("/api/v1/movies/{id}", movieId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.primaryTitle", is(movie1.getPrimaryTitle())));
    }

    @Test
    @WithMockUser
    public void whenDeleteMovieWithValidParamShouldDeleteMovieFromDbAndReturnNoContentStatus() throws Exception {
        Long movieId = movie1.getId();
        when(movieService.isExists(movieId)).thenReturn(true).thenReturn(false);

        mockMvc.perform(delete("/api/v1/movies/{id}", movieId))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void whenDeleteMovieWithInvalidParamShouldReturnBadRequest() throws Exception {
        Long movieId = movie1.getId();
        when(movieService.isExists(movieId)).thenReturn(true).thenReturn(true);

        mockMvc.perform(delete("/api/v1/movies/{id}", movieId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Movie with id " + movieId + " was not deleted!"));
    }

    @Test
    @WithMockUser
    public void whenPostAddMovieShouldSaveMovieToDbAndReturnCreatedStatus() throws Exception {
        MovieDTO movieDTO = new MovieDTO("Test Movie");
        Long expectedId = 1234L;

        doAnswer(invocation -> {
            Movie movie = invocation.getArgument(0);
            movie.setId(expectedId);
            return null;
        }).when(movieService).add(any(Movie.class));

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(movieDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/movies/" + expectedId));

        verify(movieService).add(any(Movie.class));
    }

    @Test
    @WithMockUser
    public void whenPutUpdateMovieShouldUpdateMovieInDbAndReturnOk() throws Exception {
        Long movieId = movie1.getId();
        MovieDTO movieDTO = new MovieDTO("New Movie");
        when(movieService.findById(movieId)).thenReturn(Optional.ofNullable(movie1));

        mockMvc.perform(put("/api/v1/movies/{id}", movieId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(movieDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.primaryTitle", is(movieDTO.getPrimaryTitle())));

        verify(movieService).update(any(Movie.class), any(Movie.class));
    }

    @Test
    @WithMockUser
    public void whenPutUpdateMovieRatingWhenMovieWithNoRateYetShouldCreateMovieRatingInDbAndReturnOk() throws Exception {
        Long movieId = movie1.getId();
        Integer rate = 5;

        when(movieService.findById(movieId)).thenReturn(Optional.ofNullable(movie1));

        mockMvc.perform(put("/api/v1/movies/{id}/rating", movieId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rate.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rate").value(rate));

        verify(movieService).add(any(Movie.class));
    }

    @Test
    @WithMockUser
    public void whenPutUpdateMovieRatingWhenMovieHasRateShouldUpdateMovieRatingInDbAndReturnOk() throws Exception {
        Long movieId = movie1.getId();
        Integer rate = 5;
        Rating rating = new Rating(1L, 5.6D, 20);

        when(movieService.findById(movieId)).thenReturn(Optional.ofNullable(movie1));
        movie1.setRating(rating);

        doAnswer(invocation -> {
            Rating updatedRating = invocation.getArgument(0);
            Integer updatedRate = invocation.getArgument(1);

            Double newTotalRate = updatedRating.getRate() * updatedRating.getVotes() + updatedRate;
            Integer newVotesCount = updatedRating.getVotes() + 1;
            Double newRate = newTotalRate / newVotesCount;

            updatedRating.setRate(newRate);
            updatedRating.setVotes(newVotesCount);

            return null;
        }).when(ratingService).update(rating, rate);

        mockMvc.perform(put("/api/v1/movies/{id}/rating", movieId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rate.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rate").value(rate));

        verify(ratingService).update(rating, rate);
    }
}