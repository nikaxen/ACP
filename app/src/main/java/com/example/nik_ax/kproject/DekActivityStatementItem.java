package com.example.nik_ax.kproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nik_ax.kproject.xo.FetchData;
import com.example.nik_ax.kproject.xo.MySpinnerData;
import com.example.nik_ax.kproject.xo.entityMark;
import com.example.nik_ax.kproject.xo.entityStatement;
import com.example.nik_ax.kproject.xo.entityStudgroup;
import com.example.nik_ax.kproject.xo.entitySubject;
import com.example.nik_ax.kproject.xo.entityUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DekActivityStatementItem extends AppCompatActivity {

    String API_PATH = "https://nikaxen.000webhostapp.com/androidproject/";
    String URL="";
    public static entityUser eu;
    public static entitySubject esubj;
    public static entityStudgroup esg;


    TextView id_statement, title, status;

    public static ArrayAdapter<entityMark> marksAdapter;

    public static TextView prep;
    public static TextView subject;
    public static TextView student_group;

    public String changedStatus="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_statements_item);
        setTitle("Ведомость");
        entityStatement es = (entityStatement) getIntent().getSerializableExtra("StatementItem");
        Log.i("App", "es status =" + es.getStatus());
        id_statement =(TextView) findViewById(R.id.tvID_STATEMENT);
        title =(TextView) findViewById(R.id.tvSTATEMENT_TITLE);
        status =(TextView) findViewById(R.id.tvSTATUS);
        prep =(TextView) findViewById(R.id.tvPREP);
        subject =(TextView) findViewById(R.id.tvSUBJECT);
        student_group =(TextView) findViewById(R.id.tvSTUDGROUP);

        Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();

        URL = API_PATH + "api_user.php?action=select&id_user=" + es.getId_user();
        FetchData fd_prep = new FetchData();
        fd_prep.setTask("Select_prep");
        fd_prep.execute(URL);
        eu = fd_prep.entity_prep;


        URL = API_PATH + "api_subject.php?action=select&id_subject=" + es.getId_subject();
        FetchData fd_subj = new FetchData();
        fd_subj.setTask("Select_subject");
        fd_subj.execute(URL);
        esubj = fd_subj.entity_subject;

        URL = API_PATH + "api_user.php?action=select_studgroup&grid=" + es.getGrid();
        FetchData fd_studgroup = new FetchData();
        fd_studgroup.setTask("Select_studgroup");
        fd_studgroup.execute(URL);
        esg = fd_studgroup.entity_studgroup;


        id_statement.setText(String.valueOf(es.getId_statement()));
        title.setText(es.getTitle());

        prep.setText(eu.getFio());
        subject.setText(esubj.getSubject_title());
        student_group.setText(esg.getSgname());


        if(es.getStatus().equals("opened")){status.setText("ОТКРЫТА");}
        if(es.getStatus().equals("closed")){status.setText("ЗАКРЫТА");}
        if(es.getStatus().equals("ready")){status.setText("НА РАССМОТРЕНИИ");}

        /* spChangeStatus */
        List<MySpinnerData> statusesList = new ArrayList<>();
        if(!es.getStatus().equals("opened")){
            MySpinnerData msd = new MySpinnerData(1, "opened");
            statusesList.add(msd);
        }
        if(!es.getStatus().equals("closed")){
            MySpinnerData msd = new MySpinnerData(2, "closed");
            statusesList.add(msd);
        }

        ArrayAdapter<MySpinnerData> statusesAdapter = new ArrayAdapter<MySpinnerData>(this, android.R.layout.simple_spinner_item, statusesList);
        statusesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner SpinnerStatuses = null;
        SpinnerStatuses = (Spinner) findViewById(R.id.spChangeStatementStatus);
        SpinnerStatuses.setAdapter(statusesAdapter);
        SpinnerStatuses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                Log.i("App", "SELECTED ITEM changeStatementStatus");
                MySpinnerData item = (MySpinnerData)parent.getItemAtPosition(position);
                changedStatus=item.getValue();
                Log.i("App", "Status selected: " + changedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ShowAllMarksInStatement();
    }


    public void ShowAllMarksInStatement(){
        entityStatement es = (entityStatement) getIntent().getSerializableExtra("StatementItem");
        Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();
        FetchData fd_m = new FetchData();
        fd_m.setTask("SelectAllMarksInStatementDek");
        URL = API_PATH + "api_user.php?action=select_marks_in_st&id_statement="+es.getId_statement();
        fd_m.execute(URL);

        List<entityMark> ent_em_List = fd_m.entityMarkList;
        ListView MarksStatementsList = (ListView) findViewById(R.id.listViewMarksInStatementDek);
        marksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ent_em_List);
        MarksStatementsList.setAdapter(marksAdapter);

        MarksStatementsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                entityMark em = (entityMark) parent.getAdapter().getItem(position);
                Log.i("App", "item id = " + em.getMark());

            }
        });
    }

    public void btnChangeStatementStatus(View view){
        entityStatement es = (entityStatement) getIntent().getSerializableExtra("StatementItem");
        URL = API_PATH + "api_statement.php?action=change_statement_status&id_statement="+ es.getId_statement() +"&status="+ changedStatus +"";
        FetchData fd_studgroup = new FetchData();
        fd_studgroup.setTask("ChangeStatementStatus");
        fd_studgroup.execute(URL);
        Intent intent = new Intent(this, DekActivity.class);
        intent.putExtra("link_to", (Serializable) "DekStatements");
        finish();
        startActivity(intent);
    }
}
