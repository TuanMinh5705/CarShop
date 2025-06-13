package com.example.carshop.controller;

import com.example.carshop.model.Car;
import com.example.carshop.model.CartItem;
import com.example.carshop.model.User;
import com.example.carshop.repository.CartItemRepository;
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

    @GetMapping("")
    public String homeUser(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/car-shop";

        model.addAttribute("car", iCarService.findAll());
        return "/client/home-client";
    }

    @PostMapping("/cart/add/{carId}")
    public String addToCart(@PathVariable Long carId, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/car-shop";

        Car car = iCarService.findById(carId);
        if (car == null) return "redirect:/home-client?error=carNotFound";

        List<CartItem> cart = cartItemRepository.findByUser(user);
        Optional<CartItem> existing = cart.stream()
                .filter(i -> i.getCar().getId().equals(carId))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + 1);
            cartItemRepository.save(item);
        } else {
            cartItemRepository.save(new CartItem(user, car, 1));
        }

        session.setAttribute("cart", cartItemRepository.findByUser(user));
        return "redirect:/home-client/cart/view";
    }

    @GetMapping("/cart/view")
    public String viewCart(HttpSession session, Model model,
                           @ModelAttribute("error") String error,
                           @ModelAttribute("message") String message) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/car-shop";

        List<CartItem> cart = cartItemRepository.findByUser(user);
        double total = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();

        model.addAttribute("cartItems", cart);
        model.addAttribute("total", total);
        model.addAttribute("error", error);
        model.addAttribute("message", message); // thêm thông báo thành công

        return "/client/cart";
    }

    @GetMapping("/cart/remove/{carId}")
    public String removeFromCart(@PathVariable Long carId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/car-shop";

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        cartItems.stream()
                .filter(item -> item.getCar().getId().equals(carId))
                .findFirst()
                .ifPresent(cartItemRepository::delete);

        session.setAttribute("cart", cartItemRepository.findByUser(user));
        redirectAttributes.addFlashAttribute("message", "✔️ Đã xoá sản phẩm khỏi giỏ hàng!");
        return "redirect:/home-client/cart/view";
    }

    @PostMapping("/cart/checkout")
    public String checkout(@RequestParam(value = "selectedCarIds", required = false) List<Long> selectedCarIds,
                           HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/car-shop";

        if (selectedCarIds == null || selectedCarIds.isEmpty()) {
            List<CartItem> cart = cartItemRepository.findByUser(user);
            double total = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();
            model.addAttribute("cartItems", cart);
            model.addAttribute("total", total);
            model.addAttribute("error", "⚠️ Bạn chưa chọn sản phẩm nào để mua ngay!");
            return "/client/cart";
        }

        List<CartItem> cart = cartItemRepository.findByUser(user);
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : cart) {
            if (selectedCarIds.contains(item.getCar().getId())) {
                selectedItems.add(item);
            }
        }

        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("total", selectedItems.stream().mapToDouble(CartItem::getTotalPrice).sum());

        return "/client/order-confirmation";
    }


}
