package com.example.bankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountResponse {
    private String accountNumber;
    private BigDecimal balance;
    private String fullName;
    private String email;
}
