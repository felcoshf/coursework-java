package com.example.game_library.service;

import com.example.game_library.model.User;
import com.example.game_library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean register(String login, String password) {
        if (userRepository.existsByLogin(login)) return false;
        userRepository.save(new User(login, password));
        return true;
    }

    public User authenticate(String login, String password) {
        return userRepository.findByLogin(login)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
