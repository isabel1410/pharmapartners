package com.pharma.medicatiebewaking.controller;

import com.pharma.medicatiebewaking.Interface.*;
import com.pharma.medicatiebewaking.model.Health;
import com.pharma.medicatiebewaking.model.MedicationDetails;
import com.pharma.medicatiebewaking.model.Person;
import com.pharma.medicatiebewaking.model.PersonMedication;
import net.minidev.json.parser.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonMedicationRepository personMedicationRepository;
    @Autowired
    private MedicationDetailsRepository medicationDetailsRepository;

    @PostMapping("/selectMedication")
    public @ResponseBody String addPersonMedication(@RequestParam int personId, @RequestParam int medicationDetailId, @RequestParam float intakeFrequency){
        HashMap<String, Object> data = new HashMap<>();

        if(intakeFrequency > 8 || intakeFrequency < 0.5){
            data.put("message", "De frequentie van uw inname moet tussen 0,5 en 8 keer per dag liggen");
            return new JSONObject(data).toString();
        }

        try {
            Person person = personRepository.findByPersonId(personId);

            MedicationDetails medicationDetails = medicationDetailsRepository.findByMedicationDetailsId(medicationDetailId);

            PersonMedication personMedication = new PersonMedication(medicationDetails, intakeFrequency);

//            Dit werkt volgens mij ook:
//            person.getPersonMedication().add(0,personMedication);

            List<PersonMedication> personMedicationList = person.getPersonMedication();
            personMedicationList.add(0, personMedication);
            person.setPersonMedication(personMedicationList);

            //Save person
            personRepository.save(person);

            data.put("message", "Toevoegen van medicatie is gelukt");

            return new JSONObject(data).toString();
        } catch (Exception ex){
            System.out.println(ex.toString());

            data.put("message", ex.toString());

            return new JSONObject(data).toString();
        }
    }

    @DeleteMapping("/deleteMed/{personMedicationId}")
    public @ResponseBody String deletePersonMedication(@PathVariable int personMedicationId){
        HashMap<String, Object> data = new HashMap<>();

        System.out.println(personMedicationId);

        try {
            PersonMedication personMedication = personMedicationRepository.findByPersonMedicationId(personMedicationId);

            Person person =  personRepository.findByPersonMedication(personMedication);
            List<PersonMedication> personMedicationList = person.getPersonMedication();
            personMedicationList.remove(personMedication);
            person.setPersonMedication(personMedicationList);

            personRepository.save(person);

            data.put("message", "Verwijderen van medicatie is gelukt");
            return new JSONObject(data).toString();
        } catch (Exception ex){
            data.put("message", ex.toString());
            return new JSONObject(data).toString();
        }
    }
}
