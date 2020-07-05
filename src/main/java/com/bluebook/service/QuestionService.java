package com.bluebook.service;

import java.util.Arrays;

import com.bluebook.domain.InputQuestion;
import com.bluebook.domain.MultiChoiceQuestion;
import com.bluebook.domain.Test;
import com.bluebook.domain.TrueFalseQuestion;
import com.bluebook.repositories.ClassroomRepository;
import com.bluebook.repositories.QuestionRepository;
import com.bluebook.repositories.TestRepository;
import com.bluebook.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service class is used when creating new questions.
 */
@Service
public class QuestionService {
    
    @Autowired
    TestRepository testRepo;
    @Autowired
    ClassroomRepository classRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    QuestionRepository quesRepo;

    private static final String[] bools = {"true", "false"};

    /**
     * 
     * @param workingTest The test the question is going to be added to.
     * @param questionString The question the user will see
     * @param correctAnswer The correct answer
     * @param incorrectAnswers an array of incorrect answers. Has to contain at least one item in position [0]
     * @return  returns true if successfully creates and adds the question. Otherwise returns false.
     * This method returns false if it fails to create and save the question
     * Otherwise, it returns true
     */
    public final Boolean createMultiChoiceQuestion(final Test workingTest, final String questionString, 
        final String correctAnswer, final String[] incorrectAnswers) {

        //These are essential for a multiple choice.
        if(questionString == null || questionString.length() == 0) return false;
        if(correctAnswer == null || correctAnswer.length() == 0) return false;
        if(incorrectAnswers[0] == null || incorrectAnswers[0].length() == 0) return false;

        if(correctAnswer.contains(";") 
            || questionString.contains(";") 
            || incorrectAnswers[0].contains(";")) {
            return false;
        } 

        int id = 0;
        while(quesRepo.existsById(id)) id++;
        MultiChoiceQuestion question = new MultiChoiceQuestion(id, workingTest, 
            questionString, correctAnswer);

        question.addAnswer(incorrectAnswers[0]);
        //these are only added if there is actual data contained in the array.
        if(incorrectAnswers[1] != null || incorrectAnswers[1].length() != 0) {
            if(incorrectAnswers[1].contains(";")) return false;
            question.addAnswer(incorrectAnswers[1]);
        }
            

        if(incorrectAnswers[2] != null || incorrectAnswers[2].length() != 0) {
            if(incorrectAnswers[2].contains(";")) return false;
            question.addAnswer(incorrectAnswers[2]);
        }
            

        workingTest.getQuestions().add(question);
        quesRepo.save(question);
        testRepo.save(workingTest);

        return true;
    }
    /**
     * @param workingTest The test the question is going to be added to.
     * @param questionString The question the user will see
     * @param correctAnswer The correct answer. For this it should be "true" or "false" or 
     * else it'll return false
     * @return  returns true if successfully creates and adds the question. Otherwise returns false.
     */
    public final Boolean createTrueFalseQuestion(final Test workingTest, final String questionString, 
        final String correctAnswer) {

        if(questionString == null || questionString.length() == 0) return false;
        if(correctAnswer == null || correctAnswer.length() == 0) return false;
        if(!Arrays.stream(bools).parallel().anyMatch(correctAnswer::contains)) return false;
        if(correctAnswer.contains(";") || questionString.contains(";")) 
            return false;
        

        int id = 0;
        while(quesRepo.existsById(id)) id++;
        TrueFalseQuestion question = new TrueFalseQuestion(id, workingTest, questionString, correctAnswer);

        workingTest.getQuestions().add(question);
        quesRepo.save(question);
        testRepo.save(workingTest);
        
        return true;
    }

    /**
     * @param workingTest The test the question is going to be added to.
     * @param questionString The question the user will see
     * @param correctAnswer The correct answer
     * @param distance This is how different the user's answer can be from correct answer. It
     * uses levenshteins algorithm. TODO: Implement this.
     * @return  returns true if successfully creates and adds the question. Otherwise returns false.
     */
    public final Boolean createInputQuestion(final Test workingTest, final String questionString, 
        final String correctAnswer, final int distance) {

        if(questionString == null || questionString.length() == 0) return false;
        if(correctAnswer == null || correctAnswer.length() == 0) return false;
        if(correctAnswer.contains(";") || questionString.contains(";")) 
            return false;

        int id = 0;
        while(quesRepo.existsById(id)) id++;
        InputQuestion question = new InputQuestion(id, workingTest, questionString, correctAnswer);

        workingTest.getQuestions().add(question);
        quesRepo.save(question);
        testRepo.save(workingTest);
        
        return true;
    }
}