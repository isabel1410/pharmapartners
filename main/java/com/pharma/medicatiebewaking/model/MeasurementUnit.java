package com.pharma.medicatiebewaking.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
public @Data
class MeasurementUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int measurementUnitId;
    private String measurementUnit;

    public MeasurementUnit() {

    }

    public MeasurementUnit(int measurementUnitId, String measurementUnit) {
        this.measurementUnitId = measurementUnitId;
        this.measurementUnit = measurementUnit;
    }
}