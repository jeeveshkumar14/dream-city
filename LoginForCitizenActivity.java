package com.example.splash_screen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginForCitizenActivity extends AppCompatActivity {

    TextView login_type, sign_up;
    Button btnSignIn;
    String value = "null";
    TextInputEditText edtSignInEmail, edtSignInPassword;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_for_citizen);

        login_type = (TextView) findViewById(R.id.loginType);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        sign_up = (TextView) findViewById(R.id.txtSignUp);
        edtSignInEmail = (TextInputEditText) findViewById(R.id.edtSignInEmail);
        edtSignInPassword = (TextInputEditText) findViewById(R.id.edtSignInPassword);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("User");
            login_type.setText(value);
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edtSignInEmail.getText().toString();
                String passord=edtSignInPassword.getText().toString();
                firebaseDatabase=FirebaseDatabase.getInstance();
                databaseReference=firebaseDatabase.getReference(value);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot i : snapshot.getChildren())
                        {
                            String email1=i.child("Email").getValue(String.class);
                            String password1=i.child("Pass").getValue(String.class);
                            if(email.equals(email1) && passord.equals(password1))
                            {
                                Intent intent=new Intent(getApplicationContext(),UserMainActivity.class);
                                intent.putExtra("User",value);
                                intent.putExtra("Email",email1);
                                intent.putExtra("Phone",i.getKey().toString());
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                databaseReference.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                        for(DataSnapshot i : snapshot.getChildren())
//                        {
//                            Toast.makeText(getApplicationContext(),i.child("Email").getValue(String.class),Toast.LENGTH_LONG).show();
////                            String email1=i.child("Email").getValue(String.class);
////                            String password1=i.child("Pass").getValue(String.class);
////                            if(email.equals(email1) && passord.equals(password1))
////                            {
////                                Intent intent=new Intent(getApplicationContext(),UserMainActivity.class);
////                                startActivity(intent);
////                            }
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                Log.e("Test","size = " +
//                        Main_prefence.getInstance().registerdata.size());
//                if (validateFields()) {
//                    if (Main_prefence.getInstance().registerdata.size() != 0) {
//                        for (int i = 0; i < Main_prefence.getInstance().registerdata.size(); i++) {
//
//                            Log.e("Test","size = " +
//                                    Main_prefence.getInstance().registerdata.get(i).getUsername() + " , " +
//                                    Main_prefence.getInstance().registerdata.get(i).getPassword() + " , " +
//                                    Main_prefence.getInstance().registerdata.get(i).getLogin_type());
//
//                            if (Objects.requireNonNull(Main_prefence.getInstance().registerdata.get(i).getLogin_type())
//                                    .equalsIgnoreCase(login_type.getText().toString())) {
//
//                                if (Objects.requireNonNull(Main_prefence.getInstance().registerdata.get(i).getPassword())
//                                        .equalsIgnoreCase(edtSignInPassword.getText().toString())) {
//
//                                    if (Objects.requireNonNull(Main_prefence.getInstance().registerdata.get(i).getUsername())
//                                            .equalsIgnoreCase(edtSignInEmail.getText().toString())) {
//
//                                        Main_prefence.getInstance().user_first_name = edtSignInEmail.getText().toString();
//                                        Main_prefence.getInstance().user_login_type = login_type.getText().toString();
//                                        Main_prefence.getInstance().user_login = "Login";
//
//                                        Intent activity2Intent = new Intent(LoginForCitizenActivity.this, UserMainActivity.class);
//                                        activity2Intent.putExtra("User", login_type.getText().toString());
//                                        startActivity(activity2Intent);
//
//                                    }else {
//                                        Toast.makeText(LoginForCitizenActivity.this,
//                                                "Given UserName and Password is Mismatch", Toast.LENGTH_LONG).show();
//                                    }
//                                } else {
//                                    Toast.makeText(LoginForCitizenActivity.this,
//                                            "Given UserName and Password is Mismatch", Toast.LENGTH_LONG).show();
//                                }
//
//                            } else {
//                                Toast.makeText(LoginForCitizenActivity.this,
//                                        "Selected Type Login Type are Different", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    } else {
//                        Toast.makeText(
//                                LoginForCitizenActivity.this,
//                                "Please Register our app", Toast.LENGTH_LONG).show();
//                    }
//                }
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity2Intent = new Intent(LoginForCitizenActivity.this, RegisterActivity.class);
                activity2Intent.putExtra("User",value);
                startActivity(activity2Intent);
            }
        });
    }

    private boolean validateFields() {
        boolean status = true;
        if (edtSignInEmail.getText().toString().equals("")) {
            Toast.makeText(
                    this,
                    "Enter Full Name",
                    Toast.LENGTH_LONG
            ).show();
            status = false;
        }
        else if (edtSignInPassword.getText().toString().equals("")) {
            Toast.makeText(
                    this,
                    "Enter Password",
                    Toast.LENGTH_LONG
            ).show();
            status = false;
        }else {
            status = true;
        }
        return status;
    }


}
/**
 * if (login_type.getText().toString().equals("Citizen Login")) {
 * Intent activity2Intent = new Intent(LoginForCitizenActivity.this, UserMainActivity.class);
 * activity2Intent.putExtra("Login_type", "Citizen Login");
 * startActivity(activity2Intent);
 * }
 * else if (login_type.getText().toString().equals("Service Worker Login")) {
 * Intent activity2Intent = new Intent(LoginForCitizenActivity.this, UserMainActivity.class);
 * activity2Intent.putExtra("Login_type", "Service Worker Login");
 * startActivity(activity2Intent);
 * }
 * else if (login_type.getText().toString().equals("Municipality Login")) {
 * Intent activity2Intent = new Intent(LoginForCitizenActivity.this, UserMainActivity.class);
 * activity2Intent.putExtra("Login_type", "Municipality Login");
 * startActivity(activity2Intent);
 * }
 */