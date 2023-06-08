package ua.com.foxminded.Movie_rest_service.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.Movie_rest_service.DTO.PersonDTO;
import ua.com.foxminded.Movie_rest_service.models.Person;
import ua.com.foxminded.Movie_rest_service.services.PersonService;
import ua.com.foxminded.Movie_rest_service.utils.DTOconverters.PersonDTOConverter;
import ua.com.foxminded.Movie_rest_service.utils.exceptions.MovieDataException;
import ua.com.foxminded.Movie_rest_service.utils.exceptions.PersonsNotFoundException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ua.com.foxminded.Movie_rest_service.utils.DTOconverters.PersonDTOConverter.convertFromDTO;
import static ua.com.foxminded.Movie_rest_service.utils.DTOconverters.PersonDTOConverter.convertToDTO;

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
        return convertToDTO(personService.findById(Long.valueOf(id))
                .orElseThrow(() -> new PersonsNotFoundException("Person with id " + id + " was not found!")));
    }

    @PostMapping
    public ResponseEntity<?> addNewPerson(@RequestBody PersonDTO personDTO) {
        Person person = convertFromDTO(personDTO);

        try {
            personService.add(person);
        } catch (DataIntegrityViolationException e) {
            throw new MovieDataException("Error! Person was not saved to DB!");
        }

        return ResponseEntity.created(URI.create("/persons/" + person.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable("id") String id, @RequestBody PersonDTO personDTO) {
        Person oldPerson = personService.findById(Long.valueOf(id))
                .orElseThrow(() -> new PersonsNotFoundException("Person with id " + id + " was not found!"));
        Person newPerson = convertFromDTO(personDTO);

        personService.update(oldPerson, newPerson);
        return ResponseEntity.ok(oldPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable("id") String id) {
        if (!personService.isExists(Long.valueOf(id))) {
            throw new PersonsNotFoundException("Person with id " + id + " was not found!");
        }

        personService.deleteById(Long.valueOf(id));

        if (personService.isExists(Long.valueOf(id))) {
            return ResponseEntity.badRequest().body("Person with id " + id + " was not deleted!");
        }

        return ResponseEntity.noContent().build();
    }
}