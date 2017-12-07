package com.example.nik_ax.kproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nik_ax.kproject.xo.FetchData;
import com.example.nik_ax.kproject.xo.entityMarkStud;

import java.io.Serializable;
import java.util.List;

public class StudActivity extends AppCompatActivity {

    String API_PATH = "https://nikaxen.000webhostapp.com/androidproject/";
    String URL="";
    entityMarkStud ems;
    public static ArrayAdapter<entityMarkStud> marksAdapter;

    int stud_id=0;

    private static final String PREFS_FILE = "Account";
    private static final String PREF_IDUSER = "ID_USER";
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud);
        setTitle("Все мои оценки");
        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        int id_user = settings.getInt(PREF_IDUSER, 0);
        stud_id=id_user;
        ShowAllMarksForStudent(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stud_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nav_stud_allmarks:
                setContentView(R.layout.activity_stud);
                setTitle("Все мои оценки");
                ShowAllMarksForStudent(1);

                return true;
            case R.id.nav_stud_badmarks:
                setContentView(R.layout.activity_stud);
                setTitle("Неудовлетворительные оценки");
                ShowAllMarksForStudent(2);

                return true;
            case R.id.nav_profile:
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void ShowAllMarksForStudent(int mode){
        Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();
        FetchData fd = new FetchData();
        fd.setTask("GetMarksForStudent");
        if(mode==1){
            URL = API_PATH + "api_statement.php?action=select_marks_for_stud&id_user="+stud_id;
        }else{
            URL = API_PATH + "api_statement.php?action=select_badmarks_for_stud&id_user="+stud_id;
        }
        fd.execute(URL);

        List<entityMarkStud> ent_ems_List = fd.entityMarkStudList;
        ListView MarksStudList = (ListView) findViewById(R.id.StudMarksList);
        marksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ent_ems_List);
        MarksStudList.setAdapter(marksAdapter);

        MarksStudList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                entityMarkStud em = (entityMarkStud) parent.getAdapter().getItem(position);
                Log.i("App", "item id = " + em.getId_mark());
                StudGoToMark(em);
            }
        });
    }

    public void StudGoToMark(entityMarkStud em){
        Intent intent = new Intent(this, StudMark.class);
        intent.putExtra("MarkItem", (Serializable) em);
        startActivity(intent);
    }
}
