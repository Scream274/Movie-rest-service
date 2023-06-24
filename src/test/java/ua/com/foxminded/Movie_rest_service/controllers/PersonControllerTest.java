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
import ua.com.foxminded.Movie_rest_service.DTO.PersonDTO;
import ua.com.foxminded.Movie_rest_service.models.Person;
import ua.com.foxminded.Movie_rest_service.security.SecurityConfig;
import ua.com.foxminded.Movie_rest_service.services.PersonService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(SecurityConfig.class)
public class PersonControllerTest {
    @MockBean
    private PersonService personService;

    @Autowired
    private MockMvc mockMvc;

    private Person person1;
    private Person person2;

    @BeforeAll
    public void setUp() {
        person1 = new Person(1L, "Person X");
        person2 = new Person(2L, "Person Y");
    }

    @Test
    public void whenGetAllPersonsShouldReturnListOfAllDTOPersons() throws Exception {
        List<Person> persons = Arrays.asList(person1, person2);
        when(personService.findAll(any())).thenReturn(persons);

        mockMvc.perform(get("/api/v1/persons"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(person1.getName())));
    }

    @Test
    public void whenGetOnePersonShouldReturnPersonDTOById() throws Exception {
        Long personId = person1.getId();
        when(personService.findById(personId)).thenReturn(Optional.ofNullable(person1));

        mockMvc.perform(get("/api/v1/persons/{id}", personId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(person1.getName())));
    }

    @Test
    @WithMockUser
    public void whenDeletePersonWithValidParamShouldDeletePersonFromDbAndReturnNoContentStatus() throws Exception {
        Long personId = person1.getId();
        when(personService.isExists(personId)).thenReturn(true).thenReturn(false);

        mockMvc.perform(delete("/api/v1/persons/{id}", personId))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void whenDeletePersonWithInvalidParamShouldReturnBadRequest() throws Exception {
        Long personId = person1.getId();
        when(personService.isExists(personId)).thenReturn(true).thenReturn(true);

        mockMvc.perform(delete("/api/v1/persons/{id}", personId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Person with id " + personId + " was not deleted!"));
    }

    @Test
    @WithMockUser
    public void whenPostAddNewPersonShouldSavePersonToDbAndReturnCreatedStatus() throws Exception {
        PersonDTO personDTO = new PersonDTO("Test Person");
        Long expectedId = 1234L;

        doAnswer(invocation -> {
            Person person = invocation.getArgument(0);
            person.setId(expectedId);
            return null;
        }).when(personService).add(any(Person.class));

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(personDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/persons/" + expectedId));

        verify(personService).add(any(Person.class));
    }

    @Test
    @WithMockUser
    public void whenPutUpdatePersonShouldUpdatePersonInDbAndReturnOk() throws Exception {
        Long personId = person1.getId();
        PersonDTO personDTO = new PersonDTO("Test Person");
        when(personService.findById(personId)).thenReturn(Optional.ofNullable(person1));

        mockMvc.perform(put("/api/v1/persons/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(personDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(personDTO.getName())));

        verify(personService).update(any(Person.class), any(Person.class));
    }
}