package com.example.carshop.repository.userService;

import com.example.carshop.model.User;

import java.util.List;

public interface IGenerateRepository<T> {
    List<T> findAll();

    T findById(Long id);

    void save(T t);

    void remove(Long id);
    List<T> searchByName(String keyword);

    T findByEmail(String email);
}