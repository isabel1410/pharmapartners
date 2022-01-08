package com.pharma.medicatiebewaking.controller;

import com.pharma.medicatiebewaking.Interface.HealthRepository;
import com.pharma.medicatiebewaking.Interface.PersonRepository;
import com.pharma.medicatiebewaking.model.Account;
import com.pharma.medicatiebewaking.Interface.AccountRepository;
import com.pharma.medicatiebewaking.model.Health;
import com.pharma.medicatiebewaking.util.Authorizer;
import com.pharma.medicatiebewaking.model.Person;
import com.pharma.medicatiebewaking.util.JwtUtil;
import com.pharma.medicatiebewaking.util.Util;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Controller
@RequestMapping("/account")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private HealthRepository healthRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("/register")
    public @ResponseBody
    String register(@RequestParam String email, @RequestParam String password, @RequestParam String repeatPassword, @RequestParam String firstname,
                    @RequestParam String lastname, @RequestParam("date") String dateOfBirth, HttpServletResponse response) throws NoSuchAlgorithmException, ParseException {
        Util util = new Util();
        HashMap<String, Object> data = new HashMap<>();

        // Check if user data is valid
        if (!util.EmailValidator(email)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            data.put("message", "Email is niet correct.");
            return new JSONObject(data).toString();
        }

        // Check if password is valid
        if (!repeatPassword.equals(password)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            data.put("message", "Wachtword komt niet overheen.");
            return new JSONObject(data).toString();
        }

        if (!util.PasswordValidator(password)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            data.put("message", "Wachtwoord moet minimaal een cijfer, speciale karakter, Hoofdletter, kleineletter en minimaal 8 karakters bevatten.");
            return new JSONObject(data).toString();
        }


        Account dBAccount = accountRepository.findAccountByEmail(email);

        // Check if email exist
        if (dBAccount != null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            data.put("message", "Deze email is al in gebruik.");
            return new JSONObject(data).toString();
        }

        // Hash + salt password
        Authorizer auth = new Authorizer();
        String salt = auth.GenerateSalt();
        String hashedPassword = auth.HashPassword(password, salt);

        // Save user to database
        String DateOfBirth = dateOfBirth;
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(DateOfBirth);
        Person person = new Person(firstname, lastname, date);
        healthRepository.save(person.getHealth());
        personRepository.save(person);

        // Create new account
        Account account = new Account(email, hashedPassword, salt, personRepository.findByPersonId(person.getPersonId()));

        accountRepository.save(account);

        data.put("message", "Registratie gelukt.");
        return new JSONObject(data).toString();
    }

    @PostMapping("/login")
    public @ResponseBody
    String login(@RequestParam String email, @RequestParam String password, HttpServletResponse response) throws NoSuchAlgorithmException {
        HashMap<String, Object> data = new HashMap<>();

        // Get user's hash by email from database
        Account account = accountRepository.findAccountByEmail(email);

        // Check if account exist
        if (account == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            data.put("message", "Email of wachtwoord is onjuist.");
            return new JSONObject(data).toString();
        }

        String databaseHash = account.getPassword();

        // Hash + salt password
        Authorizer auth = new Authorizer();
        String hashedPassword = auth.HashPassword(password, account.getSalt());

        // Check if hashed password and user password are the same
        boolean loginCheck = auth.ValidatePassword(databaseHash, hashedPassword);
        if (loginCheck) {
            final String jwt = jwtTokenUtil.generateToken(account);
            data.put("message", "Login gelukt.");
            data.put("jwt", jwt);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            data.put("message", "Email of wachtwoord is onjuist.");
        }
        return new JSONObject(data).toString();
    }

    @PutMapping("/change")
    public @ResponseBody
    String changeAccount(@RequestParam int personId, String email, HttpServletResponse response) throws Exception{
        HashMap<String, Object> data = new HashMap<>();
        Util util = new Util();

        // Check if email is valid
        if (!util.EmailValidator(email)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            data.put("message", "Email is niet correct.");
            return new JSONObject(data).toString();
        }
        Account dBAccount = accountRepository.findAccountByEmail(email);

        // Check if email exist
        if (dBAccount != null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            data.put("message", "Dit email is al in gebruik.");
            return new JSONObject(data).toString();
        }
        try{
            Account account = accountRepository.findAccountByPersonPersonId(personId);
            account.setEmail(email);
            accountRepository.save(account);
            data.put("message", "Uw email is gewijzigd");
            return new JSONObject(data).toString();
        }catch (Exception e){
            System.out.println(e.getMessage());
            data.put("message", "Er is iets mis gegaan met foutcode" + e.getMessage());
            return new JSONObject(data).toString();
        }

    }

    @GetMapping("/{personId}")
    public @ResponseBody
    String getAccount(@PathVariable int personId, HttpServletResponse response) {
        HashMap<String, Object> data = new HashMap<>();


        try{
            Account account = accountRepository.findAccountByPersonPersonId(personId);
            Account newAccount = new Account();
            newAccount.setPerson(new Person());
            newAccount.setEmail(account.getEmail());
            newAccount.getPerson().setFirstname(account.getPerson().getFirstname());
            newAccount.getPerson().setLastname(account.getPerson().getLastname());
            newAccount.getPerson().setDateOfBirth(account.getPerson().getDateOfBirth());

            response.setStatus(HttpServletResponse.SC_OK);
            data.put("account", newAccount);
            data.put("message", "Account gegevens opgehaald");
            return new JSONObject(data).toString();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            data.put("message", "Account gegevens niet opgehaald met foutmelding " + ex.getMessage() + ", Probeer het over enkele minuten opnieuw :)");
            return new JSONObject(data).toString();
        }
    }
}
