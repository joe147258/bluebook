package com.bluebook.restcontrollers;

import java.util.Arrays;
import java.util.Map;

import com.bluebook.domain.CustomUser;
import com.bluebook.domain.InputQuestion;
import com.bluebook.domain.MultiChoiceQuestion;
import com.bluebook.domain.Test;
import com.bluebook.domain.TrueFalseQuestion;
import com.bluebook.repositories.QuestionRepository;
import com.bluebook.repositories.TestRepository;
import com.bluebook.repositories.UserRepository;
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
    
    private static final String[] bools = {"true", "false"};

    @PostMapping("/new/questions/{testId}/add-multi-choice")
    public Object addMultiChoiceQuestion(@PathVariable int testId, @RequestParam Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;
        

        String questionString = params.get("questionString");
        String correctAnswer = params.get("correctAnswer");

        String incorrectAnswer1 = params.get("incorrectAnswer1");
        String incorrectAnswer2 = params.get("incorrectAnswer2");
        String incorrectAnswer3 = params.get("incorrectAnswer3");
        //these fields are needed for a question
        if(questionString == null || questionString.length() == 0) return false;
        if(correctAnswer == null || correctAnswer.length() == 0) return false;
        if(incorrectAnswer1 == null || incorrectAnswer1.length() == 0) return false;

        int id = 0;
        while(quesRepo.existsById(id)) id++;
        MultiChoiceQuestion question = new MultiChoiceQuestion(id, workingTest, questionString, correctAnswer);

        question.addAnswer(incorrectAnswer1);
        //these are only added if there is actual data there.
        if(incorrectAnswer2.length() != 0) question.addAnswer(incorrectAnswer2);
        if(incorrectAnswer3.length() != 0) question.addAnswer(incorrectAnswer3);

        
        workingTest.getQuestions().add(question);

        quesRepo.save(question);
        testRepo.save(workingTest);
        return true;
    }

    @PostMapping("/new/questions/{testId}/add-true-false")
    public Object addTrueFalse(@PathVariable int testId, @RequestParam Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;

        String questionString = params.get("questionString");
        String correctAnswer = params.get("correctAnswer").toLowerCase();

        //these fields are needed for a question
        if(questionString == null || questionString.length() == 0) return false;
        if(correctAnswer == null || correctAnswer.length() == 0) return false;

        if(!Arrays.stream(bools).parallel().anyMatch(correctAnswer::contains)) return false;

        
        int id = 0;
        while(quesRepo.existsById(id)) id++;
        TrueFalseQuestion question = new TrueFalseQuestion(id, workingTest, questionString, correctAnswer);
        workingTest.getQuestions().add(question);

        quesRepo.save(question);
        testRepo.save(workingTest);
        return true;
    }

    //in the future you can add character different, this is whyu its a seperate method
    @PostMapping("/new/questions/{testId}/add-input")
    public Object addInput(@PathVariable int testId, @RequestParam Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Test workingTest = testRepo.findById(testId);

        if(workingTest == null) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;

        String questionString = params.get("questionString");
        String correctAnswer = params.get("correctAnswer");

        //these fields are needed for a question
        if(questionString == null || questionString.length() == 0) return false;
        if(correctAnswer == null || correctAnswer.length() == 0) return false;
        int id = 0;
        while(quesRepo.existsById(id)) id++;
        InputQuestion question = new InputQuestion(id, workingTest, questionString, correctAnswer);
        workingTest.getQuestions().add(question);

        quesRepo.save(question);
        testRepo.save(workingTest);
        return true;
    }
}