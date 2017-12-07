package com.example.nik_ax.kproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nik_ax.kproject.xo.FetchData;
import com.example.nik_ax.kproject.xo.entityMark;
import com.example.nik_ax.kproject.xo.entityStatement;
import com.jjoe64.graphview.GraphView;

import java.util.List;

public class PrepGraphItem extends AppCompatActivity {


    int prep_id=0;

    String API_PATH = "https://nikaxen.000webhostapp.com/androidproject/";
    String URL="";

    public static ArrayAdapter<entityMark> marksAdapter;

    private static final String PREFS_FILE = "Account";
    private static final String PREF_IDUSER = "ID_USER";
    public static GraphView graph;
    SharedPreferences settings;

    public static ArrayAdapter<entityStatement> statementsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entityStatement es = (entityStatement) getIntent().getSerializableExtra("StatementItem");
        setContentView(R.layout.activity_prep_graph_item);

        setTitle("Ведомость " + es.getId_statement());
        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        int id_user = settings.getInt(PREF_IDUSER, 0);
        prep_id=id_user;
        graph = (GraphView) findViewById(R.id.graph);
        CreateGraphForPrep();
    }

    public void CreateGraphForPrep(){


        entityStatement es = (entityStatement) getIntent().getSerializableExtra("StatementItem");
        Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();
        FetchData fd = new FetchData();
        fd.setTask("SelectAllMarksInStatementGraph");
        URL = API_PATH + "api_user.php?action=select_marks_in_st&id_statement="+es.getId_statement();
        fd.execute(URL);
        Log.i("App", URL);
        List<entityMark> ent_em_List = fd.entityMarkList;
        ListView MarksStatementsList = (ListView) findViewById(R.id.LVSudentAndID);
        marksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ent_em_List);
        MarksStatementsList.setAdapter(marksAdapter);

    }
}
