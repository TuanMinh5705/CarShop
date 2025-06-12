package com.example.carshop.controller;

import com.example.carshop.model.Car;
import com.example.carshop.model.CartItem;
import com.example.carshop.service.carService.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/homeClient")
public class UserController {

    @Autowired
    private ICarService iCarService;

    @GetMapping("")
    public String homeUser(Model model) {
        model.addAttribute("car", iCarService.findAll());
        return "/client/home-client";
    }

    @PostMapping("/cart/add/{carId}")
    public String addToCart(@PathVariable Long carId, HttpSession session) {
        Car car = iCarService.findById(carId);

        if (car == null) {
            return "redirect:/homeClient?error=carNotFound";
        }

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        boolean found = false;
        for (CartItem item : cart) {
            if (item.getCar().getId().equals(carId)) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            cart.add(new CartItem(car, 1));
        }

        session.setAttribute("cart", cart);
        return "redirect:/homeClient/cart/view";
    }

    @GetMapping("/cart/view")
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        model.addAttribute("cartItems", cart);
        return "/client/cart";
    }
}
