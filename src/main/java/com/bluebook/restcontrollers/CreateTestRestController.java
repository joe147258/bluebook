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
import org.springframework.web.bind.annotation.GetMapping;
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
        
        String[] incorrectAnswers = {
            params.get("incorrectAnswer1"),
            params.get("incorrectAnswer2"),
            params.get("incorrectAnswer3")
        };
        //service method is called to create the question. Returns false if it fails.
        return questionService.createMultiChoiceQuestion(workingTest, params.get("questionString"),
            params.get("correctAnswer"), incorrectAnswers);
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

    @PostMapping("/new/questions/{testId}/set-due")
    public final Boolean setDuedate(@PathVariable int testId, @RequestParam String date, @RequestParam String time) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Test test = testRepo.findById(testId);

        if(test == null) return false;
        if(user.getId() != test.getTestOwner().getId()) return false;

        return testService.setDueDate(test, date, time);
    }

    @GetMapping("/new/questions/{testId}/get-ques-info")
    public final Object getQuesInfo(@PathVariable final int testId, @RequestParam int qId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Test test = testRepo.findById(testId);

        if(test == null) return false;
        if(user.getId() != test.getTestOwner().getId()) return false;

        return questionService.getQuesInfo(testId, qId);
    }

    @PostMapping("/new/questions/{testId}/edit-question")
    public final Object editQuestion(@PathVariable final int testId, @RequestParam final Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Test workingTest = testRepo.findById(testId);

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
}