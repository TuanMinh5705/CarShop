package com.example.carshop.service.userService;

import com.example.carshop.model.User;

import java.util.List;

public interface IGenerateService<T> {
    List<T> findAll();

    void save(T t);

    T findById(Long id);

    void remove(Long id);
    List<T> searchByName(String keyword);
    User findByEmail(String email);
}