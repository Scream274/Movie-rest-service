package ua.com.foxminded.Movie_rest_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private String name;
    private Integer birthYear;
    private Integer deathYear;
    private String profession;

    public PersonDTO(String name) {
        this.name = name;
    }
}