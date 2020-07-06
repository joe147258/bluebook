package com.bluebook.restcontrollers;


import java.util.Map;

import com.bluebook.domain.CustomUser;

import com.bluebook.domain.Test;

import com.bluebook.repositories.QuestionRepository;
import com.bluebook.repositories.TestRepository;
import com.bluebook.repositories.UserRepository;
import com.bluebook.service.QuestionService;
import com.bluebook.service.TestService;
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
    QuestionService questionService;
    @Autowired
    TestService testService;
    
    

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
        return questionService.createMultiChoiceQuestion(workingTest, params.get("questionString"),
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
        return questionService.createTrueFalseQuestion(workingTest,  
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

        return questionService.createInputQuestion(workingTest,  
            params.get("questionString"), params.get("correctAnswer"), params.get("distance"));
    }

    @PostMapping("/new/questions/{testId}/change-fbtype/{newFbType}")
    public final Boolean changeFeedbackType(@PathVariable final int testId, @PathVariable final String newFbType) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;
        if(workingTest.getPublished()) return false;
        if(newFbType.equals(workingTest.getFeedbackType())) return false;

        return testService.changeFeedbackType(workingTest, newFbType);
    }

    @PostMapping("/new/questions/{testId}/change-title")
    public final Boolean changeTitle(@PathVariable int testId, @RequestParam String newTitle) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Test test = testRepo.findById(testId);

        if(test == null) return false;
        if(user.getId() != test.getTestOwner().getId()) return false;

        return testService.updateTestTitle(test, newTitle);
    }
}