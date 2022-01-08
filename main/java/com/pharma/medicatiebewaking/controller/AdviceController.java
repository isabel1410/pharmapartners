package com.pharma.medicatiebewaking.controller;

import com.pharma.medicatiebewaking.Interface.AdviceRepository;
import com.pharma.medicatiebewaking.Interface.PersonMedicationRepository;
import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.model.Advice;
import com.pharma.medicatiebewaking.model.AdviceKeyValue;
import com.pharma.medicatiebewaking.model.AdviceKeyValueInformation;
import com.pharma.medicatiebewaking.model.Person;
import com.pharma.medicatiebewaking.services.AdviceService;
import com.pharma.medicatiebewaking.util.Tool;
import com.pharma.medicatiebewaking.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/advice")
@CrossOrigin(origins = "http://localhost:3000")
public class AdviceController extends Tool {
    JSONObject jsonKeyValueInformation;

    /**
     * Repositories
     **/
    @Autowired
    private AdviceRepository adviceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMedicationRepository personMedicationRepository;

    private final AdviceService service;

    public AdviceController(AdviceService service) throws Exception {
        this.service = service;
        jsonKeyValueInformation = jsonStringToJSONObject("src/main/resources/keyValueInformation.json");
    }

    @GetMapping("/prepare/{personId}")
    public @ResponseBody
    String PrepareAdvice(@PathVariable int personId, HttpServletResponse response) {
        HashMap<String, Object> data = new HashMap<>();

        //Get person and medication
        Person person = personRepository.findByPersonId(personId);

        List<AdviceKeyValue> adviceKeyValues = new ArrayList<>();

        //Lijst met alle adviceKeyValues vanuit de kennisregels ophalen
        service.getAdviceKeyValues(person, adviceKeyValues);

        //Kijk door kennisregels welke values er nodig zijn
        List<AdviceKeyValue> personKeyValues = service.filterPersonKeyValues_prepare(adviceKeyValues);

        ArrayList<HashMap<String, Object>> keyValuesAdvice = new ArrayList<>();

        for (AdviceKeyValue adviceKeyValue : personKeyValues) {
            HashMap<String, Object> keyValueAdvice = new HashMap<>();

            float value = service.getAdviceKeyValue(adviceKeyValue, person);
            //String value = String.valueOf(floatValue);
            AdviceKeyValueInformation information = service.getAdviceKeyValueInformation(adviceKeyValue.getKeyValue());

            keyValueAdvice.put("keyValue", adviceKeyValue.getKeyValue());
            keyValueAdvice.put("category", adviceKeyValue.getCategory());
            keyValueAdvice.put("value", value);
            keyValueAdvice.put("information", information);

            keyValuesAdvice.add(keyValueAdvice);
        }

        data.put("keyValues", keyValuesAdvice);
        data.put("message", "Values retrieved successfully");

        response.setStatus(HttpServletResponse.SC_OK);

        return new JSONObject(data).toString();
    }

    @PostMapping("/generate/{personId}")
    public @ResponseBody
    String GenerateAdvice(@PathVariable int personId, @RequestBody String values, HttpServletResponse response) throws Exception {
        HashMap<String, Object> data = new HashMap<>();

        try{
            JSONArray jsonValues = new JSONObject(values).getJSONArray("keyValues");
            System.out.println("JSONVALUES" + jsonValues);
            service.saveValuesToPerson(jsonValues, personId);

            Person person = personRepository.findByPersonId(personId);

            List<AdviceKeyValue> adviceKeyValues = new ArrayList<>();

            service.getAdviceKeyValues(person, adviceKeyValues);
            service.setAdviceKeyValues(person, adviceKeyValues);

            System.out.println("advice key values = " + adviceKeyValues);

            Advice advice = service.getAdvice(adviceKeyValues);

            //Get advices, add new one to it and save person to DB
            List<Advice> personAdvices = person.getAdviceList();
            personAdvices.add(advice);
            personRepository.save(person);

            response.setStatus(HttpServletResponse.SC_OK);
            data.put("advice", advice);
            data.put("message", "advice generated successfully");
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            data.put("message", "advice generated unsuccessfully");
        }

        return new JSONObject(data).toString();
    }

    @GetMapping("/{personId}")
    public @ResponseBody String getAdvice(@PathVariable int personId, HttpServletResponse response) {
        HashMap<String, Object> data = new HashMap<>();

        service.getAdvice(personId, data);

        return new JSONObject(data).toString();
    }

    @GetMapping("/history/{personId}")
    public @ResponseBody String History(@PathVariable int personId, HttpServletResponse response) {
        HashMap<String, Object> data = new HashMap<>();
        Person person = personRepository.findByPersonId(personId);
        if (person == null) {
            data.put("message", "No data");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new JSONObject(data).toString();
        }
        List<Advice> advices = person.getAdviceList();
        Util util = new Util();
        data.put("advices", util.generateAdviceHistoryApi(advices));
        data.put("message", "Data opgehaald");
        response.setStatus(HttpServletResponse.SC_OK);
        return new JSONObject(data).toString();
    }

    @GetMapping("/delete")
    @ResponseBody
    public void deleteAll(){
        personMedicationRepository.deleteAll();
        adviceRepository.deleteAll();
    }
}