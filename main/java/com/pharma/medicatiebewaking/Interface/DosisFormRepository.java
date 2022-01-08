package com.pharma.medicatiebewaking.Interface;

import org.springframework.data.repository.CrudRepository;
import com.pharma.medicatiebewaking.model.DosisForm;

public interface DosisFormRepository extends CrudRepository<DosisForm, Integer> {
}
