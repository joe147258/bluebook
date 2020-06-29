package com.bluebook.domain;

import javax.persistence.Entity;

@Entity
public class InputQuestion extends TestQuestion {
    public InputQuestion() {

    }

    public InputQuestion(int id, Test test, String question, String correctAnswer) {
        this.id = id;
        this.test = test;
        this.question = question;
        this.correctAnswer = correctAnswer;
    }
    
}