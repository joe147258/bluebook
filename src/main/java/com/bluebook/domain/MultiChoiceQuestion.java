package com.bluebook.domain;

import java.util.ArrayList;
import java.util.Collections;

import javax.persistence.Entity;
import javax.persistence.Lob;


@Entity
public class MultiChoiceQuestion extends TestQuestion {

    @Lob
    ArrayList<String> answerList = new ArrayList<String>();
    
    public MultiChoiceQuestion(){
        //FOR JPA
    }
    public MultiChoiceQuestion(int id, Test test, String question, String correctAnswer){
        this.id = id;
        this.test = test;
        this.question = question;
        this.correctAnswer = correctAnswer;
        answerList.add(correctAnswer);
    }

    public void addAnswer(String s) {
		answerList.add(s);
    }

    public void shuffleAnswers() {
		  Collections.shuffle(answerList);
    }

    public String getAnswer(int i) {
        if(i < 0 || i > answerList.size()) return "OUT_OF_BOUNDS";
        else return answerList.get(i);
    }
    public String[] getIncorrectAnswers() {
        ArrayList<String> incorrectAnswers = new ArrayList<String>();
        for(String s : answerList) {
            if(!s.equals("correctAnswer")) {
                incorrectAnswers.add(s);
            }
        }
        return (String[]) incorrectAnswers.toArray();
    }
}