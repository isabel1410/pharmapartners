package com.pharma.medicatiebewaking.model;

import lombok.Data;

public @Data class AdviceKeyValueInformation {
    private String name;
    private String measurement;
    private String description;

    public AdviceKeyValueInformation(){

    }

    public AdviceKeyValueInformation(String name, String measurement, String description){
        this.name = name;
        this.measurement = measurement;
        this.description = description;
    }

}

