package ua.com.foxminded.Movie_rest_service.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.Movie_rest_service.models.Person;

import java.util.List;

@Service
public interface PersonService {
    @Transactional
    List<Person> findAll(Pageable pageable);

    @Transactional
    Person findById(Long id);

    @Transactional
    void deleteById(Long id);

    @Transactional
    void add(Person person);

    @Transactional
    void update(Person oldPerson, Person newPerson);

    @Transactional
    boolean isExists(Long id);
}