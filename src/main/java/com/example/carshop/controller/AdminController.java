package com.example.carshop.controller;

import com.example.carshop.model.Car;
import com.example.carshop.model.User;
import com.example.carshop.service.userService.IUserService;
import com.example.carshop.service.carService.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/show-form")
public class AdminController {
    @Autowired
    private ICarService iCarService;
    @Autowired
    private IUserService iUserService;

    @GetMapping("")
    public String homeAdmin(Model model){
        model.addAttribute("car", iCarService.findAll());
        return "/admin/car-management/car-list";
    }

    @GetMapping("/createCar")
    public String createCar(Model model){
        model.addAttribute("car", new Car());
        return "/admin/car-management/car-create";
    }

    @PostMapping("/saveCreateCar")
    public String saveCreateCar(Car car){
        iCarService.save(car);
        return "redirect:/show-form";
    }

    @GetMapping("/{id}/editCar")
    public String editCar(@PathVariable Long id, Model model){
        model.addAttribute("car", iCarService.findById(id));
        return "/admin/car-management/car-edit";
    }

    @PostMapping("/updateCar")
    public String updateCar(Car car){
        iCarService.save(car);
        return "redirect:/show-form";
    }

    @GetMapping("/{id}/deleteCar")
    public String deleteCar(@PathVariable Long id, Model model) {
        model.addAttribute("car", iCarService.findById(id));
        return "/admin/car-management/car-delete";
    }

    @PostMapping("/confirmDelete")
    public String delete(Car car){
        iCarService.remove(car.getId());
        return "redirect:/show-form";
    }

    @GetMapping("/{id}/viewCar")
    public String viewCar(@PathVariable Long id, Model model){
        model.addAttribute("car", iCarService.findById(id));
        return "/admin/car-management/car-view";
    }

    @GetMapping("/searchCar")
    public String searchCars(@RequestParam("keyword") String keyword, Model model) {
        List<Car> carList = iCarService.searchByName(keyword);
        model.addAttribute("car", carList);
        model.addAttribute("keyword", keyword);
        return "/admin/car-management/car-list";
    }

    //UserManagement
    @GetMapping("/clientList")
    public String listUser(Model model){
        model.addAttribute("user", iUserService.findAll());
        return "/admin/client-management/client-list";
    }

    @GetMapping("/createClient")
    public String createUser(Model model){
        model.addAttribute("user", new User());
        return "/admin/client-management/client-create";
    }

    @PostMapping("/saveClient")
    public String saveUser(User user){
        iUserService.save(user);
        return "redirect:/show-form/clientList";
    }

    @GetMapping("/{id}/editClient")
    public String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", iUserService.findById(id));
        return "/admin/client-management/client-edit";
    }

    @PostMapping("/updateClient")
    public String updateUser(User user){
        iUserService.save(user);
        return "redirect:/show-form/clientList";
    }

    @GetMapping("/{id}/viewClient")
    public String userView(@PathVariable long id, Model model){
        model.addAttribute("user", iUserService.findById(id));
        return "/admin/client-management/client-view";
    }

    @GetMapping("/searchClient")
    public String searchUsers(@RequestParam("keyword") String keyword, Model model) {
        List<User> userList = iUserService.searchByName(keyword);
        model.addAttribute("user", userList);
        model.addAttribute("keyword", keyword);
        return "/admin/client-management/client-list";
    }
}