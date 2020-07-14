package com.bluebook.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

    public List<String> getIncorrectAnswers() {
        List<String> incorrectAnswers = new LinkedList<String>();
        for(String s : answerList) {
            if(!s.equalsIgnoreCase(correctAnswer)) { 
                incorrectAnswers.add(s);
            }
        }
        return incorrectAnswers;
    }
    public void clearAnswerList() {
        answerList.clear();
    }
    public ArrayList<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(ArrayList<String> answerList) {
        this.answerList = answerList;
    }
}