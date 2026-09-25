package com.example.bankapp.service;

import com.example.bankapp.dto.AccountResponse;
import com.example.bankapp.model.Account;
import com.example.bankapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public AccountResponse getMyAccount(String email) {
        Account account = findByEmail(email);
        return toResponse(account);
    }

    @Transactional
    public AccountResponse deposit(String email, BigDecimal amount) {
        Account account = findByEmail(email);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        return toResponse(account);
    }

    @Transactional
    public AccountResponse withdraw(String email, BigDecimal amount) {
        Account account = findByEmail(email);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Balans kifayet etmir");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        return toResponse(account);
    }

    private Account findByEmail(String email) {
        return accountRepository.findByUser_Email(email)
                .orElseThrow(() -> new IllegalArgumentException("Hesab tapilmadi"));
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getUser().getFullName(),
                account.getUser().getEmail()
        );
    }
}
