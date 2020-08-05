package com.bluebook.controller;


import java.util.Map;


import com.bluebook.config.CustomUserDetails;
import com.bluebook.domain.Classroom;
import com.bluebook.domain.CustomUser;
import com.bluebook.domain.Test;
import com.bluebook.repository.ClassroomRepository;
import com.bluebook.repository.TestRepository;
import com.bluebook.repository.UserRepository;
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
@RequestMapping("/test/")
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
    public String newTest(@RequestParam Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Classroom classroom = classroomRepo.findById(Integer.parseInt(params.get("classId")));

        if(classroom == null) return "redirect:/server-problem"; 
        if("STUDENT".equals(user.getRole())) return "redirect:/permission-denied";
        if(user.getId() != classroom.getClassOwner().getId()) return "redirect:/permission-denied";

        int testId = testService.createNewTest(classroom, params.get("name"), params.get("type"), user);

        if(testId < 0) return "redirect:/server-problem";
        else return "redirect:/test/question/" + testId; 
    }

    @GetMapping(value="/question/{testId}")
    public String newTestQuestions(Model model, @PathVariable int testId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());

        Test workingTest = testRepo.findById(testId);
        if(workingTest == null) return "redirect:/server-problem";
        if(workingTest.getTestOwner().getId() != user.getId()) return "redirect:/permission-denied";
        if(workingTest.getPublished()) return "redirect:/permission-denied";

        model.addAttribute("user", user);
        model.addAttribute("workingTest", workingTest);

        return "test-add-questions"; 
    }

    @GetMapping(value="/publish/{testId}")
    public String publishTest(@PathVariable int testId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return "redirect:/server-problem";
        if(workingTest.getTestOwner().getId() != user.getId()) return "redirect:/permission-denied";

        testService.publishTest(workingTest);

        return "redirect:/classroom/teacher/" + workingTest.getClassroom().getId(); 
    }

    @PostMapping(value="/schedule/{testId}")
    public String scheduleTest(@PathVariable int testId, @RequestParam String scheduledDate,
        @RequestParam String scheduledTime) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return "redirect:/server-problem";
        if(workingTest.getTestOwner().getId() != user.getId()) return "redirect:/permission-denied";
        
        if(testService.scheduleTest(testId, scheduledDate, scheduledTime))
            return "redirect:/classroom/teacher/" + workingTest.getClassroom().getId(); 
        else
            return "redirect:/test/question" + testId + "?error=1";//TODO: IMPLEMENT AN ERROR MESSAGE (be date error) and then implement rescheduling
    }

 
    @GetMapping(value="/edit/{testId}")
    public String editTest(@PathVariable int testId) {

        return "";
    } 
    
    
}