package com.example.carshop.controller;

import com.example.carshop.model.Car;
import com.example.carshop.model.CartItem;
import com.example.carshop.model.User;
import com.example.carshop.repository.CartItemRepository;
import com.example.carshop.repository.userService.IUserRepository;
import com.example.carshop.service.carService.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home-client")
public class UserController {

    @Autowired
    private ICarService iCarService;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired private IUserRepository userRepository;

    @GetMapping("")
    public String homeUser(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/car-shop";

        model.addAttribute("car", iCarService.findAll());
        return "/client/home-client";
    }
    @GetMapping("/car-detail/{id}")
    public String showCarDetail(@PathVariable Long id, Model model) {
        model.addAttribute("car", iCarService.findById(id));
        return "/client/car-detail";
    }
    @GetMapping("/info-client")
    public String showInfoClient(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/authenticate/login";
        }
        User userFromDb = userRepository.findById(currentUser.getId());
        model.addAttribute("client", userFromDb);
        return "/client/info-client";
    }
    @GetMapping("/search")
    public String searchCars(@RequestParam("keyword") String keyword, Model model) {
        List<Car> cars = iCarService.searchByName(keyword);
        model.addAttribute("car", cars);
        model.addAttribute("keyword", keyword);
        return "/client/home-client"; // đúng tên file HTML
    }
}
