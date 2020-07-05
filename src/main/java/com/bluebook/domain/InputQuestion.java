package com.bluebook.domain;

import javax.persistence.Entity;

@Entity
public class InputQuestion extends TestQuestion {
    private int distance;
    public InputQuestion() {

    }

    public InputQuestion(int id, Test test, String question, String correctAnswer, int distance) {
        this.id = id;
        this.test = test;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    
}