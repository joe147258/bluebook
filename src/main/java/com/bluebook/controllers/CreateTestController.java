package com.bluebook.controllers;

import java.util.Arrays;
import java.util.Map;

import com.bluebook.config.CustomUserDetails;
import com.bluebook.domain.Classroom;
import com.bluebook.domain.CustomUser;
import com.bluebook.domain.Test;
import com.bluebook.repositories.ClassroomRepository;
import com.bluebook.repositories.TestRepository;
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
@RequestMapping("/tests/")
public class CreateTestController {
    /**
     * There are three types of tests that are planned to be implenented the purpose
     * of the array is to ensure the user doesn't bug the server. The three types
     * are: -END_FEEDBACK (feedback is given after the user has completed the quiz)
     * -INSTANT_FEEDBACK (feedback is given as soon as the question is answered)
     * -MARKED (questions are marked by the teacher and feedback is given then)
     */
    private static final String[] validTypes = { "END_FEEDBACK", "INSTANT_FEEDBACK", "MARKED_FEEDBACK" };
    @Autowired
    ClassroomRepository classroomRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    TestRepository testRepo;

    @PostMapping(value="/new")
    public String newTest(@RequestParam Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Classroom classroom = classroomRepo.findById(Integer.parseInt(params.get("classId")));
        if(classroom == null) return "redirect:/server-problem"; 
        if(!user.getRole().equals("TEACHER")) return "redirect:/permission-denied";
        if(user.getId() != classroom.getClassOwner().getId()) return "redirect:/permission-denied";
        //this checks type of quizzes
        if(!Arrays.stream(validTypes).parallel().anyMatch(params.get("type")::contains)) 
            return "redirect:/server-problem"; 
        String name = params.get("name");
        String type = params.get("type");

        int id = 0;
        while(testRepo.existsById(id)) id++;
        Test newTest = new Test(id, name, type, user, classroom);
        testRepo.save(newTest);

        return "redirect:/tests/new/questions/" + id; 
    }

    @GetMapping(value="/new/questions/{testId}")
    public String newTestQuestions(Model model, @PathVariable int testId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());

        Test workingTest = testRepo.findById(testId);
        if(workingTest == null) return "redirect:/server-problem";
        if(workingTest.getTestOwner().getId() != user.getId()) return "redirect:/permission-denied";

        model.addAttribute("user", user);
        model.addAttribute("workingTest", workingTest);


        return "test-add-questions"; 
    }
    
}