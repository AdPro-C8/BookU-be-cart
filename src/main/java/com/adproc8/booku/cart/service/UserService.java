package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import com.adproc8.booku.cart.model.User;

public interface UserService {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    void deleteById(UUID id);
}
