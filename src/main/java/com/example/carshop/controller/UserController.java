package com.example.carshop.controller;

import com.example.carshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userForm")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @GetMapping("")
    public String homeUser(Model model){
        model.addAttribute("users", iUserService.findAll());
        return "userList";
    }

}
