package com.example.nik_ax.kproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nik_ax.kproject.xo.FetchData;
import com.example.nik_ax.kproject.xo.entityMarkStud;
import com.example.nik_ax.kproject.xo.entityStatement;
import com.example.nik_ax.kproject.xo.entitySubject;

public class StudMark extends AppCompatActivity {

    String API_PATH = "https://nikaxen.000webhostapp.com/androidproject/";
    String URL="";
    public static entityStatement est;
    public static entitySubject esubj = new entitySubject();

    public static TextView subject_title,statement_title;
    TextView themark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_mark);
        entityMarkStud em = (entityMarkStud) getIntent().getSerializableExtra("MarkItem");
        setTitle("Оценка " + em.getId_mark());
        subject_title = (TextView) findViewById(R.id.tv_stud_subject);
        statement_title = (TextView) findViewById(R.id.tv_stud_statement);
        themark = (TextView) findViewById(R.id.tv_stud_mark);

        Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();

        URL = API_PATH + "api_statement.php?action=select&id_statement=" + em.getId_statement();
        FetchData fd_st = new FetchData();
        fd_st.setTask("SelectStatementForSTUDENT");
        fd_st.execute(URL);
        est = fd_st.entity_statement;

        URL = API_PATH + "api_statement.php?action=select_subject_from_st_for_sm&id_mark=" + em.getId_mark();
        Log.i("App", "URL = " + URL);
        FetchData fd_subj = new FetchData();
        fd_subj.setTask("SelectSubjectForStudMarkEA");
        fd_subj.execute(URL);
        esubj = fd_subj.entity_subject;

        themark.setText(String.valueOf(em.getMark()));

    }
}
