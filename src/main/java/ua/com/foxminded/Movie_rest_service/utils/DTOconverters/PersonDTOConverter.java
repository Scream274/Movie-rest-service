package ua.com.foxminded.Movie_rest_service.utils.DTOconverters;

import org.springframework.stereotype.Service;
import ua.com.foxminded.Movie_rest_service.DTO.PersonDTO;
import ua.com.foxminded.Movie_rest_service.models.Person;

@Service
public class PersonDTOConverter {

    public static PersonDTO convertToDTO(Person person) {
        PersonDTO personDTO = new PersonDTO();

        personDTO.setName(person.getName());
        personDTO.setProfession(person.getProfession());
        personDTO.setBirthYear(person.getBirthYear());
        personDTO.setDeathYear(person.getDeathYear());

        return personDTO;
    }

    public static Person convertFromDTO(PersonDTO personDTO) {

        //TODO
        Person person = new Person();
        return person;
    }
}
