package com.pharma.medicatiebewaking;

import com.pharma.medicatiebewaking.util.Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class UtilTests {

    @Test
    public void EmailValidationTest() {

        // Given
        String email = "test@example.com";
        String incorrectemail = "testexample.com";

        // When
        Util util = new Util();
        boolean workingMail = util.EmailValidator(email);
        boolean notworkingMail = util.EmailValidator(incorrectemail);

        //Assert
        Assert.isTrue(workingMail, "Email correct");
        Assert.isTrue(!notworkingMail, "Email incorrect");
    }

    @Test
    public void PasswordValidationTests() {

        //Given
        String correct = "Test1234%";
        String notEightCharacters = "Test1%";
        String NoLowerCase = "TEST1234%";
        String NoUpperCase = "test1234%";
        String NoSpecialCharacter = "Test1234";
        String NoNumber = "Test@#$%";
        String NoWhiteSpace = "Test 1234&";

        //When
        Util util = new Util();
        boolean workingpass = util.PasswordValidator(correct);
        boolean EightCharacters = util.PasswordValidator(notEightCharacters);
        boolean LowerCase = util.PasswordValidator(NoLowerCase);
        boolean UpperCase = util.PasswordValidator(NoUpperCase);
        boolean SpecialCharacter = util.PasswordValidator(NoSpecialCharacter);
        boolean Number = util.PasswordValidator(NoNumber);
        boolean WhiteSpace = util.PasswordValidator(NoWhiteSpace);

        //Arrange
        Assert.isTrue(workingpass, "Password correct");
        Assert.isTrue(!EightCharacters, "Password incorrect");
        Assert.isTrue(!LowerCase, "Password incorrect");
        Assert.isTrue(!UpperCase, "Password incorrect");
        Assert.isTrue(!SpecialCharacter, "Password incorrect");
        Assert.isTrue(!Number, "Password incorrect");
        Assert.isTrue(!WhiteSpace, "Password incorrect");
    }
}
