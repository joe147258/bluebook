package com.bluebook.controllers;


import java.util.Map;


import com.bluebook.config.CustomUserDetails;
import com.bluebook.domain.Classroom;
import com.bluebook.domain.CustomUser;
import com.bluebook.domain.Test;
import com.bluebook.repositories.ClassroomRepository;
import com.bluebook.repositories.TestRepository;
import com.bluebook.repositories.UserRepository;
import com.bluebook.service.TestService;


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

    @Autowired
    ClassroomRepository classroomRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    TestRepository testRepo;
    @Autowired
    TestService testService;

    

    @PostMapping(value="/new")
    public final String newTest(@RequestParam final Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Classroom classroom = classroomRepo.findById(Integer.parseInt(params.get("classId")));

        if(classroom == null) return "redirect:/server-problem"; 
        if("STUDENT".equals(user.getRole())) return "redirect:/permission-denied";
        if(user.getId() != classroom.getClassOwner().getId()) return "redirect:/permission-denied";

        int testId = testService.createNewTest(classroom, params.get("name"), params.get("type"), user);

        if(testId < 0) return "redirect:/server-problem";
        else return "redirect:/tests/new/questions/" + testId; 
    }

    @GetMapping(value="/new/questions/{testId}")
    public final String newTestQuestions(Model model, @PathVariable final int testId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());

        Test workingTest = testRepo.findById(testId);
        if(workingTest == null) return "redirect:/server-problem";
        if(workingTest.getTestOwner().getId() != user.getId()) return "redirect:/permission-denied";
        if(workingTest.getPublished()) return "redirect:/permission-denied";

        model.addAttribute("user", user);
        model.addAttribute("workingTest", workingTest);

        return "test-add-questions"; 
    }

    @GetMapping(value="/publish-test/{testId}")
    public final String publishTest(@PathVariable final int testId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return "redirect:/server-problem";
        if(workingTest.getTestOwner().getId() != user.getId()) return "redirect:/permission-denied";

        testService.publishTest(workingTest);

        return "redirect:/classrooms/teacher/" + workingTest.getClassroom().getId(); 
    }

    @PostMapping(value="/schedule-test/{testId}")
    public final String scheduleTest(@PathVariable final int testId, @RequestParam String scheduledDate,
        @RequestParam String scheduledTime) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return "redirect:/server-problem";
        if(workingTest.getTestOwner().getId() != user.getId()) return "redirect:/permission-denied";
        
        if(testService.scheduleTest(testId, scheduledDate, scheduledTime))
            return "redirect:/classrooms/teacher/" + workingTest.getClassroom().getId(); 
        else
            return "redirect:/tests/new/questions/" + testId + "?error=1";//TODO: IMPLEMENT AN ERROR MESSAGE (be date error) and then implement rescheduling
    }

 

    
    
}