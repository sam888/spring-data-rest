package com.example.datarest.service;

import com.example.datarest.dao.PersonRepository;
import com.example.datarest.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Person save(Person newPerson) {
        return personRepository.save( newPerson );
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }
}
