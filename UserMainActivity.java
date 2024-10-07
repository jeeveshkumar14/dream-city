package com.example.splash_screen;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserMainActivity extends AppCompatActivity
        implements Added_issues_adapter.OnItemClickListener {

    ImageView ic_add,ic_setting;
    TextView tv_username,no_data;
    TextView tv_Present,tv_Leaves;
    Added_issues_adapter added_issues_adapter;
    RecyclerView rv_doctor_list;
    String value = "null";
    TextView tv_Absent;
    String email="null";
    CardView attended_overview_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("User");
            email = extras.getString("Email");
        }
        tv_Leaves = findViewById(R.id.tv_Leaves);
        tv_Absent = findViewById(R.id.tv_Absent);
        tv_Present = findViewById(R.id.tv_Present);
        ic_add = findViewById(R.id.ic_add);
        ic_setting = findViewById(R.id.ic_setting);
        tv_username = findViewById(R.id.tv_username);
        no_data = findViewById(R.id.no_data);
        rv_doctor_list = findViewById(R.id.rv_doctor_list);
        attended_overview_card = findViewById(R.id.attended_overview_card);
        FirebaseDatabase.getInstance().getReference(value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    if (i.child("Email").getValue().toString().equals(email)) {
                        tv_username.setText(i.child("Name").getValue().toString());
                        tv_Present.setText(i.child("Count").getValue().toString());
                        tv_Absent.setText(i.child("Solve").getValue().toString());
                        tv_Leaves.setText(i.child("Work").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity2Intent = new Intent(UserMainActivity.this, AddIssuesActivity.class);
                activity2Intent.putExtra("User", value);
                activity2Intent.putExtra("Email", email);
                startActivity(activity2Intent);
            }
        });
        ic_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(UserMainActivity.this)
                        .setIcon(R.drawable.baseline_report_gmailerrorred_24)
                        .setTitle("Alert")
                        .setMessage("If you want to logout?")
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Main_prefence.getInstance().user_login = "failed";
                                Intent a = new Intent(UserMainActivity.this, LoginSelectionActivity.class);
                                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);
                            }
                        })
                        .show();
            }
        });
        ArrayList<AddedIssuesDataModel> arrayList=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Issues").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot i : snapshot.getChildren())
                {
                    AddedIssuesDataModel addedIssuesDataModel=new AddedIssuesDataModel();
                    addedIssuesDataModel.setUsername("USER");
                    addedIssuesDataModel.setDate_time(i.child("Date").getValue().toString());
                    Bitmap img=getConvert(i.child("Image").getValue().toString());
                    addedIssuesDataModel.setImage(img);
                    addedIssuesDataModel.setFetched_location(i.getKey().toString());
                    addedIssuesDataModel.setLandmark(i.child("Land").getValue().toString());
                    arrayList.add(addedIssuesDataModel);
                }
                        added_issues_adapter = new Added_issues_adapter(UserMainActivity.this,
                        arrayList);
                        rv_doctor_list.setAdapter(added_issues_adapter);
                        added_issues_adapter.setOnItemClickListener(UserMainActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        added_issues_adapter = new Added_issues_adapter(UserMainActivity.this,
//                Main_prefence.getInstance().issuesdata);
//        rv_doctor_list.setAdapter(added_issues_adapter);
//        added_issues_adapter.setOnItemClickListener(this);

        if (value.equalsIgnoreCase("Citizen Login")) {
            ic_add.setVisibility(View.VISIBLE);
            attended_overview_card.setVisibility(View.GONE);
            if (Main_prefence.getInstance().issuesdata.size() == 0) {
                rv_doctor_list.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
                no_data.setText("No cleaning data has added here! Click add button to create a new issues");
            }else {
                rv_doctor_list.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.GONE);
            }

        }else if (value.equalsIgnoreCase("Service Worker Login")) {
            ic_add.setVisibility(View.GONE);

        }else if (value.equalsIgnoreCase("Municipality Login")) {
            ic_add.setVisibility(View.GONE);
            attended_overview_card.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap getConvert(String image) {
        byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override

    public void onBackPressed() {
//        super.onBackPressed();
        UserMainActivity.this.finish();
        finishAffinity();
    }

    @Override
    public void onclick(int position) {
        Intent intent =  new Intent(UserMainActivity.this,FixngIssuesActivity.class);
        intent.putExtra("Image", Main_prefence.getInstance().issuesdata.get(position).getImage());
        intent.putExtra("FetchLocation", Main_prefence.getInstance().issuesdata.get(position).getFetched_location());
        intent.putExtra("landmarks", Main_prefence.getInstance().issuesdata.get(position).getLandmark());
        intent.putExtra("date_time", Main_prefence.getInstance().issuesdata.get(position).getDate_time());
        intent.putExtra("User", Main_prefence.getInstance().issuesdata.get(position).getLogin_type());
        startActivity(intent);
    }
}