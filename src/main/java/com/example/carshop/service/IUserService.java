package com.example.carshop.service;

import com.example.carshop.model.User;
import java.util.List;

public interface IUserService {
    User findByEmail(String email);
    List<User> findAll();
    void save(User user);
    User findById(Long id);
    void remove(Long id);
}
