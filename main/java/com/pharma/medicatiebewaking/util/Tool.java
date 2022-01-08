package com.pharma.medicatiebewaking.util;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Tool {

    public JSONObject jsonStringToJSONObject(String filePath) throws Exception {
        String cases = readFileAsString(filePath);
        JSONObject jsonObject = new JSONObject(cases);

        return jsonObject;
    }

    public static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

}
