package com.example.demo.controller;

import com.example.demo.entiy.Role;
import com.example.demo.entiy.User;
import com.example.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;
    @GetMapping("/reg")
    public String regPage(){
        return "reg";
    }
    @PostMapping("/reg")
    public String addUser(User user, Model model) {
        UserDetails userBD = userRepo.findByUsername(user.getUsername());
        if (userBD != null) {
            model.addAttribute("message", "User exist!");
            return "reg";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);
        return "redirect:/login";
    }
}
