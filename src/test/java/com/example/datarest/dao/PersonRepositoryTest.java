package com.example.datarest.dao;

import com.example.datarest.model.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests here can access H2 with pre-populated test data from /src/test/resources/import.sql
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    /**
     *
     * @throws Exception
     */
    @Test
    public void test_verifyTestDataPreloaded_success() throws Exception {
        List<Person> personList = (List<Person>) personRepository.findAll();
        System.out.println("Person List: " + personList );
        assertFalse( personList.isEmpty() );
        assertEquals(1, personList.size());
    }

}
