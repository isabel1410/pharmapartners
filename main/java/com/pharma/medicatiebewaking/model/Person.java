package com.pharma.medicatiebewaking.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.pharma.medicatiebewaking.model.Health;
import lombok.Data;


@Entity
public @Data class Person {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int personId;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PersonMedication> personMedication;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Advice> adviceList;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="healthId")
    private Health health;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;

    public Person(){

    }

    public Person(int personId, List<PersonMedication> personMedication, List<Advice> adviceList, Health health, String firstname, String lastname, Date dateOfBirth){
        this.personId = personId;
        this.personMedication = personMedication;
        this.adviceList = adviceList;
        this.health = health;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
    }

    public Person(int personId, List<PersonMedication> personMedication, Health health, Date dateOfBirth){
        this.personId = personId;
        this.personMedication = personMedication;
        this.health = health;
        this.dateOfBirth = dateOfBirth;
    }

    public Person(String firstname, String lastname, Date dateOfBirth){
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.health = new Health();
    }


}

