package com.example.bankapp.controller;

import com.example.bankapp.dto.AccountResponse;
import com.example.bankapp.dto.AmountRequest;
import com.example.bankapp.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<AccountResponse> getMyAccount(Authentication authentication) {
        return ResponseEntity.ok(accountService.getMyAccount(authentication.getName()));
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountResponse> deposit(Authentication authentication,
                                                     @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(accountService.deposit(authentication.getName(), request.getAmount()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountResponse> withdraw(Authentication authentication,
                                                      @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(accountService.withdraw(authentication.getName(), request.getAmount()));
    }
}
