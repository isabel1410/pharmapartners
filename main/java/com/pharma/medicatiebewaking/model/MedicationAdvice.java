package com.pharma.medicatiebewaking.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
public @Data class MedicationAdvice {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int medicationAdviceId;
    @OneToOne
    private PersonMedication personMedication;
    private String description;
    private AdviceStatus status;

    public MedicationAdvice(){

    }

    public MedicationAdvice(PersonMedication personMedication, String description, AdviceStatus status){
        this.personMedication = personMedication;
        this.description = description;
        this.status = status;
    }
}
