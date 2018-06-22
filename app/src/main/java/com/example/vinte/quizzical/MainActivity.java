package com.example.vinte.quizzical;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements QuizRepository.QuizCallback{

    public static final String KEY_QUESTION_ID = "key_question_id";
    private boolean userAnswer;
    private boolean questionAnswered = false;
    private Quiz quiz;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @BindView(R.id.trueButton) RadioButton trueRadioButton;
    @BindView(R.id.falseButton) RadioButton falseRadioButton;
    @BindView(R.id.next_button) Button nextButton;
    @BindView(R.id.radioGroup) RadioGroup radioGroup;
    @BindView(R.id.imageQuestionResult) ImageView imageView;
    @BindView(R.id.question_text) TextView questionTextView;
    @BindView(R.id.determinateBar) ProgressBar progressBar;

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

        Intent intent = getIntent();
        int id = intent.getIntExtra(KEY_QUESTION_ID, -1);
        new QuizRepository(this).getRemoteQuiz(id,this);
    }

    private void showQuestion(){
        Question question = quiz.getQuestions().get(currentQuestionIndex);
        questionTextView.setText(question.getStatement());
        imageView.setVisibility(View.INVISIBLE);
        nextButton.setEnabled(false);
    }

    private void checkAnswer(boolean answerToCheck){
        userAnswer = answerToCheck;
        Question question = quiz.getQuestions().get(currentQuestionIndex);
        imageView.setVisibility(View.VISIBLE);
        if(answerToCheck == question.getAnswer()){
            Glide.with(this).load("http://www.clipartbest.com/cliparts/RTA/Eoz/RTAEozeyc.png").into(imageView);
            if (!questionAnswered) score++;
        } else {
            Glide.with(this).load("https://cdn1.iconfinder.com/data/icons/basic-ui-icon-rounded-colored/512/icon-02-512.png").into(imageView);
        }
        nextButton.setEnabled(true);
        trueRadioButton.setEnabled(false);
        falseRadioButton.setEnabled(false);
        questionAnswered = true;

        progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);

        progressBar.setProgress( (int)(((double)(currentQuestionIndex + 1) / (float) quiz.getQuestions().size())*100) );
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

    @Override
    public void onFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Unable to fetch quiz", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSuccess(final Quiz quiz) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.quiz = quiz;
                showQuestion();

                if (questionAnswered){
                    checkAnswer(userAnswer);
                }
            }
        });
    }
}
