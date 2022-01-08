package com.pharma.medicatiebewaking.controller;
import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.model.Person;
import com.pharma.medicatiebewaking.util.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.pharma.medicatiebewaking.Interface.HealthRepository;
import com.pharma.medicatiebewaking.model.Health;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.math.BigDecimal;
import java.math.RoundingMode;



@Controller
@RequestMapping("/health")
@CrossOrigin(origins = "http://localhost:3000")

public class HealthController extends Util {
    @Autowired
    private HealthRepository healthRepository;
    @Autowired
    private PersonRepository personRepository;


    public boolean CheckUserInputIsFloat(Health health)
    {
        boolean isFloat = true;
        String weight = Float.toString(health.getWeight());
        String height = Float.toString(health.getHeight());
        try{
            Float.parseFloat(weight);
            Float.parseFloat(height);
        }catch(NumberFormatException e){
            isFloat = false;
        }
        return isFloat;
    }

//    public String ConvertBloodPressure(String tPressure, String lPressure){
//        String bloodPressure = tPressure + "/" + lPressure;
//        return bloodPressure;
//    }

//    public Health Set2Decimal(Health health, int places){
//        BigDecimal bigDecimalWeight = new BigDecimal(Float.toString(health.getWeight()));
//        bigDecimalWeight = bigDecimalWeight.setScale(places, RoundingMode.HALF_UP);
//        health.setWeight(bigDecimalWeight.floatValue());
//        System.out.println(health.getWeight());
//        return health;
//    }

    @GetMapping("/{personId}")
    public @ResponseBody Health getHealthById(@PathVariable int personId) {
        return personRepository.findByPersonId(personId).getHealth();
    }

    @PutMapping("/save")
    public @ResponseBody String saveHealth(@RequestParam String columnName, String value, int id) {
        HashMap<String, Object> data = new HashMap<>();

        if (value.isEmpty()) {
            data.put("message", "Alle velden dienen ingevuld te zijn");
            return new org.json.JSONObject(data).toString();
        } else if (personRepository.findByPersonId(id) == null) {
            data.put("message", "Gebruiker bestaat niet");
            return new org.json.JSONObject(data).toString();
        }

        Health health = personRepository.findByPersonId(id).getHealth();

        switch (columnName) {
            case "height":
                int height;

                try {
                    height = Integer.parseInt(value);
                } catch (Exception e) {
                    data.put("message", "De lengte mag geen komma bevatten");
                    return new org.json.JSONObject(data).toString();
                }

                health.setHeight(height);
                columnName = "lengte";
                break;
            case "weight":
                float weight = Float.parseFloat(value);
                health.setWeight(weight);
                columnName = "gewicht";
                break;
            case "bloodPressure":
                String topPressure;
                String lowPressure;

                try {
                    topPressure = value.split("/")[0];
                    lowPressure = value.split("/")[1];
                } catch (Exception e) {
                    data.put("message", "Beide bloeddrukwaarden moeten ingevuld zijn");
                    return new org.json.JSONObject(data).toString();
                }

                if (!ValidateTopAndLowPressure(topPressure, lowPressure)) {
                    data.put("message", "De broeddruk mag alleen cijfers bevatten");
                    return new org.json.JSONObject(data).toString();
                } else if (!ValidateTopPressureAsHighest(topPressure, lowPressure)) {
                    data.put("message", "De bovendruk moet hoger zijn dan de onderdruk");
                    return new org.json.JSONObject(data).toString();
                }

                health.setBloodPressure(value);
                columnName = "bloeddruk";
                break;
            case "clcr":
                float clcr = Float.parseFloat(value);
                health.setClcr(clcr);
                columnName = "creatineklaring";
                break;
            case "clcrMonth":
                float clcrMonth = Float.parseFloat(value);
                health.setClcrMonth(clcrMonth);
                columnName = "laatste meting creatineklaring";
                break;
            default:
                data.put("message", "Unknown column name");
                return new org.json.JSONObject(data).toString();
        }
        healthRepository.save(health);
        data.put("message", "Uw "+ columnName + " is opgeslagen");
        return new JSONObject(data).toString();
    }

//    @GetMapping("/healthById")
//    public @ResponseBody Health HealthById(@RequestParam int id){
//        Person person = personRepository.findByPersonId(id);
//        return person.getHealth();
//    }

//    @PutMapping("/change")
//    public @ResponseBody String ChangeHealth(@RequestParam String height, String topPressure, String lowPressure, String weight, int id){
//        HashMap<String, Object> data = new HashMap<>();
//        if(height.isEmpty() || weight.isEmpty() || topPressure.isEmpty() || lowPressure.isEmpty()){
//            data.put("message", "Er zijn velden leeg gelaten.");
//            return new JSONObject(data).toString();
//        }
//
//        float Height = Float.parseFloat(height);
//        float Weight = Float.parseFloat(weight);
//
//        if(ValidateTopAndLowPressure(topPressure, lowPressure) && ValidateTopPressureAsHighest(topPressure, lowPressure)){
//            String bloodPressure = ConvertBloodPressure(topPressure, lowPressure);
//
//            Health health = new Health();
//            health.setHeight(Height);
//            health.setWeight(Weight);
//            health.setBloodPressure(bloodPressure);
//            health.setHealthId(id);
//
//            health = Set2Decimal(health, 2);
//
//            Health healthFoundById = healthRepository.findById(health.getHealthId()).get();
//            healthFoundById.setHeight(health.getHeight());
//            healthFoundById.setBloodPressure(health.getBloodPressure());
//            healthFoundById.setWeight(health.getWeight());
//
//            healthRepository.save(healthFoundById);
//            data.put("message", "De data is veranderd.");
//            return new JSONObject(data).toString();
//        }else{
//            data.put("message", "De bloeddruk klopt niet");
//            return new JSONObject(data).toString();
//        }
//    }

//    @PostMapping("/save")
//    public @ResponseBody String SaveHealth(@RequestParam String height, String topPressure, String lowPressure, String weight, int id) throws Exception {
//        HashMap<String, Object> data = new HashMap<>();
//        if(height.isEmpty() || weight.isEmpty() || topPressure.isEmpty() || lowPressure.isEmpty()){
//            data.put("message", "Er zijn velden leeg gelaten.");
//            return new JSONObject(data).toString();
//        }
//        float Height = Float.parseFloat(height);
//        float Weight = Float.parseFloat(weight);
//
//        if (ValidateTopAndLowPressure(topPressure, lowPressure) && ValidateTopPressureAsHighest(topPressure, lowPressure)){
//                String bloodPressure = ConvertBloodPressure(topPressure, lowPressure);
//                Health health = new Health(Height, bloodPressure, Weight);
//
//                health = Set2Decimal(health, 2);
//                healthRepository.save(health);
//                System.out.print(health);
//
////                Person person = personRepository.findByPersonId(id);
////                System.out.println(person);
////                person.setHealth(health);
////                personRepository.save(person);
//
//                data.put("message", "Informatie is opgeslagen.");
//                return new JSONObject(data).toString();
//        }else{
//            data.put("message", "De bloeddruk klopt niet");
//            return new JSONObject(data).toString();
//        }
//
//    }

}
