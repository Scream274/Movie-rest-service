package ua.com.foxminded.Movie_rest_service.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "movies_persons")
@IdClass(MoviePersonId.class)
public class MoviePerson {

    @Id
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Id
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "category")
    private String category;

    @Column(name = "job")
    private String job;

    @Column(name = "character")
    private String character;
}