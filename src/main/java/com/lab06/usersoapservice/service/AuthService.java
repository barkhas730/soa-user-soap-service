package com.lab06.usersoapservice.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lab06.usersoapservice.entity.User;
import com.lab06.usersoapservice.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(String username, String password) {
        String normalizedUsername = normalizeUsername(username);

        if (normalizedUsername == null || password == null || password.isBlank()) {
            return "Hereglegchiin ner bolon nuuts ugee oruulna uu.";
        }

        if (userRepository.findByUsername(normalizedUsername).isPresent()) {
            return "Ene hereglegchiin ner ali hediin burtgeltei baina.";
        }

        User user = new User();
        user.setUsername(normalizedUsername);
        user.setPassword(password);
        userRepository.save(user);
        return "Hereglegch amjilttai burtgegdlee.";
    }

    public LoginResult loginUser(String username, String password) {
        String normalizedUsername = normalizeUsername(username);
        Optional<User> foundUser = normalizedUsername == null
                ? Optional.empty()
                : userRepository.findByUsername(normalizedUsername);

        if (foundUser.isEmpty()) {
            return new LoginResult(false, null, null, "Hereglegchiin ner oldsongui.");
        }

        User user = foundUser.get();
        if (!user.getPassword().equals(password)) {
            return new LoginResult(false, null, null, "Nuuts ug buruu baina.");
        }

        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userRepository.save(user);

        return new LoginResult(true, token, user.getId(), "Amjilttai nevterlee.");
    }

    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        return userRepository.findByToken(token).isPresent();
    }

    public Long getUserIdByToken(String token) {
        Optional<User> foundUser = userRepository.findByToken(token);
        return foundUser.map(User::getId).orElse(null);
    }

    private String normalizeUsername(String username) {
        if (username == null) {
            return null;
        }

        String normalized = username.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
