package com.pharma.medicatiebewaking.controller;

import com.pharma.medicatiebewaking.Interface.MedicationDetailsRepository;
import com.pharma.medicatiebewaking.Interface.MedicationRepository;
import com.pharma.medicatiebewaking.Interface.PersonMedicationRepository;
import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.model.Medication;
import com.pharma.medicatiebewaking.model.MedicationDetails;
import com.pharma.medicatiebewaking.model.Person;
import com.pharma.medicatiebewaking.model.PersonMedication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/medication")
public class MedicationController {
    @Autowired
    private MedicationRepository medicationRepository;
    @Autowired
    private MedicationDetailsRepository medicationDetailsRepository;
    @Autowired
    private PersonMedicationRepository personMedicationRepository;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<Medication> getAllMedication(){
        return medicationRepository.findAll();
    }

    @GetMapping("/allMedicationDetails")
    public @ResponseBody Iterable<MedicationDetails> getAllMedicationDetails() {
        Person person = personRepository.findByPersonId(41);

        List<PersonMedication> personMedicationList =  person.getPersonMedication();

        List<MedicationDetails> medicationDetailsList = (List<MedicationDetails>) medicationDetailsRepository.findAll();

        for(PersonMedication personMedication : personMedicationList){
            medicationDetailsList.remove(personMedication.getMedicationDetails());
        }

        return medicationDetailsList;
    }

    @GetMapping("/allSelectedMedicationDetails")
    public @ResponseBody Iterable<PersonMedication> getAllSelectedMedicationDetails(){
        Person person = personRepository.findByPersonId(41);
        return person.getPersonMedication();
    }

}
