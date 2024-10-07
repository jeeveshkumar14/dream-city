package com.example.splash_screen;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main_prefence {

    final String PREF_NAME = "Main_prefence";

    public String user_login_type = "";
    public String user_first_name = "";
    public String user_login = "";

    public ArrayList<RegisterDataModel> registerdata = new ArrayList<>();
    public ArrayList<AddedIssuesDataModel> issuesdata = new ArrayList<>();


    SharedPreferences.Editor editor;
    static Main_prefence _instance = new Main_prefence();

    public static Main_prefence getInstance() {
        return _instance;
    }

    public boolean savepref (Context contextain) {
        editor = contextain.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        return editor.commit();
    }

    public void loadPrefs(Context ctx) {
        SharedPreferences p = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }

    public boolean removePrefs(Context ctx) {
        editor = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        return editor.commit();
    }
}
