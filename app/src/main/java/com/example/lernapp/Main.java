package com.example.lernapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;

public class Main extends AppCompatActivity {
    TextView textViewName, textViewEmail, textViewFetchResult;
    SharedPreferences sharedPreferences;
    Button buttonLogout, buttonFetchUser, buttonHome;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        textViewName = findViewById(R.id.name);
        textViewEmail = findViewById(R.id.email);
        textViewFetchResult = findViewById(R.id.fetchResult);

        buttonLogout = findViewById(R.id.logout);
        buttonFetchUser = findViewById(R.id.fetchProfile);

        // Get the shared preferences
        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println(item.getItemId());
                if (item.getItemId() == R.id.test) {
                    Intent intent = new Intent(getApplicationContext(), com.example.lernapp.Test.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.ranking) {
                    Intent intent1 = new Intent(getApplicationContext(), com.example.lernapp.Ranking.class);
                    startActivity(intent1);
                    finish();
                    return true;
                } else return false;

            }
        });

        // Check if user is logged in, if not, redirect to login screen
        if (sharedPreferences.getString("logged", "false").equals("false")) {
            Intent intent = new Intent(getApplicationContext(), com.example.lernapp.Login.class);
            startActivity(intent);
            finish();
        }

        // Set the name and email in the text views
        textViewName.setText(sharedPreferences.getString("name", ""));
        textViewEmail.setText(sharedPreferences.getString("email", ""));

        // Logout button click listener
        buttonLogout.setOnClickListener(v -> {
            // Create a request queue
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://10.1.7.16/login-registration-android/logout.php";

            // Create a StringRequest to send a POST request to the logout URL
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        if (response.equals("success")) {
                            // Clear the shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("logged", "");
                            editor.putString("name", "");
                            editor.putString("email", "");
                            editor.putString("apiKey", "");
                            editor.apply();

                            // Redirect to login screen
                            Intent intent = new Intent(getApplicationContext(), com.example.lernapp.Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Show an error message using a toast
                            Toast.makeText(Main.this, response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    Throwable::printStackTrace) {
                // Define the POST parameters
                protected Map<String, String> getParams() {
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("email", sharedPreferences.getString("email", ""));
                    paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));
                    return paramV;
                }
            };
            queue.add(stringRequest);
        });

        // Fetch user button click listener
        buttonFetchUser.setOnClickListener(v -> {
            // Create a request queue
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://192.168.178.21/login-registration-android/profile.php";

            // Create a StringRequest to send a POST request to the profile URL
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        // Display the fetched result in a text view
                        textViewFetchResult.setText(response);
                        textViewFetchResult.setVisibility(View.VISIBLE);
                    },
                    Throwable::printStackTrace) {
                // Define the POST parameters
                protected Map<String, String> getParams() {
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("email", sharedPreferences.getString("email", ""));
                    paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));
                    paramV.put("userId", sharedPreferences.getString("userId", ""));
                    return paramV;
                }
            };
            queue.add(stringRequest);
        });



    }
}
