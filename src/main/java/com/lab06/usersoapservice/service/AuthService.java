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
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return "Хэрэглэгчийн нэр болон нууц үгээ оруулна уу.";
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return "Энэ хэрэглэгчийн нэр аль хэдийн бүртгэлтэй байна.";
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password);
        userRepository.save(user);
        return "Хэрэглэгч амжилттай бүртгэгдлээ.";
    }

    public LoginResult loginUser(String username, String password) {
        Optional<User> foundUser = userRepository.findByUsername(username);

        if (foundUser.isEmpty()) {
            return new LoginResult(false, null, null, "Хэрэглэгчийн нэр олдсонгүй.");
        }

        User user = foundUser.get();
        if (!user.getPassword().equals(password)) {
            return new LoginResult(false, null, null, "Нууц үг буруу байна.");
        }

        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userRepository.save(user);

        return new LoginResult(true, token, user.getId(), "Амжилттай нэвтэрлээ.");
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
}
