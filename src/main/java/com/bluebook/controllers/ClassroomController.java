package com.bluebook.controllers;

import java.util.Map;

import com.bluebook.config.CustomUserDetails;
import com.bluebook.domain.Classroom;
import com.bluebook.domain.CustomUser;
import com.bluebook.repositories.ClassroomRepository;
import com.bluebook.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/classrooms")
public class ClassroomController {

    @Autowired
    UserRepository userRepo;
    @Autowired
    ClassroomRepository classroomRepo;
    
    @GetMapping("/new")
    public final String newClassroom(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        model.addAttribute("user", user);
        if(!user.getRole().equals("TEACHER")) return "redirect:/permission-denied";
        return "new-classroom-form";
    }

    @PostMapping("/create-classroom")
    public final String createClassroom(Model model, @RequestParam final Map<String, String> params){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        if(!user.getRole().equals("TEACHER")) return "redirect:/permission-denied";
        String className = params.get("className");
        String classDesc = params.get("classDesc");
        /**
         * There is front end validation on this as well and only resorts to this if a user
         * messes with the front end or have JavaScript disabled, etc
         */
        if(className.length() == 0 || classDesc.length() == 0) return "redirect:/classrooms/new?error=1"; //No length error 
        if(className.length() > 60 || classDesc.length() > 500) return "redirect:/classrooms/new?error=2"; //too long error

        int id = 0;
        while(classroomRepo.existsById(id)) id++;
        Classroom classroom = new Classroom(id, user, className, classDesc);
        classroomRepo.save(classroom);
        return "redirect:/classrooms/teacher/" + id; 
        
    }

    @GetMapping("/teacher/{classId}")
    public final String viewClassTeacher(Model model, @PathVariable final int classId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Classroom classroom = classroomRepo.findById(classId);
        
        if(classroom == null) return "redirect:/server-problem"; 
        if(user.getId() != classroom.getClassOwner().getId()) return "redirect:/permission-denied";

        model.addAttribute("user", user);
        model.addAttribute("classroom", classroom);
        return "teacher-page";
    }

    @PostMapping("/join-class")
    public final String joinClass(Model model, @RequestParam final String joinCode){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Classroom classroom = classroomRepo.findByJoinCode(joinCode);

        if(classroom == null) return "redirect:/server-problem"; 
        if("STUDENT".equals(user.getRole())) return "redirect:/permission-denied";
        if(classroom.containsUser(user.getId())) return "redirect:/permission-denied";
        if(classroom.getBannedUsers().get(user.getId()) != null) return "redirect:/permission-denied";

        classroom.getStudents().add(user);
        user.getStudentClassrooms().add(classroom);
        userRepo.save(user);
        classroomRepo.save(classroom);
        
        return "redirect:/classrooms/student/" + classroom.getId(); 
    }

    @GetMapping("/student/{classId}")
    public final String viewClassStudent(Model model, @PathVariable final int classId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Classroom classroom = classroomRepo.findById(classId);

        if(classroom == null) return "redirect:/server-problem"; 
        if(!classroom.containsUser(user.getId())) return "redirect:/permission-denied";

        model.addAttribute("user", user);
        model.addAttribute("classroom", classroom);
        return "student-page";
    }
}