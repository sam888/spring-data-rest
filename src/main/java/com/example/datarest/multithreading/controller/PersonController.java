package com.example.datarest.multithreading.controller;

import com.example.datarest.model.Person;
import com.example.datarest.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController
@Slf4j
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/people", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> findAll() {
        return personService.findAll();
    }

    @PostMapping("/people")
    public Person savePerson(@RequestBody Person newPerson) {
        return personService.save(newPerson);
    }

    @RequestMapping(value = "/people/{id}", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        Optional<Person> person = personService.findById(id);
        return person
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /*
    @DeleteMapping("/people/{id}")
    void deleteEmployee(@PathVariable Long id) {
        service.deleteById(id);
    }*/
}
