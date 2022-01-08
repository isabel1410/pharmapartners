package com.pharma.medicatiebewaking.Interface;

import org.springframework.data.repository.CrudRepository;
import com.pharma.medicatiebewaking.model.MedicationDetails;

import java.util.List;

public interface MedicationDetailsRepository extends CrudRepository<MedicationDetails, Integer> {
    MedicationDetails findByMedicationDetailsId(int medicationDetailsId);
}
