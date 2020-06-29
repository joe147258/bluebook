package com.bluebook.domain;

import javax.persistence.Entity;

@Entity
public class TrueFalseQuestion extends TestQuestion{



    public TrueFalseQuestion() {

    }

    public TrueFalseQuestion(int id, Test test, String question, String correctAnswer) {
        this.id = id;
        this.test = test;
        this.question = question;
        this.correctAnswer = correctAnswer;
    }
}