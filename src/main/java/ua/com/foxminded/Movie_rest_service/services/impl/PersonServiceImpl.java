package ua.com.foxminded.Movie_rest_service.services.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.Movie_rest_service.models.Person;
import ua.com.foxminded.Movie_rest_service.repositories.PersonRepository;
import ua.com.foxminded.Movie_rest_service.services.PersonService;

import java.util.List;
import java.util.Optional;

public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public List<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable).getContent();
    }

    @Override
    @Transactional
    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void add(Person person) {
        personRepository.save(person);
    }

    @Override
    @Transactional
    public void update(Person oldPerson, Person newPerson) {

        oldPerson.setBirthYear(newPerson.getBirthYear());
        oldPerson.setDeathYear(newPerson.getDeathYear());
        oldPerson.setName(newPerson.getName());
        oldPerson.setProfession(newPerson.getProfession());

        personRepository.save(oldPerson);
    }

    @Override
    @Transactional
    public boolean isExists(Long id) {
        return personRepository.existsById(id);
    }
}