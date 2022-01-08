package com.pharma.medicatiebewaking.Interface;

import com.pharma.medicatiebewaking.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    Account findAccountByEmail(String email);
    Account deleteAccountByAccountId(int accountId);
    Account findAccountByPersonPersonId(int personId);
}

