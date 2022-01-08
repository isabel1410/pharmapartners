package com.pharma.medicatiebewaking.util;

import com.pharma.medicatiebewaking.model.Advice;
import com.pharma.medicatiebewaking.model.MedicationAdvice;
import com.pharma.medicatiebewaking.model.PersonMedication;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public boolean EmailValidator(String email) {
        // create the EmailValidator instance
        EmailValidator validator = EmailValidator.getInstance();

        // check for valid email addresses using isValid method
        return validator.isValid(email);
    }

    public boolean PasswordValidator(String password) {
        boolean result = false;

//        ^                 # start-of-string
//        (?=.*[0-9])       # a digit must occur at least once
//        (?=.*[a-z])       # a lower case letter must occur at least once
//        (?=.*[A-Z])       # an upper case letter must occur at least once
//        (?=.*[@#$%^!&+=])  # a special character must occur at least once
//        (?=\S+$)          # no whitespace allowed in the entire string
//        .{8,}             # anything, at least eight places though
//        $                 # end-of-string

        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^!&+=])(?=\\S+$).{8,}$";
        if (!password.matches(pattern)) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public boolean ValidateTopPressureAsHighest(String tPressure, String lPressure){
        int topPressure = Integer.parseInt(tPressure);
        int lowPressure = Integer.parseInt(lPressure);
        boolean isHighest = true;

        if (topPressure < lowPressure){
            isHighest = false;
        }
        return isHighest;
    }

    public boolean ValidateTopAndLowPressure(String tPressure, String lPressure){
        boolean success = false;
        String regex = "[+-]?[0-9]+";
        Pattern p = Pattern.compile(regex);
        Matcher m1 = p.matcher(tPressure);
        Matcher m2 = p.matcher(lPressure);


        if (m1.find() && m1.group().equals(tPressure) && m2.find() && m2.group().equals(lPressure)){

            success = true;
        }
        return success;
    }

    //Builder pattern bekijken voor generateAdviceHistory api bekijken om makkelijker te maken / minder code?



    public List<HashMap<String, Object>> generateAdviceHistoryApi(List<Advice> advices) {
        List<HashMap<String, Object>> adviceArray = new ArrayList<HashMap<String, Object>>();
        for (Advice advice : advices) {
            HashMap<String, Object> adviceData = new HashMap<>();
            adviceData.put("title", advice.getTitle());
            adviceData.put("adviceId", advice.getAdviceId());
            adviceData.put("status", advice.getStatus());
            adviceData.put("date", advice.getDate());
            adviceData.put("health", advice.getHealthValues());
            List<MedicationAdvice> medicationAdvices = advice.getMedicationAdviceList();
            List<HashMap<String, Object>> medicationArray = new ArrayList<HashMap<String, Object>>();
            for (MedicationAdvice medicationAdvice : medicationAdvices) {
                HashMap<String, Object> medicationAdviceData = new HashMap<>();
                PersonMedication personMedication = medicationAdvice.getPersonMedication();
                medicationAdviceData.put("medicationName", personMedication.getMedicationDetails().getMedication().getMedicationName());
                medicationAdviceData.put("dosisForm", personMedication.getMedicationDetails().getDosisForm().getDosisForm());
                medicationAdviceData.put("dosis", personMedication.getIntakeFrequency());
                medicationAdviceData.put("strength", personMedication.getMedicationDetails().getStrength());
                medicationAdviceData.put("measuringUnit", personMedication.getMedicationDetails().getMeasurementUnit().getMeasurementUnit());
                HashMap<String, Object> medicationAdviceHashMap = new HashMap<>();
                medicationAdviceHashMap.put("medicationAdviceId", medicationAdvice.getMedicationAdviceId());
                medicationAdviceHashMap.put("description", medicationAdvice.getDescription());
                medicationAdviceHashMap.put("status", medicationAdvice.getStatus());
                medicationAdviceData.put("medicationAdvice", medicationAdviceHashMap);
                medicationArray.add(medicationAdviceData);
            }
            adviceData.put("medications", medicationArray);
            adviceArray.add(adviceData);
        }
        return adviceArray;
    }
}
