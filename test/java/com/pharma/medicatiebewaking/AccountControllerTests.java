package com.pharma.medicatiebewaking;

import com.pharma.medicatiebewaking.Interface.AccountRepository;
import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.controller.AccountController;
import com.pharma.medicatiebewaking.model.Account;
import com.pharma.medicatiebewaking.model.Person;
import com.sun.tools.jconsole.JConsoleContext;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AccountControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private PersonRepository personRepository;

    @Test
    void LoginTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "mohamed@example.com")
                .param("password", "Test1234$");

        MvcResult result = mvc.perform(request).andReturn();
        MvcResult results = mvc.perform(request).andExpect(status().isUnauthorized()).andReturn();


        HashMap<String, Object> data = new HashMap<>();
        data.put("message", "Email of wachtwoord is onjuist.");
        JSONObject jsonObject = new JSONObject(data);

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals(jsonObject.toString()), "Wrong password passed");
    }

    @Test
    void RegisterTest() throws Exception {
        String gDatum = "10/01/2020";
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(gDatum);
        Person person = new Person("test", "test", date);
        Account account = new Account("test@example.com", "Test1234!", "salt", person);

        when(accountRepository.save(account)).thenReturn(account);

        RequestBuilder request = MockMvcRequestBuilders.post("/account/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "test1@example.com")
                .param("password", "Test1234!")
                .param("repeatPassword", "Test1234!")
                .param("firstname", "test1")
                .param("lastname", "test1")
                .param("date", "10/02/2012");
        MvcResult result = mvc.perform(request).andReturn();

        System.out.println(result);
        System.out.println(result.getResponse().getContentAsString());

        HashMap<String, Object> data = new HashMap<>();
        data.put("message", "Registratie gelukt.");
        JSONObject jsonObject = new JSONObject(data);

        //Assert
        Assert.isTrue(result.getResponse().getContentAsString().equals(jsonObject.toString()), "Registration test");
    }

//    @Mock
//    AccountRepository accountRepository;
//
//    @InjectMocks
//    AccountController accountController;
//
//    @Test
//    public void LoginTest() throws NoSuchAlgorithmException {
//        // Given
//        HttpServletResponse response = null;
//        String email = "Test1@example.com";
//        String password= "Test1234&";
//
//        // When
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        String succeeded = accountController.login(email,password, response);
//
//        // Assert
//        Assert.isTrue(succeeded.equals("{\"message\":\"Login gelukt.\"}"), "Het is hetzelfde");
//    }

}
