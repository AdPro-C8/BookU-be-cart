package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adproc8.booku.cart.model.User;
import com.adproc8.booku.cart.repository.UserRepository;

@Service
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) throws IllegalArgumentException {
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
