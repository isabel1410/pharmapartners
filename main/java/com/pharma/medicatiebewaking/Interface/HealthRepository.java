package com.pharma.medicatiebewaking.Interface;

import org.springframework.data.repository.CrudRepository;
import com.pharma.medicatiebewaking.model.Health;

public interface HealthRepository extends CrudRepository<Health, Integer>{
}
