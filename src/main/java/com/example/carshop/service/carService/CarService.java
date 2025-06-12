package com.example.carshop.service.carService;

import com.example.carshop.model.Car;
import com.example.carshop.repository.carService.ICarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService implements ICarService {
    @Autowired
    private ICarRepository iCarRepository;

    @Override
    public List<Car> findAll() {
        return iCarRepository.findAll();
    }

    @Override
    public void save(Car car) {
        iCarRepository.save(car);
    }

    @Override
    public Car findById(Long id) {
        return iCarRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        iCarRepository.remove(id);
    }

    @Override
    public List<Car> searchByName(String keyword) {
        return iCarRepository.searchByName(keyword);
    }

}