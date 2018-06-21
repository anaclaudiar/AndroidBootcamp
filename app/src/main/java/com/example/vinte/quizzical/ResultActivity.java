package com.example.vinte.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends AppCompatActivity{

    public static final String KEY_SCORE = "score";
    public static final String KEY_TOTAL_QUESTIONS = "total";

    @BindView(R.id.reset) Button resetButton;

    @OnClick(R.id.reset) void clickReset() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private TextView resultText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        ButterKnife.bind(this);

        resultText = findViewById(R.id.result_text);

        Intent intent = getIntent();
        int score = intent.getIntExtra(KEY_SCORE, -1);
        int totalQuestions = intent.getIntExtra(KEY_TOTAL_QUESTIONS, -1);

        String result = String.format("%d / %d", score, totalQuestions);
        resultText.setText(result);
    }

}
