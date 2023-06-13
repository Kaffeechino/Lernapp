package com.example.lernapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class Login extends AppCompatActivity {

    // Declare variables
    TextView textViewError; // Displays error messages
    ProgressBar progressBar; // Shows a loading progress
    String name, email, password, apiKey, userId; // Variables for user data

    TextView textViewRegisterNow; // TextView to navigate to registration activity
    TextInputEditText textInputEditTextEmail, textInputEditTextPassword; // Text input fields for email and password
    Button buttonSubmit; // Button to submit login form
    SharedPreferences sharedPreferences; // Stores shared preferences for the app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        textViewRegisterNow = findViewById(R.id.registerNow);
        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonSubmit = findViewById(R.id.submit);
        textViewError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);

        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.getString("logged", "false").equals("true")) {
            // Redirect to main activity
            Intent intent = new Intent(getApplicationContext(), Main.class);
            startActivity(intent);
            finish();
        }

        buttonSubmit.setOnClickListener(v -> {
            // Handle button click event
            textViewError.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            // Retrieve user input values
            email = String.valueOf(textInputEditTextEmail.getText());
            password = String.valueOf(textInputEditTextPassword.getText());

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://10.1.7.16/login-registration-android/login.php";

            // Make a POST request to the login API
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("success")) {
                                // Login successful
                                name = jsonObject.getString("name");
                                email = jsonObject.getString("email");
                                apiKey = jsonObject.getString("apiKey");
                                userId = jsonObject.getString("userId");

                                // Store user data in shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("logged", "true");
                                editor.putString("name", name);
                                editor.putString("email", email);
                                editor.putString("apiKey", apiKey);
                                editor.putString("userId", userId);
                                editor.apply();

                                // Redirect to main activity
                                Intent intent = new Intent(getApplicationContext(), Main.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Login failed
                                textViewError.setText(message);
                                textViewError.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    error -> {
                        // Handle API request error
                        progressBar.setVisibility(View.GONE);
                        textViewError.setText(error.getLocalizedMessage());
                        textViewError.setVisibility(View.VISIBLE);
                    }) {
                protected Map<String, String> getParams() {
                    // Set request parameters (email and password)
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("email", email);
                    paramV.put("password", password);
                    return paramV;
                }
            };

            // Add the request to the request queue
            queue.add(stringRequest);
        });

        textViewRegisterNow.setOnClickListener(v -> {
            // Handle register now text view click event
            // Redirect to registration activity
            Intent intent = new Intent(getApplicationContext(), Registration.class);
            startActivity(intent);
            finish();
        });
    }

}
