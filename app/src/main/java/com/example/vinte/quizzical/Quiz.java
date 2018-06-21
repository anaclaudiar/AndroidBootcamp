package com.example.vinte.quizzical;

import java.util.ArrayList;
import java.util.List;

public class Quiz {

    private List<Question> questions = new ArrayList();

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

    private static Quiz quiz;
    public static Quiz getInstance(){
        if (quiz == null){
            quiz = new Quiz();
            quiz.addQuestion(new Question("The moon is made of cheese", false));
            quiz.addQuestion(new Question("The sum of a triangle's internal angles are 180", true));
        }
        return quiz;
    }
}
