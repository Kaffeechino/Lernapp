package com.example.lernapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Test extends AppCompatActivity {

    RadioButton answer1RadioButton, answer2RadioButton, answer3RadioButton, answer4RadioButton;
    Button buttonGetQuestion, buttonSendAnswer;
    TextView textViewQuestion, textViewTest;
    String selectedAnswer, email;
    int selectedButtonId, questionId;
    SharedPreferences sharedPreferences;
    JSONArray answersArray;
    String myString;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "false");

        textViewQuestion = findViewById(R.id.questionTextView);
        textViewTest = findViewById(R.id.test);
        buttonGetQuestion = findViewById(R.id.buttonGetQuestion);
        buttonSendAnswer = findViewById(R.id.buttonSendAnswer);

        RadioGroup radioGroup = findViewById(R.id.answersBox);
        answer1RadioButton = findViewById(R.id.answer1RadioButton);
        answer2RadioButton = findViewById(R.id.answer2RadioButton);
        answer3RadioButton = findViewById(R.id.answer3RadioButton);
        answer4RadioButton = findViewById(R.id.answer4RadioButton);

        ShapeDrawable shapeDrawable0 = new ShapeDrawable();
        ShapeDrawable shapeDrawable1 = new ShapeDrawable();
        ShapeDrawable shapeDrawable2 = new ShapeDrawable();

        shapeDrawable0.getPaint().setColor(Color.WHITE);
        shapeDrawable1.getPaint().setColor(Color.GREEN);
        shapeDrawable2.getPaint().setColor(Color.RED);

        String email = sharedPreferences.getString("email", "false");


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println(item.getItemId());

                if (item.getItemId() == R.id.ranking) {
                    Intent intent = new Intent(getApplicationContext(), com.example.lernapp.Ranking.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.home) {
                    Intent intent1 = new Intent(getApplicationContext(), com.example.lernapp.Test.class);
                    startActivity(intent1);
                    finish();
                    return true;
                } else return false;

            }
        });

        buttonGetQuestion.setOnClickListener(v -> {
            answer1RadioButton.setBackground(shapeDrawable0);
            answer2RadioButton.setBackground(shapeDrawable0);
            answer3RadioButton.setBackground(shapeDrawable0);
            answer4RadioButton.setBackground(shapeDrawable0);
            buttonSendAnswer.setVisibility(View.VISIBLE);
            textViewQuestion.setVisibility(View.VISIBLE);
            textViewTest.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);


            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://10.1.7.16/login-registration-android/getQuestions.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject questionObject = jsonObject.getJSONObject("question");
                            String questionText = (String) questionObject.get("questionText");
                            questionId = (int) questionObject.get("questionID");
                            System.out.println(questionId);
                            textViewQuestion.setText(questionText);
                            String questionId = String.valueOf(questionObject.get("questionID"));
                            textViewTest.setText(questionId);
                            answersArray = jsonObject.getJSONArray("answers");
                            answer1RadioButton.setText(answersArray.getJSONArray(0).getString(1));
                            answer2RadioButton.setText(answersArray.getJSONArray(1).getString(1));
                            answer3RadioButton.setText(answersArray.getJSONArray(2).getString(1));
                            answer4RadioButton.setText(answersArray.getJSONArray(3).getString(1));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }, error -> Log.e("Error", error.getLocalizedMessage())) {
                protected Map<String, String> getParams() {
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("userId", sharedPreferences.getString("userId", ""));
                    return paramV;
                }
            };
            queue.add(stringRequest);
        });

        buttonSendAnswer.setOnClickListener(v -> {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://10.1.7.16/login-registration-android/checkAnswer.php";

            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                try {
                    if (answersArray.getJSONArray(i).getString(2).equals("1")) {
                        radioButton.setBackground(shapeDrawable1);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
                            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                            try {
                                if (answersArray.getJSONArray(i).getString(2).equals("1")) {
                                    radioButton.setBackground(shapeDrawable1);
                                }
                                if (radioButton.isChecked() && answersArray.getJSONArray(i).getString(2).equals("0")) {
                                    radioButton.setBackground(shapeDrawable2);
                                }
                                System.out.println(radioButton.isSelected());
                                System.out.println(radioButton.hasSelection());
                                System.out.println(answersArray.getJSONArray(i).getString(2));

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        buttonSendAnswer.setVisibility(View.GONE);
                    }, error -> Log.e("Error", error.getLocalizedMessage())) {
                protected Map<String, String> getParams() {
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("data", selectedAnswer);
                    paramV.put("id", String.valueOf(questionId));
                    paramV.put("email", email);
                    return paramV;
                }
            };
            queue.add(stringRequest);
        });


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = findViewById(checkedId);
            selectedAnswer = selectedRadioButton.getText().toString();
            selectedButtonId = selectedRadioButton.getId();
        });

    }

}