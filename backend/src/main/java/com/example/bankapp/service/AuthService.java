package com.example.bankapp.service;

import com.example.bankapp.dto.AuthResponse;
import com.example.bankapp.dto.LoginRequest;
import com.example.bankapp.dto.RegisterRequest;
import com.example.bankapp.model.Account;
import com.example.bankapp.model.User;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.UserRepository;
import com.example.bankapp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private static final SecureRandom RANDOM = new SecureRandom();

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Bu email artiq qeydiyyatdan kecib");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();
        accountRepository.save(account);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getFullName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Istifadeci tapilmadi"));

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getFullName(), user.getEmail());
    }

    private String generateAccountNumber() {
        String candidate;
        do {
            StringBuilder sb = new StringBuilder("AZ");
            for (int i = 0; i < 10; i++) {
                sb.append(RANDOM.nextInt(10));
            }
            candidate = sb.toString();
        } while (accountRepository.existsByAccountNumber(candidate));
        return candidate;
    }
}
