package ua.com.foxminded.Movie_rest_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Get all persons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all persons",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Persons not found",
                    content = @Content(mediaType = "application/json"))})
    @GetMapping
    public List<PersonDTO> getAllMovies(@RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        List<Person> persons = personService.findAll(pageable);

        if (persons.isEmpty()) {
            throw new PersonsNotFoundException("Persons was not found!");
        }

        return persons.stream().map(PersonDTOConverter::convertToDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Get person by its Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the person",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Persons not found",
                    content = @Content(mediaType = "application/json"))})
    @GetMapping("/{id}")
    public PersonDTO getCarByNumber(@PathVariable("id") String id) {
        return convertToDTO(personService.findById(Long.valueOf(id))
                .orElseThrow(() -> new PersonsNotFoundException("Person with id " + id + " was not found!")));
    }

    @Operation(summary = "Add new person", security = @SecurityRequirement(name = "bearerAuth"))
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

    @Operation(summary = "Update person by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable("id") String id, @RequestBody PersonDTO personDTO) {
        Person oldPerson = personService.findById(Long.valueOf(id))
                .orElseThrow(() -> new PersonsNotFoundException("Person with id " + id + " was not found!"));
        Person newPerson = convertFromDTO(personDTO);

        personService.update(oldPerson, newPerson);
        return ResponseEntity.ok(oldPerson);
    }

    @Operation(summary = "Delete person by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete person",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Person was not deleted",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Person not found",
                    content = @Content(mediaType = "application/json"))})
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