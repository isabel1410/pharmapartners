package com.pharma.medicatiebewaking.model;

import com.pharma.medicatiebewaking.model.Person;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public @Data class PersonMedication {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int personMedicationId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="medicationDetailsId")
    private MedicationDetails medicationDetails;
    private float intakeFrequency;

    public PersonMedication(){

    }

    public PersonMedication(MedicationDetails medicationDetails, float intakeFrequency){
        this.medicationDetails = medicationDetails;
        this.intakeFrequency = intakeFrequency;
    }
}
