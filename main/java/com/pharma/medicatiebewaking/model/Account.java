package com.pharma.medicatiebewaking.model;

import lombok.Data;

import javax.persistence.*;

@Entity
public @Data
class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int accountId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personId")
    private Person person;
    private String email;
    private String password;
    private String salt;

    public Account() {
    }

    public Account(String email) {
        this.email = email;
    }

    public Account(String email, String password, String salt, Person person) {
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.person = person;
    }

}