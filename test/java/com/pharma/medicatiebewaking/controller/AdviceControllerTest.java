package com.pharma.medicatiebewaking.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdviceControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void prepareAdvice_valid() throws Exception{

        HashMap<String, Object> data =  new HashMap<>();
        JSONArray keyValues = new JSONArray();
        data.put("keyValues", keyValues);
        data.put("message", "Values retrieved succesfully");

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/advice/prepare/41",
                String.class).contains(data.toString()));
    }
}
