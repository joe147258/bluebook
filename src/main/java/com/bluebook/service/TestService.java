package com.bluebook.service;

import java.util.Arrays;

import com.bluebook.domain.Test;
import com.bluebook.repositories.TestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TestService {

    @Autowired
    TestRepository testRepo;

    private static final String[] validTypes = { "END_FEEDBACK", "INSTANT_FEEDBACK", "MARKED_FEEDBACK" };

    public final Boolean changeFeedbackType(final Test workingTest, final String newFbType) {
        if(!Arrays.stream(validTypes).parallel().anyMatch(newFbType::contains)) 
            return false; 
            
        workingTest.setFeedbackType(newFbType);
        testRepo.save(workingTest);
        return true;
    }   
}