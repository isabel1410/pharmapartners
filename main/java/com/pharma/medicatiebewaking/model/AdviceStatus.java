package com.pharma.medicatiebewaking.model;

public enum AdviceStatus {
    GREEN("GREEN"),
    RED("RED"),
    GREY("GREY");

    private final String value;

    private AdviceStatus(final String value){
        this.value = value;
    }

}


