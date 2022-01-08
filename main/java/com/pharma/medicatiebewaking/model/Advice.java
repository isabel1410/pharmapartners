package com.pharma.medicatiebewaking.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
public @Data class Advice {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int adviceId;
    private String title;
    private AdviceStatus status;
    @OneToMany(cascade = CascadeType.ALL)
    private List<MedicationAdvice> medicationAdviceList;
    private String healthValues;
    private LocalDateTime date;

    public Advice(){

    }

    public Advice(int adviceId, String title, AdviceStatus status, List<MedicationAdvice> medicationAdviceList){
        this.adviceId = adviceId;
        this.title = title;
        this.status = status;
        this.medicationAdviceList = medicationAdviceList;
    }
}
