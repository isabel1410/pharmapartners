package com.pharma.medicatiebewaking.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
public @Data
class Medication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int medicationId;
    private String medicationName;
    private String category;

    public Medication() {

    }

    public Medication(int medicationId, String medicationName, String category) {
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.category = category;
    }
    public Medication(int medicationId, String medicationName){
        this.medicationId = medicationId;
        this.medicationName = medicationName;
    }
}
