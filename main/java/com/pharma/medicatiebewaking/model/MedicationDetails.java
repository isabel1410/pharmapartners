package com.pharma.medicatiebewaking.model;

import lombok.Data;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;

@Entity
public @Data
class MedicationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int medicationDetailsId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicationId")
    private Medication medication;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dosisFormId")
    private DosisForm dosisForm;
    private float strength;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "measurementUnitId")
    private MeasurementUnit measurementUnit;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="pictureId")
    private Picture picture;

    public MedicationDetails() {

    }

    public MedicationDetails(Medication medication, DosisForm dosisForm, float strength) {
        this.medication = medication;
        this.dosisForm = dosisForm;
        this.strength = strength;
    }
}