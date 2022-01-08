package com.pharma.medicatiebewaking.Interface;

import org.springframework.data.repository.CrudRepository;

import com.pharma.medicatiebewaking.model.Medication;

import java.util.List;

public interface MedicationRepository extends CrudRepository<Medication, Integer> {
    Medication findByMedicationId(int medicationId);

    List<Medication> findAllByMedicationName(String medicationName);
}
