package com.bluebook.domain;



import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.ManyToOne;

@Entity
public abstract class TestQuestion {
    @Id
    protected int id;
    protected String question;
    protected String correctAnswer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    protected Test test;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getQuestionType() {
        return this.getClass().getName();
    }
    public String getFormattedQuestionType() {
        switch(this.getClass().getName()){
            case "com.bluebook.domain.MultiChoiceQuestion":
                return "Multiple Choice Question";
            case "com.bluebook.domain.InputQuestion":    
                return "Input Question";
            case "com.bluebook.domain.TrueFalseQuestion":
                return "True / false";
            default:
                return "Unknown...";
        }
    }

    public int getQuestionNumber() {

        return 1;
    }

}