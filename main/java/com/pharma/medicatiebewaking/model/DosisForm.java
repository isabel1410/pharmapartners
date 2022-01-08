package com.pharma.medicatiebewaking.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public @Data
class DosisForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int dosisFormId;
    private String dosisForm;

    public DosisForm() {

    }

    public DosisForm(int dosisFormId, String dosisForm) {
        this.dosisFormId = dosisFormId;
        this.dosisForm = dosisForm;
    }
}
