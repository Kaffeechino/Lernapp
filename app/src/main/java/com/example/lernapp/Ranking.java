package com.example.lernapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class Ranking extends AppCompatActivity {
    ListView listView;
    String[] ranks;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        System.out.println("start");
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println(item.getItemId());

                if (item.getItemId() == R.id.test) {
                    Intent intent = new Intent(getApplicationContext(), com.example.lernapp.Test.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.home) {
                    Intent intent1 = new Intent(getApplicationContext(), com.example.lernapp.Main.class);
                    startActivity(intent1);
                    finish();
                    return true;
                } else return false;

                }

        });
        System.out.println("weiter");


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://10.1.7.16/login-registration-android/ranking.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        ranks = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                ranks[i] = jsonArray.getString(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                Ranking.this,
                                android.R.layout.simple_list_item_1,
                                ranks
                        );
                        listView.setAdapter(adapter);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }, error -> {
        });
        queue.add(stringRequest);

        listView = findViewById(R.id.listView);
        System.out.println("nach der Query");

    }
}