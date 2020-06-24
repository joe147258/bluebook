package uol.bluebook.domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class MultipleChoiceQuestion extends TestQuestion {

    @Lob
    List<String> answerList = new ArrayList<String>();

    public MultipleChoiceQuestion(){
        //FOR JPA
    }
    public MultipleChoiceQuestion(int id, Test test, String question, String correctAnswer){
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
}