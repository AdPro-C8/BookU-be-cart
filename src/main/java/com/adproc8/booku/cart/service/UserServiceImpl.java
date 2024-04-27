package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.adproc8.booku.cart.model.User;
import com.adproc8.booku.cart.repository.UserRepository;

@Service
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) throws IllegalArgumentException {
        String plainPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(plainPassword);
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public Optional<User> findById(UUID id) throws IllegalArgumentException {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) throws IllegalArgumentException {
        return userRepository.findByUsername(username);
    }

    public void deleteById(UUID id) throws IllegalArgumentException {
        userRepository.deleteById(id);
    }
}
