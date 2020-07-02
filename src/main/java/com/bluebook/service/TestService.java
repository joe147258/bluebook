package com.bluebook.service;

import java.util.Arrays;

import com.bluebook.domain.Classroom;
import com.bluebook.domain.CustomUser;
import com.bluebook.domain.Test;
import com.bluebook.repositories.TestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TestService {

    @Autowired
    TestRepository testRepo;

    /**
     * There are three types of tests that are planned to be implenented the purpose
     * of the array is to ensure the user doesn't bug the server. The three types
     * are: -END_FEEDBACK (feedback is given after the user has completed the quiz)
     * -INSTANT_FEEDBACK (feedback is given as soon as the question is answered)
     * -MARKED (questions are marked by the teacher and feedback is given then)
     */
    private static final String[] validTypes = { "END_FEEDBACK", "INSTANT_FEEDBACK", "MARKED_FEEDBACK" };

    public final Boolean changeFeedbackType(final Test workingTest, final String newFbType) {
        //TODO: check if quiz is published before doing this (after publishing implemented) disallowed if published
        if(!Arrays.stream(validTypes).parallel().anyMatch(newFbType::contains)) 
            return false; 

        workingTest.setFeedbackType(newFbType);
        testRepo.save(workingTest);
        return true;
    }   
    /**
     * 
     * @param classroom the classroom the test belongs to
     * @param name The name of the test
     * @param type  the type of feedback the test has
     * @param owner the owner of the test
     * @return returns the class id if it is successfully create, otherwise it returns -1
     */
    public final int createNewTest(final Classroom classroom, final String name, final String type, final CustomUser owner) {

        if(!Arrays.stream(validTypes).parallel().anyMatch(type::contains)) 
            return -1; 

        if(name.contains(";")) return -1; 
        
        int id = 0;
        while(testRepo.existsById(id)) id++;
        Test newTest = new Test(id, name, type, owner, classroom);
        testRepo.save(newTest);

        return id;
    }
    /**
     * 
     * @param test the test to be changed
     * @param newTitle the new title
     * @return true if successful, else false
     */
    public final Boolean updateTestTitle(final Test test, final String newTitle) {
        if(newTitle.contains(";")) return false;
        test.setName(newTitle);
        testRepo.save(test);
        return true;
    }


}