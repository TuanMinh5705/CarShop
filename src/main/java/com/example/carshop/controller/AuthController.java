// src/main/java/com/example/carshop/controller/AuthController.java
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/car-shop")
public class AuthController {
    private final IUserService userService;
    private final CartItemRepository cartItemRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
    );

    public AuthController(IUserService userService, CartItemRepository cartItemRepository) {
        this.userService = userService;
        this.cartItemRepository = cartItemRepository;
    }

    @GetMapping("")
    public String showLoginForm(Model model) {
        return "/authenticate/login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {
        // 1. Tìm user theo email
        User user = userService.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Sai email hoặc mật khẩu.");
            return "/authenticate/login";
        }
        // 2. Kiểm tra trạng thái ACTIVE
        if (user.getStatus() != UserStatus.ACTIVE) {
            model.addAttribute("error", "Tài khoản của bạn đã bị khóa hoặc chưa được kích hoạt.");
            return "/authenticate/login";
        }
        // 3. Lưu thông tin user vào session (bỏ cart)
        session.setAttribute("currentUser", user);

        // 4. Chuyển hướng theo role
        if (user.getRole() == UserRole.ADMIN) {
            return "redirect:/show-form";
        } else {
            return "redirect:/home-client";
        }
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new User());
        return "/authenticate/register";
    }

    @PostMapping("/register")
    public String handleRegistration(
            @ModelAttribute("userForm") User userForm,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model,
            RedirectAttributes redirectAttrs) {

        if (!EMAIL_PATTERN.matcher(userForm.getEmail()).matches()) {
            model.addAttribute("error", "Email không hợp lệ.");
            return "/authenticate/register";
        }
        if (!userForm.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu và xác nhận mật khẩu không khớp.");
            return "/authenticate/register";
        }
        if (userService.findByEmail(userForm.getEmail()) != null) {
            model.addAttribute("error", "Email này đã được đăng ký trước đó.");
            return "/authenticate/register";
        }

        userForm.setRole(UserRole.USER);
        userForm.setStatus(UserStatus.ACTIVE);
        userService.save(userForm);
        redirectAttrs.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/car-shop";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/car-shop";
    }
}
