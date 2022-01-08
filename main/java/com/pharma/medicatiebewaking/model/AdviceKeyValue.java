package com.pharma.medicatiebewaking.model;

import lombok.Data;
import org.springframework.data.jpa.repository.Query;

public @Data class AdviceKeyValue {
    private int id;
    private String caseName;
    private String description;
    private String keyValue;
    private float value;
    private float condition;
    private String operator;
    private String category;
    private int isTrue;
    private int isFalse;
    private boolean last;
    private boolean lastKey;
    AdviceKeyValueInformation adviceKeyValueInformation;
    private boolean visible;
    private PersonMedication personMedication;

    public AdviceKeyValue(){

    }

    public AdviceKeyValue(String keyValue, String description, String category, PersonMedication personMedication){
        this.keyValue = keyValue;
        this.description = description;
        this.category = category;
        this.personMedication = personMedication;
    }

    public AdviceKeyValue(int id, String caseName, String description, String keyValue, float value, float condition, String operator, String category, int isTrue, int isFalse, boolean last, boolean lastKey, AdviceKeyValueInformation adviceKeyValueInformation, boolean visible, PersonMedication personMedication){
        this.id = id;
        this.caseName = caseName;
        this.description = description;
        this.keyValue = keyValue;
        this.value = value;
        this.condition = condition;
        this.operator = operator;
        this.category = category;
        this.isTrue = isTrue;
        this.isFalse = isFalse;
        this.last = last;
        this.lastKey = lastKey;
        this.adviceKeyValueInformation = adviceKeyValueInformation;
        this.visible = visible;
        this.personMedication = personMedication;
    }

}