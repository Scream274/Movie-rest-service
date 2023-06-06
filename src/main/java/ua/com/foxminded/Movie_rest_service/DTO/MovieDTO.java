package ua.com.foxminded.Movie_rest_service.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private String type;

    private String primaryTitle;

    private String originalTitle;

    private Boolean isAdult;

    private Integer startYear;

    private Integer endYear;

    private Integer runtimeMinutes;

    private String genres;

    private Double rate;
}