package com.example.vinte.quizzical;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Okio;

public class QuizRepository {

    private Context context;

    public QuizRepository(Context context) {
        this.context = context;
    }

    public Quiz getQuiz(){
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open("quiz.json");
        } catch (IOException e) {
            Log.e("QuizRepo", "unable to open quiz.json", e);
            return null;
        }
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Quiz> jsonAdapter = moshi.adapter(Quiz.class);

        try {
            Quiz quiz = jsonAdapter.fromJson(Okio.buffer(Okio.source(inputStream)));
            return quiz;
        } catch (IOException e) {
            Log.e("QuizRepo", "unable to open quiz.json", e);
            return null;
        }
    }

    public void getRemoteQuiz(int id, final QuizCallback quizCallback){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://oolong.tahnok.me/cdn/quizzes/" + id + ".json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                quizCallback.onFailure();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!response.isSuccessful()){
                    quizCallback.onFailure();
                }

                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<Quiz> jsonAdapter = moshi.adapter(Quiz.class);
                Quiz quiz = jsonAdapter.fromJson(response.body().source());
                quizCallback.onSuccess(quiz);
            }
        });
    }

    public interface QuizCallback {
        void onFailure();
        void onSuccess(Quiz quiz);
    }

    public void getRemoteQuizes(final QuizesCallback quizesCallback){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://oolong.tahnok.me/cdn/quizzes.json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                quizesCallback.onFailure();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!response.isSuccessful()){
                    quizesCallback.onFailure();
                }

                Moshi moshi = new Moshi.Builder().build();
                Type type = Types.newParameterizedType(List.class, Quiz.class);

                JsonAdapter<List<Quiz>> jsonAdapter = moshi.adapter(type);
                List<Quiz> quizes = jsonAdapter.fromJson(response.body().source());

                quizesCallback.onSuccess(quizes);
            }
        });
    }

    public interface QuizesCallback {
        void onFailure();
        void onSuccess(List<Quiz> quizes);
    }
}
