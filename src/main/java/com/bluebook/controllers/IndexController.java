package com.bluebook.controllers;

import com.bluebook.config.CustomUserDetails;
import com.bluebook.domain.CustomUser;
import com.bluebook.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;






@Controller
@EnableAutoConfiguration
public class IndexController {
    @Autowired
    UserRepository userRepo;
    
    @GetMapping({"/", "/index"})
    public final String index(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        model.addAttribute("user", user);
        
        return "index";
    }



}