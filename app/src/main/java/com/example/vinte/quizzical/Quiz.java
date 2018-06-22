package com.example.vinte.quizzical;

import java.util.ArrayList;
import java.util.List;

public class Quiz {

    private List<Question> questions = new ArrayList();
    private String title;
    private int id;

    public List<Question> getQuestions() {
        return questions;
    }
    public void addQuestion(Question question){
        questions.add(question);
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
}
