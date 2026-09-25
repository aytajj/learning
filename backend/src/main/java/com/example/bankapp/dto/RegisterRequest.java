package com.example.bankapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Ad Soyad bos ola bilmez")
    private String fullName;

    @NotBlank(message = "Email bos ola bilmez")
    @Email(message = "Email formati yanlisdir")
    private String email;

    @NotBlank(message = "Sifre bos ola bilmez")
    @Size(min = 6, message = "Sifre minimum 6 simvol olmalidir")
    private String password;
}
