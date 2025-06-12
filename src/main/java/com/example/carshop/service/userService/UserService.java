package com.example.carshop.service.userService;

import com.example.carshop.model.User;
import com.example.carshop.repository.userService.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public User findByEmail(String email) {
        return iUserRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return iUserRepository.findAll();
    }

    @Override
    public void save(User user) {
        iUserRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return iUserRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        iUserRepository.remove(id);
    }

    @Override
    public List<User> searchByName(String keyword) {
        return iUserRepository.searchByName(keyword);
    }

}