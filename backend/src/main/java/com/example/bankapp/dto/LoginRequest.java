package com.example.bankapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email bos ola bilmez")
    private String email;

    @NotBlank(message = "Sifre bos ola bilmez")
    private String password;
}
