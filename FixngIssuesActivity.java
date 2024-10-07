package com.example.splash_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FixngIssuesActivity extends AppCompatActivity {

    String user,image,fetchlocation,land,datentime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixng_issues);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("User");
            image = extras.getString("Image");
            fetchlocation=extras.getString("FetchLocation");
            land=extras.getString("landmarks");
            datentime=extras.getString("date_time");
            //imageview.stimage bitmap(image);
            //fetchlocationview.stimage bitmap(fetchlocation);
            //landmarkview.stimage bitmap(land);
            //datentimeview.stimage bitmap(datentime);


            //service worker stauts;
        }
    }
}