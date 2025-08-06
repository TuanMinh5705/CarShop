package com.example.carshop.controller;

import com.example.carshop.model.User;
import com.example.carshop.model.UserRole;
import com.example.carshop.model.UserStatus;
import com.example.carshop.service.carService.ICarService;
import com.example.carshop.service.userService.IUserService;
import com.example.carshop.repository.CartItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/car-shop")
public class AuthController {

    private final ICarService carService;
    private final IUserService userService;
    private final CartItemRepository cartItemRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final String LOGIN_VIEW = "/authenticate/login";
    private static final String REGISTER_VIEW = "/authenticate/register";
    private static final String HOME_CLIENT_REDIRECT = "redirect:/home-client";
    private static final String ADMIN_REDIRECT = "redirect:/show-form";
    private static final String HOME_REDIRECT = "redirect:/car-shop";

    public AuthController(ICarService carService, IUserService userService, CartItemRepository cartItemRepository) {
        this.carService = carService;
        this.userService = userService;
        this.cartItemRepository = cartItemRepository;
    }

    @GetMapping("")
    public String showHomePage(Model model) {
        model.addAttribute("car", carService.findAll());
        return "/client/home-client";
    }

    @GetMapping("/showLogin")
    public String showLoginForm(Model model) {
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
            return LOGIN_VIEW;
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            model.addAttribute("error", "Tài khoản của bạn đã bị khóa hoặc chưa được kích hoạt.");
            return LOGIN_VIEW;
        }

        session.setAttribute("currentUser", user);
        return (user.getRole() == UserRole.ADMIN) ? ADMIN_REDIRECT : HOME_CLIENT_REDIRECT;
    }

    @GetMapping("/showRegister")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new User());
        return REGISTER_VIEW;
    }

    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute("userForm") User userForm,
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     Model model,
                                     RedirectAttributes redirectAttrs) {

        if (!isValidEmail(userForm.getEmail())) {
            model.addAttribute("error", "Email không hợp lệ.");
            return REGISTER_VIEW;
        }

        if (!userForm.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu và xác nhận mật khẩu không khớp.");
            return REGISTER_VIEW;
        }

        if (userService.findByEmail(userForm.getEmail()) != null) {
            model.addAttribute("error", "Email này đã được đăng ký trước đó.");
            return REGISTER_VIEW;
        }

        userForm.setRole(UserRole.USER);
        userForm.setStatus(UserStatus.ACTIVE);
        userService.save(userForm);

        redirectAttrs.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        return HOME_REDIRECT;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return HOME_REDIRECT;
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
