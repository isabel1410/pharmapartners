package com.pharma.medicatiebewaking.Interface;

import com.pharma.medicatiebewaking.model.PersonMedication;
import org.springframework.data.repository.CrudRepository;
import com.pharma.medicatiebewaking.model.MedicationDetails;

import java.util.List;

public interface PersonMedicationRepository extends CrudRepository<PersonMedication, Integer>{
    PersonMedication findByPersonMedicationId(int personMedicationId);
}
