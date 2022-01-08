package com.pharma.medicatiebewaking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharma.medicatiebewaking.Interface.AccountRepository;
import com.pharma.medicatiebewaking.Interface.HealthRepository;
import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.model.Health;
import com.pharma.medicatiebewaking.model.Person;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class HealthControllerTest2 {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PersonRepository repository;

    @MockBean
    HealthRepository healthRepository;

    @InjectMocks
    HealthController controller;

    @Test
    void validateTopPressureAsHighestTest() {
        String topPressure = "120";
        String lowPressure = "80";
        boolean result1 = controller.ValidateTopPressureAsHighest(topPressure, lowPressure);
        boolean result2 = controller.ValidateTopPressureAsHighest(lowPressure, topPressure);
        Assert.isTrue(result1, "TopPressure should be higher than LowPressure. Returned result is: " + result1);
        Assert.isTrue(!result2, "TopPressure shouldn't be higher than LowPressure. Returned result is: " + result2);
    }

    @Test
    void validateTopAndLowPressureTest() {
        String topPressure1 = "120";
        String topPressure2 = "120x";
        String lowPressure1 = "80";
        String lowPressure2 = "80x";
        boolean result1 = controller.ValidateTopAndLowPressure(topPressure1, lowPressure1);
        boolean result2 = controller.ValidateTopAndLowPressure(topPressure1, lowPressure2);
        boolean result3 = controller.ValidateTopAndLowPressure(topPressure2, lowPressure1);
        boolean result4 = controller.ValidateTopAndLowPressure(topPressure2, lowPressure2);
        Assert.isTrue(result1, "Result should be true. Returned result is: " + result1);
        Assert.isTrue(!result2, "Result should be false, because low pressure is invalid. Returned result is: " + result2);
        Assert.isTrue(!result3, "Result should be false, because top pressure is invalid. Returned result is: " + result3);
        Assert.isTrue(!result4, "Result should be false, because both pressures are invalid. Returned result is: " + result4);
    }

    @Test
    void saveHealthTest_EmptyValue() throws Exception {
        //Then
        RequestBuilder request = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "height")
                .param("value", "")
                .param("id", "689");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals("{\"message\":\"Alle velden dienen ingevuld te zijn\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void saveHealthTest_UnknownPerson() throws Exception {
        //Then
        RequestBuilder request = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "height")
                .param("value", "155")
                .param("id", "000");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals("{\"message\":\"Gebruiker bestaat niet\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void saveHealthTest_InvalidHeight() throws Exception {
        //Given
        Person person = new Person();

        //When
        when(repository.findByPersonId(689)).thenReturn(person);

        //Then
        RequestBuilder request = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "height")
                .param("value", "1,88")
                .param("id", "689");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals("{\"message\":\"De lengte mag geen komma bevatten\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void saveHealthTest_MissingBloodPressure() throws Exception {
        //Given
        Person person = new Person();

        //When
        when(repository.findByPersonId(689)).thenReturn(person);

        //Then
        RequestBuilder request = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "bloodPressure")
                .param("value", "120/")
                .param("id", "689");
        MvcResult result = mockMvc.perform(request).andReturn();

        RequestBuilder request2 = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "bloodPressure")
                .param("value", "/80")
                .param("id", "689");
        MvcResult result2 = mockMvc.perform(request).andReturn();

        RequestBuilder request3 = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "bloodPressure")
                .param("value", "/")
                .param("id", "689");
        MvcResult result3 = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals("{\"message\":\"Beide bloeddrukwaarden moeten ingevuld zijn\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
        Assert.isTrue(result2.getResponse().getContentAsString().equals("{\"message\":\"Beide bloeddrukwaarden moeten ingevuld zijn\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
        Assert.isTrue(result3.getResponse().getContentAsString().equals("{\"message\":\"Beide bloeddrukwaarden moeten ingevuld zijn\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void saveHealthTest_MissingColumnName() throws Exception {
        //Given
        Person person = new Person();

        //When
        when(repository.findByPersonId(689)).thenReturn(person);

        //Then
        RequestBuilder request = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "")
                .param("value", "188")
                .param("id", "689");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals("{\"message\":\"Unknown column name\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void saveHealthTest_SuccessHeight() throws Exception {
        //Given
        Person person = new Person();
        Health health = new Health();
        person.setHealth(health);

        //When
        when(repository.findByPersonId(689)).thenReturn(person);

        //Then
        RequestBuilder request = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "height")
                .param("value", "188")
                .param("id", "689");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals("{\"message\":\"Uw lengte is opgeslagen\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void saveHealthTest_SuccessWeight() throws Exception {
        //Given
        Person person = new Person();
        Health health = new Health();
        person.setHealth(health);

        //When
        when(repository.findByPersonId(689)).thenReturn(person);

        //Then
        RequestBuilder request = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "weight")
                .param("value", "85")
                .param("id", "689");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals("{\"message\":\"Uw gewicht is opgeslagen\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void saveHealthTest_SuccessBloodPressure() throws Exception {
        //Given
        Person person = new Person();
        Health health = new Health();
        person.setHealth(health);

        //When
        when(repository.findByPersonId(689)).thenReturn(person);

        //Then
        RequestBuilder request = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "bloodPressure")
                .param("value", "115/75")
                .param("id", "689");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals("{\"message\":\"Uw bloeddruk is opgeslagen\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void saveHealthTest_SuccessClcr() throws Exception {
        //Given
        Person person = new Person();
        Health health = new Health();
        person.setHealth(health);

        //When
        when(repository.findByPersonId(689)).thenReturn(person);

        //Then
        RequestBuilder request = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "clcr")
                .param("value", "45")
                .param("id", "689");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals("{\"message\":\"Uw creatineklaring is opgeslagen\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }

    @Test
    void saveHealthTest_SuccessClcrMonth() throws Exception {
        //Given
        Person person = new Person();
        Health health = new Health();
        person.setHealth(health);

        //When
        when(repository.findByPersonId(689)).thenReturn(person);

        //Then
        RequestBuilder request = MockMvcRequestBuilders.put("/health/save")
                .contentType(MediaType.APPLICATION_JSON)
                .param("columnName", "clcrMonth")
                .param("value", "5")
                .param("id", "689");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals("{\"message\":\"Uw laatste meting creatineklaring is opgeslagen\"}"),
                "Incorrect result. Returned result: " + result.getResponse().getContentAsString());
    }
}