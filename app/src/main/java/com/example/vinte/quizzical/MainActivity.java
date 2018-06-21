package com.example.vinte.quizzical;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String USER_ANSWER = "user answer";
    private static final String QUESTION_ANSWER = "question answer";

    private RadioButton trueRadioButton;
    private RadioButton falseRadioButton;
    private TextView answerTextView;

    private boolean userAnswer;
    private boolean questionAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        answerTextView = findViewById(R.id.answer_text);
        trueRadioButton = findViewById(R.id.trueButton);

        trueRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("HI", "True radio button clicked");
                checkAnswer(true);
            }
        });

        falseRadioButton = findViewById(R.id.falseButton);
        falseRadioButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("HI", "False radio button clicked");
                checkAnswer(false);
            }
        });

        if(savedInstanceState != null){
            questionAnswered = savedInstanceState.getBoolean(QUESTION_ANSWER, false);
            userAnswer = savedInstanceState.getBoolean(USER_ANSWER, false);
        }

        if (questionAnswered){
            checkAnswer(userAnswer);
        }
    }

    private void checkAnswer(boolean answerToCheck){
        questionAnswered = true;
        userAnswer = answerToCheck;

        if(answerToCheck == false){
            answerTextView.setText(R.string.false_string);
        } else {
            answerTextView.setText(R.string.true_string);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(USER_ANSWER, userAnswer);
        bundle.putBoolean(QUESTION_ANSWER, questionAnswered);
    }

}
