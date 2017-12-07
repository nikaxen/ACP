package com.example.nik_ax.kproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nik_ax.kproject.xo.FetchData;
import com.example.nik_ax.kproject.xo.entityStatement;
import com.example.nik_ax.kproject.xo.entitySubject;

import java.io.Serializable;

public class PrepMark extends AppCompatActivity {

    String API_PATH = "https://nikaxen.000webhostapp.com/androidproject/";
    String URL="";
    public static entitySubject esubj;
    public static TextView subject_title;
    TextView statement_title, themark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prep_mark);
        entityStatement es = (entityStatement) getIntent().getSerializableExtra("StatementItem");
        int mark_id = (int) getIntent().getSerializableExtra("MarkID");
        int mark_mark = (int) getIntent().getSerializableExtra("TheMark");
        setTitle("Оценка " + mark_id);

        subject_title = (TextView) findViewById(R.id.tvPrepM_Subject);
        statement_title = (TextView) findViewById(R.id.tvPrepM_Statement);
        themark = (TextView) findViewById(R.id.tvPrepM_Mark);

        Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();

        URL = API_PATH + "api_subject.php?action=select&id_subject=" + es.getId_subject();
        FetchData fd_subj = new FetchData();
        fd_subj.setTask("Select_subject_prep_mark");
        fd_subj.execute(URL);
        esubj = fd_subj.entity_subject;


        subject_title.setText(esubj.getSubject_title());
        statement_title.setText(es.getTitle());
        themark.setText(String.valueOf(mark_mark));
    }

    public void RemoveMark(View view){
        int mark_id = (int) getIntent().getSerializableExtra("MarkID");
        entityStatement es = (entityStatement) getIntent().getSerializableExtra("StatementItem");
        URL = API_PATH + "api_statement.php?action=remove_mark&id_mark=" + mark_id;
        FetchData fd_subj = new FetchData();
        fd_subj.setTask("RemoveMark");
        fd_subj.execute(URL);
        Intent intent = new Intent(this, PrepActivity.class);
        intent.putExtra("link_to", (Serializable) "SelectedStatement");
        intent.putExtra("the_statement", (Serializable) es);
        finish();
        startActivity(intent);
    }
}
