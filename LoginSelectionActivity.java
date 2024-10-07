package com.example.splash_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginselection);

        Button citizen=(Button)findViewById(R.id.USER);
        Button service= (Button)findViewById(R.id.SERVICEWORKER);
        Button muncipality= (Button)findViewById(R.id.muncipality);

        citizen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent activity2Intent = new Intent(getApplicationContext(), LoginForCitizenActivity.class);
                activity2Intent.putExtra("User", "Citizen");
                startActivity(activity2Intent);
            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getApplicationContext(), LoginForCitizenActivity.class);
                activity2Intent.putExtra("User", "Service");
                startActivity(activity2Intent);
            }
        });

        muncipality.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getApplicationContext(), LoginForCitizenActivity.class);
                activity2Intent.putExtra("User", "Municipality");
                startActivity(activity2Intent);
            }
        });
    }

}