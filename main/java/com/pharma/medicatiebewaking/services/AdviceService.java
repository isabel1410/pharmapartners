package com.pharma.medicatiebewaking.services;

import com.pharma.medicatiebewaking.Interface.HealthRepository;
import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.controller.HealthController;
import com.pharma.medicatiebewaking.model.*;
import com.pharma.medicatiebewaking.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdviceService extends Util {
    /**
     * Algorithm values
     **/
    //Prepare Advice
    JSONObject jsonCases;
    JSONObject jsonKeyValueInformation;
    JSONObject currentCase;
    JSONObject jsonHealth;

    /**
     * MedicationAdvice values
     **/
    String description = "";
    List<MedicationAdvice> medicationAdviceList;

    /**
     * Advice values
     **/

    @Autowired
    PersonRepository personRepository;

    @Autowired
    HealthRepository healthRepository;

    public AdviceService() throws Exception{
        //Get kennisregels
        jsonCases = jsonStringToJSONObject("src/main/resources/kennisregels.json");
        jsonKeyValueInformation = jsonStringToJSONObject("src/main/resources/keyValueInformation.json");
    }

    public JSONObject jsonStringToJSONObject(String filePath) throws Exception {
        String cases = readFileAsString(filePath);
        JSONObject jsonObject = new JSONObject(cases);

        return jsonObject;
    }

    public static String readFileAsString(String file) throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    public AdviceKeyValue getAdviceKeys(JSONObject currentCase, int index) {
        //Get adviceKeyValues
        AdviceKeyValue adviceKeyValue = new AdviceKeyValue();

        adviceKeyValue.setId(index);
        adviceKeyValue.setCaseName(currentCase.getString("caseName"));
        adviceKeyValue.setKeyValue(currentCase.getString("keyValue"));
        adviceKeyValue.setDescription(currentCase.getString("description"));
        adviceKeyValue.setCondition(currentCase.getFloat("condition"));
        adviceKeyValue.setOperator(currentCase.getString("operator"));
        adviceKeyValue.setCategory(currentCase.getString("category"));
        adviceKeyValue.setIsTrue(currentCase.getInt("true"));
        adviceKeyValue.setIsFalse(currentCase.getInt("false"));
        adviceKeyValue.setLast(currentCase.getBoolean("last"));
        adviceKeyValue.setLastKey(currentCase.getBoolean("lastKey"));

        System.out.println(adviceKeyValue);

        return adviceKeyValue;
    }

    public float getAdviceKeyValue(AdviceKeyValue adviceKeyValue, Person person) {
        String keyValue = adviceKeyValue.getKeyValue();
        String category = adviceKeyValue.getCategory();

        jsonHealth = new JSONObject(person.getHealth());

        System.out.println(jsonHealth);

        float value = 0;

        switch(category){
            case "Default":
                System.out.println(category);
                value = jsonHealth.getFloat(keyValue);
                break;
            case "Included":
                System.out.println(keyValue);
                value = jsonHealth.getFloat(keyValue);
                break;
            case "Medication":
                value = checkMedication(person.getPersonMedication(), keyValue);
                break;
            case "MedicationCategory":
                value = checkMedicationCategory(person.getPersonMedication(), keyValue);
                break;
            case "Account":
                value = calculateAge(person);
                break;
            case "Excluded":
                value = 0;
            default:
                break;
        }

        return value;
    }

    public int checkCondition(AdviceKeyValue adviceKeyValue) {
        int result = 0;

        float value = adviceKeyValue.getValue();
        float condition = adviceKeyValue.getCondition();

        if(adviceKeyValue.getOperator().equals("Known")){

        }else{
            String str = adviceKeyValue.getOperator().replace(" ", "");
            switch (str) {
                case ">":
                    if (value > condition) {
                        result = adviceKeyValue.getIsTrue();
                    } else {
                        result = adviceKeyValue.getIsFalse();
                    }
                    break;
                case ">=":
                    if (value >= condition) {
                        result = adviceKeyValue.getIsTrue();
                    } else {
                        result = adviceKeyValue.getIsFalse();
                    }
                    break;
                case "boolean":
                    if(adviceKeyValue.getValue() >= 1){
                        result = adviceKeyValue.getIsTrue();
                    }else{
                        result = adviceKeyValue.getIsFalse();
                    }
                    break;
                default:

            }
        }

        return result;
    }

    public float checkMedication(List<PersonMedication> personMedicationList, String keyValue){
        float check = 0;

        for(PersonMedication personMedication : personMedicationList){
            if (personMedication.getMedicationDetails().getMedication().getMedicationName().equals(keyValue)) {
                check = 1;
                break;
            }
        }

        return check;
    }

    public float checkMedicationCategory(List<PersonMedication> personMedicationList, String category){
        float check = 0;

        System.out.println();

        for(PersonMedication personMedication : personMedicationList){
            try{
                System.out.println("MedicationCategory = "  + personMedication.getMedicationDetails().getMedication().getCategory());
                System.out.println("Category = " + category);
                if (personMedication.getMedicationDetails().getMedication().getCategory().equals(category)) {
                    check = 1;
                }
            }catch(Exception ex){
                System.out.println("Medication with category not found " + category);
            }
        }

        return check;
    }

    public AdviceKeyValueInformation getAdviceKeyValueInformation(String keyValue){
        AdviceKeyValueInformation adviceKeyValueInformation = new AdviceKeyValueInformation();

        System.out.println(keyValue);
        JSONObject currentInformation = jsonKeyValueInformation.getJSONObject(keyValue);
        adviceKeyValueInformation.setName(currentInformation.getString("name"));
        adviceKeyValueInformation.setMeasurement(currentInformation.getString("measurement"));
        adviceKeyValueInformation.setDescription(currentInformation.getString("description"));

        return adviceKeyValueInformation;
    }

    public void saveValuesToPerson(JSONArray jsonKeyValues, int personId) {
        for (int i = 0; i < jsonKeyValues.length(); i++) {

            JSONObject jsonKeyValue = jsonKeyValues.getJSONObject(i);
            String keyValue = jsonKeyValue.getString("keyValue");
            String value = Float.toString(jsonKeyValue.getFloat("value"));

            System.out.println("KEYVALUE = " + keyValue);
            System.out.println("VALUE = " + value);

            String message = saveHealth(keyValue, value, personId);
        }
    }

    public String saveHealth(String columnName, String value, int id){
        HashMap<String, Object> data = new HashMap<>();

        if (value.isEmpty()) {
            data.put("message", "Alle velden dienen ingevuld te zijn");
            return new org.json.JSONObject(data).toString();
        } else if (personRepository.findByPersonId(id) == null) {
            data.put("message", "Gebruiker bestaat niet");
            return new org.json.JSONObject(data).toString();
        }

        Health health = personRepository.findByPersonId(id).getHealth();

        System.out.println("----------------------------" + columnName);

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

    public JSONObject getCase(int caseId, PersonMedication personMedication) {
        JSONObject jsonCase;

        //Get name and category
        String medicationName = personMedication.getMedicationDetails().getMedication().getMedicationName();
        String medicationCategory = personMedication.getMedicationDetails().getMedication().getCategory();

        //set jsonCase based on category or name
        if(medicationCategory != null){
            jsonCase = jsonCases.getJSONObject(medicationCategory);
        }else{
            jsonCase = jsonCases.getJSONObject(medicationName);
        }
        currentCase = jsonCase.getJSONObject("cases").getJSONObject(String.valueOf(caseId));

        return currentCase;
    }

    public AdviceKeyValue getNextCase(int nextCaseId, List<AdviceKeyValue> currentCaseValues){
        AdviceKeyValue result = new AdviceKeyValue();

        for(AdviceKeyValue adviceKeyValue : currentCaseValues){
            if(adviceKeyValue.getId() == nextCaseId){
                result = adviceKeyValue;
            }
        }

        return result;
    }

    public List<AdviceKeyValue> filterPersonKeyValues_prepare(List<AdviceKeyValue> adviceKeyValues) {
        return adviceKeyValues
                .stream()
                .filter(x -> x.getCategory().equals("Default") || x.getCategory().equals("Included"))
                .filter(distinctByKey(AdviceKeyValue::getKeyValue))
                .collect(Collectors.toList());
    }

    public List<AdviceKeyValue> filterCaseName(List<AdviceKeyValue> adviceKeyValues, String caseName) {
        return adviceKeyValues
                .stream()
                .filter(x -> x.getCaseName().equals(caseName))
                .collect(Collectors.toList());
    }

    public List<String> getCaseNames(List<AdviceKeyValue> adviceKeyValues){
        return adviceKeyValues
                .stream()
                .map(AdviceKeyValue::getCaseName).distinct()
                .collect(Collectors.toList());
    }

    public List<AdviceKeyValue> filterNeededAdviceValues(List<AdviceKeyValue> adviceKeyValues) {
        return adviceKeyValues
                .stream()
                .filter(x -> !x.getCategory().equals("Excluded"))
                .collect(Collectors.toList());
    }

    public List<AdviceKeyValue> filterEmptyAdviceValues(List<AdviceKeyValue> adviceKeyValues) {
        return adviceKeyValues
                .stream()
                .filter(x -> x.getCategory().equals("Excluded"))
                .collect(Collectors.toList());
    }

    public float calculateAge(Person person) {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate date = person.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            System.out.println("Leeftijd = " + Period.between(date, currentDate).getYears());
            return Period.between(date, currentDate).getYears();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public boolean checkMedicationInUsage() {
        if (currentCase.getInt("condition") == 0 && currentCase.getString("operator").equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public void setAdviceKeyValues(Person person, List<AdviceKeyValue> adviceKeyValues) {
        for (AdviceKeyValue adviceKeyValue : adviceKeyValues) {
            float value = getAdviceKeyValue(adviceKeyValue, person);
            adviceKeyValue.setValue(value);
            System.out.println(adviceKeyValue.getKeyValue() + " : " + adviceKeyValue.getValue());
        }
    }

    public void getAdviceKeyValues(Person person, List<AdviceKeyValue> adviceKeyValues) {
        for (PersonMedication medication : person.getPersonMedication()) {
            //Lijst met alle adviceKeyValues vanuit de kennisregels ophalen
            try {
                //Loop through each id of current case
                boolean lastKey = false;
                int index = 2;
                while (!lastKey) {

                    JSONObject currentCase = getCase(index, medication);

                    //Kijk naar adviceKeyValues in die currentCase en sla op in list
                    AdviceKeyValue adviceKeyValue = getAdviceKeys(currentCase, index);
                    adviceKeyValue.setPersonMedication(medication);
                    adviceKeyValues.add(adviceKeyValue);

                    if (currentCase.getBoolean("lastKey")) {
                        break;
                    }

                    index += 1;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");

                AdviceKeyValue adviceKeyValue = new AdviceKeyValue(medication.getMedicationDetails().getMedication().getMedicationName(), "Geen signaal" , "Excluded" ,medication);
                adviceKeyValues.add(adviceKeyValue);
            }
        }
    }

    public Advice getAdvice(List<AdviceKeyValue> adviceKeyValues) {
        //Lijst met string met de caseNames
        List<String> caseNames = getCaseNames(adviceKeyValues);
        System.out.println(caseNames);

        List<MedicationAdvice> medicationAdvices = new ArrayList<>();

        AdviceKeyValue currentCase;

        List<AdviceKeyValue> emptyAdviceKeyValues = filterEmptyAdviceValues(adviceKeyValues);
        List<AdviceKeyValue> neededAdviceKeyValues = filterNeededAdviceValues(adviceKeyValues);


        System.out.println("Empty adviceKeyValues = " + emptyAdviceKeyValues);
        //Empty keyValues
        for(AdviceKeyValue adviceKeyValue : emptyAdviceKeyValues){
            System.out.println(adviceKeyValue.getKeyValue());
            MedicationAdvice medicationAdvice = setMedicationAdvice(adviceKeyValue);
            System.out.println("added");
            medicationAdvices.add(medicationAdvice);
            System.out.println(1);
        }

        System.out.println("medicationadvies = " + medicationAdvices);

        //neededKeyValues
        for (String currentCaseName : caseNames) {
            if(currentCaseName != null){
                List<AdviceKeyValue> currentCaseValues = filterCaseName(neededAdviceKeyValues, currentCaseName);

                currentCase = currentCaseValues.get(0);
                int nextCaseId = 0;

                while (!currentCase.isLastKey()) {

                    System.out.println(currentCase);

                    if (currentCase.isLast()) {
                        break;
                    }

                    nextCaseId = checkCondition(currentCase);
                    System.out.println(nextCaseId);

                    currentCase = getNextCase(nextCaseId, currentCaseValues);
                }

                MedicationAdvice medicationAdvice = setMedicationAdvice(currentCase);
                medicationAdvices.add(medicationAdvice);

                System.out.println("Dit is het einde: " + currentCase.getDescription());
            }
        }

        System.out.println(2);

        return setAdviceStatus(medicationAdvices, adviceKeyValues);
    }

    public MedicationAdvice setMedicationAdvice(AdviceKeyValue currentCase){
        MedicationAdvice medicationAdvice = new MedicationAdvice();

        medicationAdvice.setPersonMedication(currentCase.getPersonMedication());
        medicationAdvice.setDescription(currentCase.getDescription());
        medicationAdvice.setStatus(getMedicationStatus(currentCase));

        return medicationAdvice;
    }

    public AdviceStatus getMedicationStatus(AdviceKeyValue currentCase){
        AdviceStatus adviceStatus;

        if (currentCase.getDescription().equals("Geen signaal")) {
            adviceStatus = AdviceStatus.GREEN;
        }else{
            adviceStatus = AdviceStatus.RED;
        }

        return adviceStatus;
    }

    public Advice setAdviceStatus(List<MedicationAdvice> medicationAdvices, List<AdviceKeyValue> adviceKeyValues) {
        System.out.println("MedicationAdvices: " + medicationAdvices);
        System.out.println("Advicekeyvalues: " + adviceKeyValues);

        Advice advice =  new Advice();

        AdviceStatus adviceStatus = AdviceStatus.GREEN;

        System.out.println("MEDICATION ADVICES = " + medicationAdvices);
        System.out.println("ADVICEKEYVALUES = " + adviceKeyValues);

        for (MedicationAdvice medicationAdvice : medicationAdvices) {

            System.out.println(medicationAdvice.getStatus());

            if (adviceStatus == AdviceStatus.RED) {
                break;
            }
            if (medicationAdvice.getStatus() == AdviceStatus.RED) {
                advice.setTitle("Neem contact op met uw zorgverlener");
                adviceStatus = AdviceStatus.RED;
            } else {
                advice.setTitle("Het gebruik van uw medicatie is in orde. U hoeft geen actie te ondernemen");
                adviceStatus = AdviceStatus.GREEN;
            }
        }

        advice.setDate(LocalDateTime.now());

        advice.setStatus(adviceStatus);
        advice.setMedicationAdviceList(medicationAdvices);

        advice.setHealthValues(getHealthValues(adviceKeyValues));

        return advice;
    }

    public String getHealthValues(List<AdviceKeyValue> adviceKeyValues){
        HashMap<String, Object> healthValues = new HashMap<>();

        try{
            List<AdviceKeyValue> adviceKeyValueList = adviceKeyValues
                    .stream()
                    .filter(x -> !x.getKeyValue().equals(""))
                    .filter(distinctByKey(AdviceKeyValue::getKeyValue))
                    .collect(Collectors.toList());

            //HashMap<String, Object> inside = new HashMap<>();
            HashMap<String, Object> inside = new HashMap<>();

            for(AdviceKeyValue adviceKeyValue : adviceKeyValueList){
                JSONObject insideSecond = new JSONObject();

                AdviceKeyValueInformation adviceKeyValueInformation = getAdviceKeyValueInformation(adviceKeyValue.getKeyValue());
                JSONObject infomationJson = new JSONObject(adviceKeyValueInformation);

                insideSecond.put("information", infomationJson);
                insideSecond.put("value", adviceKeyValue.getValue());

                inside.put(adviceKeyValue.getKeyValue(), insideSecond);
            }

            healthValues.put("healthValues", inside);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        return new JSONObject(healthValues).toString();
    }

    public void getAdvice(int personId, HashMap<String, Object> data) {
        try{
            List<Advice> adviceList = personRepository.findByPersonId(personId).getAdviceList();
            int size = adviceList.size();
            data.put("Advice", adviceList.get(size - 1));
            data.put("Message", "Advice retrieved successfully");
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            data.put("Message", "Advice failed to retrieve with error message: " + ex.getMessage());
        }
    }
}
