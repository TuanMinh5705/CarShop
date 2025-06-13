package com.example.carshop.controller;

import com.example.carshop.model.User;
import com.example.carshop.model.UserRole;
import com.example.carshop.model.UserStatus;
import com.example.carshop.model.CartItem;
import com.example.carshop.service.userService.IUserService;
import com.example.carshop.repository.CartItemRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/car-shop")
public class AuthController {

    private final IUserService userService;
    private final CartItemRepository cartItemRepository;

    public AuthController(IUserService userService, CartItemRepository cartItemRepository) {
        this.userService = userService;
        this.cartItemRepository = cartItemRepository;
    }

    @GetMapping("")
    public String showLoginForm() {
        return "/authenticate/login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {
        User user = userService.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Sai email hoặc mật khẩu.");
            return "/authenticate/login";
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            model.addAttribute("error", "Tài khoản của bạn đã bị khóa hoặc chưa được kích hoạt.");
            return "/authenticate/login";
        }

        session.setAttribute("currentUser", user);

        // Tải giỏ hàng từ DB
        List<CartItem> savedCart = cartItemRepository.findByUser(user);
        session.setAttribute("cart", savedCart);

        return (user.getRole() == UserRole.ADMIN) ? "redirect:/show-form" : "redirect:/home-client";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Không xóa giỏ hàng trong DB
        return "redirect:/car-shop";
    }
}
