package com.example.lernapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    String name, email, password;
    TextInputEditText textInputEditTextName, textInputEditTextEmail, textInputEditTextPassword;
    Button buttonSubmit;
    TextView textViewError, textViewLoginNow;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        textInputEditTextName = findViewById(R.id.name);
        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonSubmit = findViewById(R.id.submit);
        textViewError = findViewById(R.id.error);
        textViewLoginNow = findViewById(R.id.loginNow);
        progressBar = findViewById(R.id.loading);

        textViewLoginNow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), com.example.lernapp.Login.class);
            startActivity(intent);
            finish();
        });

        buttonSubmit.setOnClickListener(v -> {
            textViewError.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            name = String.valueOf(textInputEditTextName.getText());
            email = String.valueOf(textInputEditTextEmail.getText());
            password = String.valueOf(textInputEditTextPassword.getText());
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://192.168.178.97/login-registration-android/register.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        progressBar.setVisibility(View.GONE);
                        if (response.equals("success")) {
                            Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), com.example.lernapp.Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            textViewError.setText(response);
                            textViewError.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }, error -> {
                progressBar.setVisibility(View.VISIBLE);
                textViewError.setText(error.getLocalizedMessage());
                textViewError.setVisibility(View.VISIBLE);
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("name", name);
                    paramV.put("email", email);
                    paramV.put("password", password);
                    return paramV;
                }
            };
            queue.add(stringRequest);
        });
    }
}