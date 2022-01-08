package com.pharma.medicatiebewaking.Interface;

import com.pharma.medicatiebewaking.model.MedicationDetails;
import com.pharma.medicatiebewaking.model.Person;
import com.pharma.medicatiebewaking.model.PersonMedication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    Person findByPersonId(int personId);
    Person findByPersonMedication(PersonMedication personMedication);
}

