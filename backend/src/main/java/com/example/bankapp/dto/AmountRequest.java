package com.example.bankapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AmountRequest {

    @NotNull(message = "Mebleg bos ola bilmez")
    @DecimalMin(value = "0.01", message = "Mebleg 0-dan boyuk olmalidir")
    private BigDecimal amount;
}
