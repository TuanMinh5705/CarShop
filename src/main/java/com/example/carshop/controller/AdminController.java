package com.example.carshop.controller;

import com.example.carshop.model.Car;
import com.example.carshop.service.ICarService;
import com.example.carshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminForm")
public class AdminController {
    @Autowired
    private ICarService iCarService;
    @Autowired
    private IUserService iUserService;

    @GetMapping("")
    public String homeAdmin(Model model){
        model.addAttribute("cars", iCarService.findAll());
        return "/admin/carManagement/carList";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("cars", new Car());
        return "/admin/carManagement/create";
    }

    @PostMapping("/save")
    public String save(Car car){
        iCarService.save(car);
        return "redirect:/AdminForm";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        model.addAttribute("cars", iCarService.findById(id));
        return "/admin/carManagement/edit";
    }

    @PostMapping("/update")
    public String edit(Car car){
        iCarService.save(car);
        return "redirect:/AdminForm";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {
        model.addAttribute("cars", iCarService.findById(id));
        return "/admin/carManagement/delete";
    }

    @PostMapping("delete")
    public String delete(Car car){
        iCarService.remove(car.getId());
        return "redirect:/AdminForm";
    }

    //UserManagement
    @GetMapping("/userList")
    public String homeUser(Model model){
        model.addAttribute("users", iUserService.findAll());
        return "/admin/userManagement/userList";
    }
}
