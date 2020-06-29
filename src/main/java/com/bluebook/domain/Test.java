package com.bluebook.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Test {
    @Id
    @Column(name="test_id")
    private int id;
    private String name;
    private String feedbackType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private CustomUser testOwner;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<TestQuestion> questions = new ArrayList<TestQuestion>();

    private Boolean published;
    
    public Test() {

    }
    public Test(int id, String name, String feedbackType, CustomUser testOwner, Classroom classroom) {
        this.id = id;
        this.name = name;
        this.feedbackType = feedbackType;
        this.testOwner = testOwner;
        this.classroom = classroom;
        this.published = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public CustomUser getTestOwner() {
        return testOwner;
    }

    public void setTestOwner(CustomUser testOwner) {
        this.testOwner = testOwner;
    }

    public List<TestQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<TestQuestion> questions) {
        
        this.questions = questions;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }


}