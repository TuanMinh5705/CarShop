package com.example.carshop.service.carService;

import java.util.List;

public interface IGenerateService<T> {
    List<T> findAll();

    void save(T t);

    T findById(Long id);

    void remove(Long id);
    List<T> searchByName(String keyword);
}