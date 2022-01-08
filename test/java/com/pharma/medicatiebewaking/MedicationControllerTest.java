package com.pharma.medicatiebewaking;

import com.pharma.medicatiebewaking.Interface.MedicationDetailsRepository;
import com.pharma.medicatiebewaking.Interface.MedicationRepository;
import com.pharma.medicatiebewaking.Interface.PersonMedicationRepository;
import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.controller.MedicationController;
import com.pharma.medicatiebewaking.model.*;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest({MedicationController.class})
public class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private MedicationDetailsRepository medicationDetailsRepository;

    @Test
    public void getAllMedicationTest() throws Exception {
        // Create a person
        Date date = new Date();
        Person person = new Person("Test", "Person", date);

        // Create medication with details
        Medication med1 = new Medication(1, "Paracetamol");
        Medication med2 = new Medication(2, "Ibuprofen");
        Medication med3 = new Medication(3, "Diclofenac");
        DosisForm dosisForm = new DosisForm(5, "Tablet");
        float strength1 = 500;
        float strength2 = 400;
        float strength3 = 600;
        MedicationDetails medicationDetails1 = new MedicationDetails(med1, dosisForm, strength1);
        MedicationDetails medicationDetails2 = new MedicationDetails(med2, dosisForm, strength2);
        MedicationDetails medicationDetails3 = new MedicationDetails(med3, dosisForm, strength3);

        // Create list with all the medication
        List<MedicationDetails> medicationDetailsList = new ArrayList<>();
        medicationDetailsList.add(medicationDetails1);
        medicationDetailsList.add(medicationDetails2);
        medicationDetailsList.add(medicationDetails3);

        // Create personalized medication
        PersonMedication personMedication1 = new PersonMedication(medicationDetails1, 3);

        // Create list with personMedication
        List<PersonMedication> personMedicationList = new ArrayList<>();
        personMedicationList.add(personMedication1);
        person.setPersonMedication(personMedicationList);

        // Create a list with medication that hasn't been selected by person
        List<MedicationDetails> resultDetails = new ArrayList<>();
        resultDetails.add(medicationDetails2);
        resultDetails.add(medicationDetails3);

        when(personRepository.findByPersonId(41)).thenReturn(person);
        //when(person.getPersonMedication()).thenReturn(personMedicationList);
        when(medicationDetailsRepository.findAll()).thenReturn(medicationDetailsList);


        RequestBuilder request = MockMvcRequestBuilders.get("/medication/allMedicationDetails")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();

        System.out.println(result);
        System.out.println(result.getResponse().getContentAsString());

        JSONObject json1 = new JSONObject(personMedicationList);
        JSONObject json2 = new JSONObject(medicationDetailsList);
        JSONObject json3 = new JSONObject(resultDetails);

        Assert.isTrue(result.equals(json3), "Results should be the same");

    }

//    @Test
//    public void TestGetAllMedicationDetails() throws NoSuchAlgorithmException {
//
//    }
//
//    @Test
//    public void TestGetAllSelectedMedicationDetails() throws NoSuchAlgorithmException {
//
//    }

}
