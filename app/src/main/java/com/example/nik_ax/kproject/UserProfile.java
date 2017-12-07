package com.example.nik_ax.kproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {

    private static final String PREFS_FILE = "Account";
    private static final String PREF_IDUSER = "ID_USER";
    private static final String PREF_FIO = "FIO";
    private static final String PREF_ROLENAME = "ROLENAME";
    private static final String PREF_EMAIL = "EMAIL";
    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;

    TextView tv_fio, tv_rolename, tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);

        tv_fio = (TextView) findViewById(R.id.textView11);
        tv_rolename = (TextView) findViewById(R.id.textView15);
        tv_email = (TextView) findViewById(R.id.textView13);


        int id_user = settings.getInt(PREF_IDUSER, 0);
        String fio = settings.getString(PREF_FIO, "");
        String rolename = settings.getString(PREF_ROLENAME, "");
        String email = settings.getString(PREF_EMAIL, "");

        String int_rolename = "undefined";

        if(rolename.equals("stud")){
            int_rolename = "Студент (stud)";
        }
        if(rolename.equals("prep")){
            int_rolename = "Преподаватель (prep)";
        }
        if(rolename.equals("dek")){
            int_rolename = "Сотрудник деканата (dek)";
        }
        if(rolename.equals("neizv")){
            int_rolename = "Неизвестный (neizv)";
        }

        tv_fio.setText(fio);
        tv_rolename.setText(int_rolename);
        tv_email.setText(email);

        setTitle("Мой профиль " + id_user);
    }

    public void AccountLogOut(View view){
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
