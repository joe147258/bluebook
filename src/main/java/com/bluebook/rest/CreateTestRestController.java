package com.bluebook.rest;

import java.util.Map;

import com.bluebook.domain.CustomUser;

import com.bluebook.domain.Test;
import com.bluebook.repository.QuestionRepository;
import com.bluebook.repository.TestRepository;
import com.bluebook.repository.UserRepository;
import com.bluebook.service.QuestionService;
import com.bluebook.service.TestService;
import com.bluebook.config.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/question")
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
    
    

    @PostMapping("/{testId}/add-multi-choice")
    public Boolean addMultiChoiceQuestion(@PathVariable int testId, @RequestParam Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;
        
        String[] incorrectAnswers = {
            params.get("incorrectAnswer1"),
            params.get("incorrectAnswer2"),
            params.get("incorrectAnswer3")
        };
        //service method is called to create the question. Returns false if it fails.
        return questionService.createMultiChoiceQuestion(workingTest, params.get("questionString"),
            params.get("correctAnswer"), incorrectAnswers);
    }

    @PostMapping("/{testId}/add-true-false")
    public Object addTrueFalse(@PathVariable int testId, @RequestParam Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;
        //service method is called to create the question. Returns false if it fails.
        return questionService.createTrueFalseQuestion(workingTest,  
            params.get("questionString"), params.get("correctAnswer").toUpperCase());
    }

    //in the future you can add character different, this is whyu its a seperate method
    @PostMapping("/{testId}/add-input")
    public Object addInput(@PathVariable int testId, @RequestParam Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;

        return questionService.createInputQuestion(workingTest,  
            params.get("questionString"), params.get("correctAnswer"), params.get("distance"));
    }

    @PostMapping("/{testId}/change-fbtype/{newFbType}")
    public Boolean changeFeedbackType(@PathVariable int testId, @PathVariable String newFbType) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;
        if(newFbType.equals(workingTest.getFeedbackType())) return false;

        return testService.changeFeedbackType(workingTest, newFbType);
    }

    @PostMapping("/{testId}/change-title")
    public Boolean changeTitle(@PathVariable int testId, @RequestParam String newTitle) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test test = testRepo.findById(testId);

        if(test == null) return false;
        if(user.getId() != test.getTestOwner().getId()) return false;

        return testService.updateTestTitle(test, newTitle);
    }

    @PostMapping("/{testId}/set-due")
    public Boolean setDuedate(@PathVariable int testId, @RequestParam String date, @RequestParam String time) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test test = testRepo.findById(testId);

        if(test == null) return false;
        if(user.getId() != test.getTestOwner().getId()) return false;

        return testService.setDueDate(test, date, time);
    }

    @GetMapping("/{testId}/get-ques-info")
    public Object getQuesInfo(@PathVariable int testId, @RequestParam int qId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test test = testRepo.findById(testId);

        if(test == null) return false;
        if(user.getId() != test.getTestOwner().getId()) return false;

        return questionService.getQuesInfo(testId, qId);
    }

    @PostMapping("/{testId}/edit-question")
    public Object editQuestion(@PathVariable int testId, @RequestParam Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(user.getId() != workingTest.getTestOwner().getId()) return false;

        int qId = -1;
        try{
            qId = Integer.parseInt(params.get("qId"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        String question = params.get("questionString");
        String correctAnswer = params.get("correctAnswer");
        switch(params.get("type")) {
            case "MULTI": {
                String[] incorrectAnswers = {
                    params.get("incorrectAnswer1"),
                    params.get("incorrectAnswer2"),
                    params.get("incorrectAnswer3")
                };
                return questionService.editMultiChoiceQuestion(qId, workingTest, question, 
                    correctAnswer, incorrectAnswers);
            }
            case "INPUT": {
                int distanceInt = -1;
                try{
                    distanceInt = Integer.parseInt(params.get("distance"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }
                return questionService.editInputQuestion(qId, workingTest, question, correctAnswer, distanceInt);
            }
            case "BOOL": {
                return questionService.editTrueFalseQuestion(qId, workingTest, question, correctAnswer);
            }
            default: {
                return false;
            }
        } 
    }

    @PostMapping("/{testId}/delete-question/{qId}")
    public Boolean deleteQuestion(@PathVariable int qId, @PathVariable int testId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(user.getId() != workingTest.getTestOwner().getId()) return false;

        return questionService.deleteQuestion(qId, testId);
    }
}