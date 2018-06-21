package com.example.vinte.quizzical;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private boolean userAnswer;
    private boolean questionAnswered = false;
    private Quiz quiz;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @BindView(R.id.trueButton) RadioButton trueRadioButton;
    @BindView(R.id.falseButton) RadioButton falseRadioButton;
    @BindView(R.id.next_button) Button nextButton;
    @BindView(R.id.radioGroup) RadioGroup radioGroup;
    @BindView(R.id.answer_text) TextView answerTextView;
    @BindView(R.id.question_text) TextView questionTextView;

    @OnClick(R.id.trueButton) void clickTrue() {
        checkAnswer(true);
    }

    @OnClick(R.id.falseButton) void clickFalse() {
        checkAnswer(false);
    }

    @OnClick(R.id.next_button) void clickNext() {
        currentQuestionIndex++;
        if(currentQuestionIndex >= quiz.getQuestions().size()){
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(ResultActivity.KEY_SCORE, score);
            intent.putExtra(ResultActivity.KEY_TOTAL_QUESTIONS, quiz.getQuestions().size());
            startActivity(intent);
            finish();
        } else {
            questionAnswered = false;
            radioGroup.clearCheck();
            trueRadioButton.setEnabled(true);
            falseRadioButton.setEnabled(true);
            showQuestion();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(savedInstanceState != null){
            questionAnswered = savedInstanceState.getBoolean(QUESTION_ANSWERED, false);
            userAnswer = savedInstanceState.getBoolean(USER_ANSWER, false);
            currentQuestionIndex = savedInstanceState.getInt(CURRENT_QUESTION_INDEX, -1);
            score = savedInstanceState.getInt(SCORE, -1);
        }

        quiz = new QuizRepository(this).getQuiz();
        showQuestion();

        if (questionAnswered){
            checkAnswer(userAnswer);
        }
    }

    private void showQuestion(){
        Question question = quiz.getQuestions().get(currentQuestionIndex);
        questionTextView.setText(question.getStatement());
        answerTextView.setText("");
        nextButton.setEnabled(false);
    }

    private void checkAnswer(boolean answerToCheck){
        userAnswer = answerToCheck;
        Question question = quiz.getQuestions().get(currentQuestionIndex);

        if(answerToCheck == question.getAnswer()){
            answerTextView.setText("YAYYYY!");
            if (!questionAnswered) score++;
        } else {
            answerTextView.setText("Wrong answer.");
        }
        nextButton.setEnabled(true);
        trueRadioButton.setEnabled(false);
        falseRadioButton.setEnabled(false);
        questionAnswered = true;
    }


    private static final String USER_ANSWER = "user answer";
    private static final String QUESTION_ANSWERED = "question answered";
    private static final String CURRENT_QUESTION_INDEX = "current question index";
    private static final String SCORE = "score";

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(USER_ANSWER, userAnswer);
        bundle.putBoolean(QUESTION_ANSWERED, questionAnswered);
        bundle.putInt(CURRENT_QUESTION_INDEX, currentQuestionIndex);
        bundle.putInt(SCORE, score);
    }
}
