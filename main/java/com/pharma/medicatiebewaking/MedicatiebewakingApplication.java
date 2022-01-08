package com.pharma.medicatiebewaking;

import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.model.Person;
import com.pharma.medicatiebewaking.model.PersonMedication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.json.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class MedicatiebewakingApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MedicatiebewakingApplication.class, args);

        // Set data
        HashMap<String, Boolean> medication = new HashMap<String, Boolean>();

        medication.put("laxans", false);
        medication.put("loperamide", true);
        String caseName = "laxantia";

        // Load data
        String cases = readFileAsString("src/main/resources/data.json");
        HashMap user = new User(medication).getMeds();

        String result = checkCase(caseName, cases, user, "1");

        System.out.println(result);
    }

    public static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    public static String checkCase(String caseName, String cases, HashMap user, String caseNumber) throws Exception {
        JSONObject loadedCase = loadCase(caseName, cases);
        JSONObject currentCase = loadedCase.getJSONObject(caseNumber);

        if (!currentCase.getBoolean("last")) {
            var hasMed = getMedInQuestion(user, currentCase.getString("medication"));
            String nextCase;

            if (hasMed == null) {
                return "Onvoldoende medicatie gegevens. Neem contact op met uw huisarts";
            } else if (hasMed) {
                nextCase = currentCase.getString("true");
            } else {
                nextCase = currentCase.getString("false");
            }

            return checkCase(caseName, cases, user, nextCase);
        } else {
            return currentCase.getString("description");
        }

    }

    // Load case
    public static JSONObject loadCase(String caseName, String cases) throws Exception {
        return new JSONObject(cases).getJSONObject(caseName);
    }

    // Check if user have medication data.
    public static Boolean getMedInQuestion(HashMap meds, String medication) {
        if (meds.containsKey(medication)) {
            return (boolean) meds.get(medication);
        } else {
            return null;
        }
    }
}


class User {
    public HashMap<String, Boolean> medication;

    public User(HashMap<String, Boolean> medication) {
        this.medication = medication;
    }

    public HashMap<String, Boolean> getMeds() {
        return medication;
    }
}