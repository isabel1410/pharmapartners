package com.pharma.medicatiebewaking.controller;

import com.pharma.medicatiebewaking.Interface.MedicationDetailsRepository;
import com.pharma.medicatiebewaking.Interface.PersonMedicationRepository;
import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.model.MedicationDetails;
import com.pharma.medicatiebewaking.model.Person;
import com.pharma.medicatiebewaking.model.PersonMedication;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PersonRepository personRepository;

    @MockBean
    PersonMedicationRepository personMedicationRepository;

    @MockBean
    MedicationDetailsRepository medicationDetailsRepository;

    @Test
    void addPersonMedication_InvalidFrequency() throws Exception {
        //Intake frequency should be in between 0,5 and 8 times a day
        RequestBuilder request = MockMvcRequestBuilders.post("/person/selectMedication")
                .contentType(MediaType.APPLICATION_JSON)
                .param("personId", "16")
                .param("medicationDetailId", "1")
                .param("intakeFrequency", "9");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals(
                "{\"message\":\"De frequentie van uw inname moet tussen 0,5 en 8 keer per dag liggen\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void addPersonMedication_NullPointerException() throws Exception {
        //No Person with this id is found in the database
        RequestBuilder request = MockMvcRequestBuilders.post("/person/selectMedication")
                .contentType(MediaType.APPLICATION_JSON)
                .param("personId", "1008")
                .param("medicationDetailId", "1")
                .param("intakeFrequency", "3");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals(
                "{\"message\":\"java.lang.NullPointerException\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void addPersonMedication_Success() throws Exception {
        //Given
        MedicationDetails medicationDetails = new MedicationDetails();
        Person person = new Person();
        person.setPersonMedication(new ArrayList<>());

        //When
        when(personRepository.findByPersonId(10)).thenReturn(person);
        when(medicationDetailsRepository.findByMedicationDetailsId(1)).thenReturn(medicationDetails);

        //Then
        RequestBuilder request = MockMvcRequestBuilders.post("/person/selectMedication")
                .contentType(MediaType.APPLICATION_JSON)
                .param("personId", "10")
                .param("medicationDetailId", "1")
                .param("intakeFrequency", "3");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals(
                "{\"message\":\"Toevoegen van medicatie is gelukt\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void deletePersonMedication_NullPointedException() throws Exception {
        //No PersonMedication with this id is found in the database
        RequestBuilder request = MockMvcRequestBuilders.delete("/person/deleteMed/12")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals(
                "{\"message\":\"java.lang.NullPointerException\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void deletePersonMedication_Success() throws Exception {
        //Given
        PersonMedication personMedication = new PersonMedication();
        Person person = new Person();
        person.setPersonMedication(new ArrayList<>());

        //When
        when(personMedicationRepository.findByPersonMedicationId(12)).thenReturn(personMedication);
        when(personRepository.findByPersonMedication(personMedication)).thenReturn(person);

        //Then
        RequestBuilder request = MockMvcRequestBuilders.delete("/person/deleteMed/12")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals(
                "{\"message\":\"Verwijderen van medicatie is gelukt\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }
}