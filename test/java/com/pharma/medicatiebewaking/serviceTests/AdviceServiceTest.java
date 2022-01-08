package com.pharma.medicatiebewaking.serviceTests;

import com.pharma.medicatiebewaking.Interface.AdviceRepository;
import com.pharma.medicatiebewaking.Interface.HealthRepository;
import com.pharma.medicatiebewaking.Interface.PersonMedicationRepository;
import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.model.*;
import com.pharma.medicatiebewaking.services.AdviceService;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AdviceServiceTest {
    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private HealthRepository healthRepository;

    private AdviceService adviceService;

    @Test
    public void getAdviceKeys_true() throws Exception {
        adviceService = new AdviceService();

        //Set expected data
        AdviceKeyValue expectedData = new AdviceKeyValue(
                2,
                "cotrimoxazol",
                "Clcr bekend?",
                "clcr",
                0.0f,
                0.0f,
                "boolean",
                "Default",
                3, 10,
                false,
                false,
                null,
                false,
                null);

        String currentCaseString = "{\"condition\":0,\"last\":false,\"keyValue\":\"clcr\",\"true\":3,\"false\":10,\"lastKey\":false,\"caseName\":\"cotrimoxazol\",\"description\":\"Clcr bekend?\",\"category\":\"Default\",\"operator\":\"boolean\"}";
        JSONObject currentCase = new JSONObject(currentCaseString);

        AdviceKeyValue result = adviceService.getAdviceKeys(currentCase, 2);

        assertThat(result).isEqualTo(expectedData);
    }

    @Test
    public void getAdviceKeyValue_true() throws Exception {
        adviceService = new AdviceService();

        //Set expected data
        AdviceKeyValueInformation adviceKeyValueInformation = new AdviceKeyValueInformation("Nierfunctie - CLcr", "ml/min", "CLcr staat voor creatine klaring. Dit gaat over het afbreken van afvalstoffen in je nieren");
        AdviceKeyValue adviceKeyValue = new AdviceKeyValue(
                2,
                "cotrimoxazol",
                "Clcr bekend?",
                "clcr",
                0.0f,
                0.0f,
                "boolean",
                "Default",
                3, 10,
                false,
                false,
                adviceKeyValueInformation,
                false,
                null);


        List<PersonMedication> personMedicationList = new ArrayList<>();

        DosisForm dosisForm = new DosisForm(1, "ml/min");
        Medication medication = new Medication(7, "cotrimoxazol", "");
        MedicationDetails medicationDetails = new MedicationDetails(medication, dosisForm, 20);

        PersonMedication personMedication = new PersonMedication(medicationDetails, 2);
        personMedicationList.add(personMedication);

        Health health = new Health(1, 82.1f, "137/100", 2, 323, 134);

        String date = "11/03/1980";
        Date dateOfBirth = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        Person person = new Person(41, personMedicationList, health, dateOfBirth);

        float expected = 323;

        float result = adviceService.getAdviceKeyValue(adviceKeyValue, person);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void checkCondition_true() throws Exception {
        adviceService = new AdviceService();

        //Set expected data
        AdviceKeyValueInformation adviceKeyValueInformation = new AdviceKeyValueInformation("Nierfunctie - CLcr", "ml/min", "CLcr staat voor creatine klaring. Dit gaat over het afbreken van afvalstoffen in je nieren");
        AdviceKeyValue adviceKeyValue = new AdviceKeyValue(
                3,
                "cotrimoxazol",
                "Clcr > 13 maanden?",
                "clcrMonth",
                13f,
                13f,
                ">",
                "Included",
                7, 4,
                false,
                false,
                adviceKeyValueInformation,
                false,
                null);

        int expected = 4;

        int result = adviceService.checkCondition(adviceKeyValue);

        assertThat(result).isEqualTo(expected);
    }
}
