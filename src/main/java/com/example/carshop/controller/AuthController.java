package com.example.carshop.controller;

import com.example.carshop.model.User;
import com.example.carshop.model.UserRole;
import com.example.carshop.model.UserStatus;
import com.example.carshop.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carshop")
public class AuthController {
    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showLoginForm() {
        return "/authenticate/login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Sai email hoặc mật khẩu.");
            return "/authenticate/login";
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            model.addAttribute("error", "Tài khoản của bạn đã bị khóa hoặc chưa được kích hoạt.");
            return "/authenticate/login";
        }

        model.addAttribute("user", user);
        return (user.getRole() == UserRole.ADMIN) ? "redirect:/adminForm" : "redirect:/userForm";
    }
}
