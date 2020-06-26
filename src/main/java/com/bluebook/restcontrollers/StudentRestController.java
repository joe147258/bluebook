package com.bluebook.restcontrollers;

import java.util.HashMap;

import com.bluebook.config.CustomUserDetails;
import com.bluebook.domain.Classroom;
import com.bluebook.domain.CustomUser;
import com.bluebook.repositories.ClassroomRepository;
import com.bluebook.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentRestController {
    
    @Autowired
    ClassroomRepository classroomRepo;
    @Autowired
    UserRepository userRepo;

    @GetMapping("/check-class/{joinCode}")
    public HashMap<String, Object> classExists(@PathVariable String joinCode) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Classroom classroom = classroomRepo.findByJoinCode(joinCode);

        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        if(classroom == null) {
            returnMap.put("response", "NO_CLASS");
            return returnMap;
        } else if(classroom.containsUser(user.getId())){
            returnMap.put("response", "IN_CLASS");
            return returnMap;
        } else if(classroom.getBannedUsers().get(user.getId()) != null) {
            returnMap.put("response", "BANNED");
            returnMap.put("teacher", classroom.getClassOwner().getUsername());
            return returnMap;
        } else {
            returnMap.put("response", "SUCCESS");
            return returnMap;    
        }

    }
}