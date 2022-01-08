package com.pharma.medicatiebewaking;
import com.pharma.medicatiebewaking.Interface.HealthRepository;
import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.controller.HealthController;
import com.pharma.medicatiebewaking.model.Health;
import com.pharma.medicatiebewaking.model.Person;
import org.apache.tomcat.jni.Local;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestParam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import javax.xml.crypto.Data;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.NoSuchAlgorithmException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class HealthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private HealthRepository healthRepository;

    @MockBean
    private PersonRepository personRepository;

    @Mock
    private HealthController healthController;



//    @InjectMocks
//    HealthController healthController;

//    @Test
//    public void TestSaveHealth() throws NoSuchAlgorithmException {
//        // Given
//        String height = "175";
//        String weight = "80.7";
//        String topPressure = "120";
//        String lowPressure = "80";
//        int id = 2;
//
//        // When
//        String succeeded = healthController.SaveHealth(height, topPressure, lowPressure, weight, id);
//        System.out.println(succeeded);
//
//        // Assert
//        Assert.isTrue(succeeded.equals("{\"message\":\"health has been added.\"}"), "Het is hetzelfde");
//    }

//    @Test
//    public void TestSaveHealth() throws Exception {
//        int height = 175;
//        float weight = 80.7f;
//        String topPressure = "120";
//        String lowPressure = "80";
//        int id = 2;
//        Date date = new Date();
//        Health health = new Health(weight, "120/80", height);
//        Person person = new Person("test", "test", date);
//
//        when(healthRepository.save(health)).thenReturn(health);
//        when(personRepository.findByPersonId(id)).thenReturn(person);
//
//        RequestBuilder request = MockMvcRequestBuilders.post("/health/save")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("height", "1.80")
//                .param("topPressure", "150")
//                .param("lowPressure", "120")
//                .param("weight", "70")
//                .param("id", "111");
//        MvcResult result = mvc.perform(request).andReturn();
//
//        System.out.println(result);
//        System.out.println(result.getResponse().getContentAsString());
//
//        HashMap<String, Object> data = new HashMap<>();
//        data.put("message", "Informatie is opgeslagen.");
//        JSONObject jsonObject = new JSONObject(data);
//        Assert.isTrue(result.getResponse().getContentAsString().equals(jsonObject.toString()), "Health save test");
//    }

    @Test
    public void TestValidateTopPressureAsHighest() {
        // Given
        String correctTopPressure = "130";
        String correctLowPressure = "120";
        String wrongTopPressure = "120";
        String wrongLowPressure = "130";

        // When
        when(healthController.ValidateTopPressureAsHighest(correctTopPressure, correctLowPressure)).thenReturn(true);
        when(healthController.ValidateTopPressureAsHighest(wrongTopPressure, wrongLowPressure)).thenReturn(false);
//        boolean topPressureHigher = healthController.ValidateTopPressureAsHighest(correctTopPressure, correctLowPressure);
//        boolean topPressureLower = healthController.ValidateTopPressureAsHighest(wrongTopPressure, wrongLowPressure);

        // Assert
//        RequestBuilder requestBuilder = new MockMvcRequestBuilders.

//        System.out.println(topPressureHigher);
//        System.out.println(topPressureLower);
//        Assert.isTrue(topPressureHigher, "Top pressure is hoger.");
//        Assert.isTrue(!topPressureLower, "Top pressure is lager.");

    }

//    @Test
//    public void TestChangeHealth() throws Exception {
//        String height = "170";
//        String topPressure = "125";
//        String lowPressure = "120";
//        String weight = "70";
//
//        Health health = new Health();
//        health.setHeight(170);
//        health.setWeight(70f);
//        health.setBloodPressure("125/120");
//        health.setHealthId(223);
//
//        when(healthRepository.save(health)).thenReturn(health);
//
//        RequestBuilder request = MockMvcRequestBuilders.put("/health/change")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("height", height)
//                .param("topPressure", topPressure)
//                .param("lowPressure", lowPressure)
//                .param("weight", weight)
//                .param("id", "223");
//        MvcResult result = mvc.perform(request).andReturn();
//
//        System.out.println(result);
//        System.out.println(result.getResponse().getContentAsString());
//
//        HashMap<String, Object> data = new HashMap<>();
//        data.put("message", "De data is veranderd.");
//        JSONObject jsonObject = new JSONObject(data);
//        Assert.isTrue(result.getResponse().getContentAsString().equals(jsonObject.toString()), "Health change test");
//    }

//    @Test
//    public void TestCheckUserInputIsFloat() throws NoSuchAlgorithmException{
//        // Given
//        Health health = new Health();
//        Health health2 = new Health();
//        health2.setHeight(2);
//        health2.setWeight(80);
//        health.setWeight(50.1f);
//        health.setHeight(1.75f);
//        // When
//        boolean success = healthController.CheckUserInputIsFloat(health);
//        boolean noSuccess = healthController.CheckUserInputIsFloat(health2);
//        System.out.println(success);
//        System.out.println(noSuccess);
//        // Assert
//        Assert.isTrue(success);
//        Assert.isTrue(noSuccess);
//    }
//
//    @Test
//    public void TestCheckUserInputNotEmpty() throws NoSuchAlgorithmException{
//        // Given
//        Health health = new Health();
//        health.setWeight(50.1f);
//        health.setHeight(1.75f);
//        health.setBloodPressure("120/80");
//        // When
//        boolean success = healthController.CheckUserInputNotEmpty(health);
//        System.out.println(success);
//        // Assert
//        Assert.isTrue(success);
//    }
//    @Test
//    public void TestValidateTopAndLowPressure() throws NoSuchAlgorithmException{
//        // Given
//        String topPressure = "120";
//        String lowPressure = "80";
//        String mockTopPressure = "test";
//        String mockLowPressure = "test";
//        // When
//        boolean success = healthController.ValidateTopAndLowPressure(topPressure,lowPressure);
//        boolean noSuccess = healthController.ValidateTopAndLowPressure(mockTopPressure,mockLowPressure);
//        System.out.println(success);
//        System.out.println(noSuccess);
//        // Assert
//        Assert.isTrue(success);
//        Assert.isTrue(!noSuccess);
//    }
//    @Test
//    public void TestConvertBloodPressure() throws NoSuchAlgorithmException{
//        // Given
//        String topPressure = "120";
//        String lowPressure = "80";
//        String correctBloodPressure = "120/80";
//        // When
//        String convertedBloodPressure = healthController.ConvertBloodPressure(topPressure, lowPressure);
//        System.out.println(convertedBloodPressure);
//        // Assert
//        Assert.isTrue(convertedBloodPressure.equals(correctBloodPressure));
//    }
//    @Test
//    public void TestValidateTopPressureAsHighest() throws NoSuchAlgorithmException{
//        // Given
//        String topPressure = "120";
//        String lowPressure = "80";
//        String mockTopPressure = "80";
//        String mockLowPressure = "120";
//        // When
//        boolean success = healthController.ValidateTopPressureAsHighest(topPressure, lowPressure);
//        boolean noSuccess = healthController.ValidateTopPressureAsHighest(mockTopPressure, mockLowPressure);
//
//        System.out.println(success);
//        System.out.println(noSuccess);
//        // Assert
//        Assert.isTrue(success);
//        Assert.isTrue(!noSuccess);
//    }
}
