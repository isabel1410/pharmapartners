package com.pharma.medicatiebewaking;

import com.pharma.medicatiebewaking.model.Medication;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MedicationTest {
    @Test
    public void MedicationIdTest() {
        Medication med = new Medication();
        med.setMedicationId(1);
        Assert.assertEquals(1, med.getMedicationId());
    }

    @Test
    public void MedicationNameTest() {
        Medication med = new Medication();
        med.setMedicationName("Paracetamol");
        Assert.assertEquals("Paracetamol", med.getMedicationName());
    }

    @Test
    public void MedicationConstructorTest() {
        Medication med = new Medication(2, "Ibuprofen");
        Assert.assertEquals(2, med.getMedicationId());
        Assert.assertEquals("Ibuprofen", med.getMedicationName());
    }
}
