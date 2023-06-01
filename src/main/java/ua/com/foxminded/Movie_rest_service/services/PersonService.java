package ua.com.foxminded.Movie_rest_service.services;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.Movie_rest_service.models.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    @Transactional
    List<Person> findAll(Pageable pageable);

    @Transactional
    Optional<Person> findById(Long id);

    @Transactional
    void deleteById(Long id);

    @Transactional
    void add(Person person);

    @Transactional
    void update(Person oldPerson, Person newPerson);

    @Transactional
    boolean isExists(Long id);
}