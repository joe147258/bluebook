package com.bluebook.restcontrollers;


import java.util.Map;

import com.bluebook.domain.CustomUser;

import com.bluebook.domain.Test;

import com.bluebook.repositories.QuestionRepository;
import com.bluebook.repositories.TestRepository;
import com.bluebook.repositories.UserRepository;
import com.bluebook.service.QuestionService;
import com.bluebook.config.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
public class CreateTestRestController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    TestRepository testRepo;
    @Autowired
    QuestionRepository quesRepo;
    @Autowired
    QuestionService testService;
    
    

    @PostMapping("/new/questions/{testId}/add-multi-choice")
    public final Boolean addMultiChoiceQuestion(@PathVariable final int testId, @RequestParam final Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;
        
        String[] incorrectAnsers = {
            params.get("incorrectAnswer1"),
            params.get("incorrectAnswer2"),
            params.get("incorrectAnswer2")
        };
        //service method is called to create the question. Returns false if it fails.
        return testService.createMultiChoiceQuestion(workingTest, params.get("questionString"),
            params.get("correctAnswer"), incorrectAnsers);
    }

    @PostMapping("/new/questions/{testId}/add-true-false")
    public final Object addTrueFalse(@PathVariable final int testId, @RequestParam final Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;
        //service method is called to create the question. Returns false if it fails.
        return testService.createTrueFalseQuestion(workingTest,  
            params.get("questionString"), params.get("correctAnswer").toLowerCase());
    }

    //in the future you can add character different, this is whyu its a seperate method
    @PostMapping("/new/questions/{testId}/add-input")
    public final Object addInput(@PathVariable final int testId, @RequestParam final Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;

        return testService.createInputQuestion(workingTest,  
            params.get("questionString"), params.get("correctAnswer"), 0);
    }
}