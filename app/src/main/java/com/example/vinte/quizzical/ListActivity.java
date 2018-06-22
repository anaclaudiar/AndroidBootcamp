package com.example.vinte.quizzical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ListActivity extends AppCompatActivity implements QuizRepository.QuizesCallback{

    private ListView listView;
    private QuizAdapter quizAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.list_view);
        new QuizRepository(this).getRemoteQuizes(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Quiz quiz = quizAdapter.getItem(position);
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.KEY_QUESTION_ID, quiz.getId());
                startActivity(intent);

            }
        });


    }

    @Override
    public void onFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ListActivity.this, "Unable to fetch quiz", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSuccess(final List<Quiz> quizzes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                quizAdapter = new QuizAdapter(ListActivity.this, quizzes);
                listView.setAdapter(quizAdapter);
            }
        });
    }


    static class QuizAdapter extends ArrayAdapter<Quiz> {

        public QuizAdapter(@NonNull Context context, @NonNull List<Quiz> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Quiz quiz = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            TextView textView = (TextView) convertView;
            textView.setText(quiz.getTitle());

            return textView;
        }
    }

}
