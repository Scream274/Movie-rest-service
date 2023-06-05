package ua.com.foxminded.Movie_rest_service.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.Movie_rest_service.DTO.PersonDTO;
import ua.com.foxminded.Movie_rest_service.models.Person;
import ua.com.foxminded.Movie_rest_service.services.PersonService;
import ua.com.foxminded.Movie_rest_service.utils.DTOconverters.PersonDTOConverter;
import ua.com.foxminded.Movie_rest_service.utils.exceptions.PersonsNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ua.com.foxminded.Movie_rest_service.utils.DTOconverters.PersonDTOConverter.convertToDTO;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {
    private final PersonService personService;
    private final int PAGE_SIZE = 5;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<PersonDTO> getAllMovies(@RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        List<Person> persons = personService.findAll(pageable);

        if (persons.isEmpty()) {
            throw new PersonsNotFoundException("Persons was not found!");
        }

        return persons.stream().map(PersonDTOConverter::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDTO getCarByNumber(@PathVariable("id") String id) {
        return convertToDTO(personService.findById(Long.valueOf(id)));
    }
}