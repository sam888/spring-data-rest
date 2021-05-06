package com.example.datarest.dao;

import com.example.datarest.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
@SuppressWarnings("unused")
public interface PersonRepository extends JpaRepository<Person, Long> {

	List<Person> findByLastName(@Param("name") String name);

}
