package com.pharma.medicatiebewaking.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Picture {
    @Id
    private int pictureId;
    private String url;
    private String description;

    public Picture() {

    }

}
