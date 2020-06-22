package uol.bluebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import uol.bluebook.domain.CustomUser;
import uol.bluebook.repositories.UserRepository;
import uol.bluebook.config.CustomUserDetails;






@Controller
@EnableAutoConfiguration
public class IndexController {
    @Autowired
    UserRepository userRepo;
    
    @GetMapping({"/", "/index"})
    public String index(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        model.addAttribute("user", user);
        
        return "index";
    }



}