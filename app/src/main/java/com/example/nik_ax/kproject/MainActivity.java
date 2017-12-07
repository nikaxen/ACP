package com.example.nik_ax.kproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity {

    private static final String PREFS_FILE = "Account";
    private static final String PREF_IDUSER = "ID_USER";
    private static final String PREF_FIO = "FIO";
    private static final String PREF_ROLENAME = "ROLENAME";
    private static final String PREF_EMAIL = "EMAIL";
    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        int id_user = settings.getInt(PREF_IDUSER, 0);
        String rolename = settings.getString(PREF_ROLENAME, "");

        if(id_user==0){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("link_to", (Serializable) "Login");
            finish();
            startActivity(intent);
        }else{
            if(rolename.equals("stud")){
                Intent intent = new Intent(this, StudActivity.class);
                intent.putExtra("link_to", (Serializable) "StudMarks");
                finish();
                startActivity(intent);
            }
            if(rolename.equals("dek")){
                Intent intent = new Intent(this, DekActivity.class);
                intent.putExtra("link_to", (Serializable) "DekStatements");
                finish();
                startActivity(intent);
            }
            if(rolename.equals("prep")){
                Intent intent = new Intent(this, PrepActivity.class);
                intent.putExtra("link_to", (Serializable) "PrepStatements");
                finish();
                startActivity(intent);
            }
            if(rolename.equals("neizv")){
                setTitle("Внимание!");
                setContentView(R.layout.neizv_user_notification);
            }

        }
    }
    public void AccountLogOut2(View view){
        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putInt(PREF_IDUSER, 0);
        prefEditor.putString(PREF_FIO, "");
        prefEditor.putString(PREF_ROLENAME, "");
        prefEditor.putString(PREF_EMAIL, "");
        prefEditor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}
