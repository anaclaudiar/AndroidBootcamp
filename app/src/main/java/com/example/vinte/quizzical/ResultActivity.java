package com.example.vinte.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends AppCompatActivity{

    public static final String KEY_SCORE = "score";
    public static final String KEY_TOTAL_QUESTIONS = "total";

    @BindView(R.id.reset) Button resetButton;
    @BindView(R.id.imageResult) ImageView imageView;
    @BindView(R.id.result_text) TextView resultText;

    @OnClick(R.id.reset) void clickReset() {
        Intent intent = new Intent(ResultActivity.this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int score = intent.getIntExtra(KEY_SCORE, -1);
        int totalQuestions = intent.getIntExtra(KEY_TOTAL_QUESTIONS, -1);

        SetFinalImage(score, totalQuestions);

        String result = String.format("%d", (int)(((double)score/(double)totalQuestions)*100)) + "%";
        resultText.setText(getString(R.string.result) + result);
    }

    private void SetFinalImage(int score, int totalQuestions) {
        if (score >= totalQuestions) {
            Glide.with(this).load("https://media.giphy.com/media/RrVzUOXldFe8M/giphy.gif").into(imageView);
        } else if (score > 0) {
            Glide.with(this).load("https://media.giphy.com/media/RN9wo1TPU0e0wm4a7l/giphy.gif").into(imageView);
        } else {
            Glide.with(this).load("https://media.giphy.com/media/gGxSl050qQOJi/giphy.gif").into(imageView);
        }
    }

}
