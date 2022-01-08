package com.pharma.medicatiebewaking.model;

import com.sun.istack.Nullable;
import jdk.jshell.execution.FailOverExecutionControlProvider;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;

@Entity
public @Data class Health {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int healthId;
    private float weight;
    private String bloodPressure;
    private int height;
    private float clcr;
    private float clcrMonth;

    public Health() {
        this.weight = 0f;
        this.bloodPressure = "0/0";
        this.height = 0;
        this.clcr = 0f;
        this.clcrMonth = 0f;
    }

    public Health(float weight, String bloodPressure, int height)
    {
        this.weight = weight;
        this.bloodPressure = bloodPressure;
        this.height = height;
    }

    public Health(int healthId, float weigth, String bloodPressure, int height, float clcr, float clcrMonth){
        this.healthId = healthId;
        this.weight = weigth;
        this.bloodPressure = bloodPressure;
        this.height = height;
        this.clcr = clcr;
        this.clcrMonth = clcrMonth;
    }

    public JSONObject toJSON() {

        JSONObject jo = new JSONObject();
        jo.put("weight", weight);
        jo.put("bloodPressure", bloodPressure);
        jo.put("height", height);
        jo.put("clcr", clcr);
        jo.put("clcrMonth", clcrMonth);

        return jo;
    }
}
