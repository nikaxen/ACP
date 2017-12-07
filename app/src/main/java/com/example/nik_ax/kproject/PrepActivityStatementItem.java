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

public class PrepActivityStatementItem extends AppCompatActivity {

    String API_PATH = "https://nikaxen.000webhostapp.com/androidproject/";
    String URL="";


    public static entitySubject esubj;
    public static entityStudgroup esg;
    public static entityUser eUser = new entityUser();

    TextView id_statement, title;

    public static TextView subject;
    public static TextView student_group;


    public static ArrayAdapter<MySpinnerData> studsinstudgroupAdapter;
    public static ArrayAdapter<entityMark> marksAdapter;


    public int selectedMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prep_statement_item);
        setTitle("Ведомость");
        entityStatement es = (entityStatement) getIntent().getSerializableExtra("StatementItem");
        id_statement =(TextView) findViewById(R.id.tv_PREP_IDSTATEMENT);
        title =(TextView) findViewById(R.id.tv_PREP_TITLESTATEMENT);
        subject =(TextView) findViewById(R.id.tv_PREPSUBJECT);
        student_group =(TextView) findViewById(R.id.tv_PREPSTUDGROUP);
        Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();






        URL = API_PATH + "api_subject.php?action=select&id_subject=" + es.getId_subject();
        FetchData fd_subj = new FetchData();
        fd_subj.setTask("Select_subject_prep");
        fd_subj.execute(URL);
        esubj = fd_subj.entity_subject;

        URL = API_PATH + "api_user.php?action=select_studgroup&grid=" + es.getGrid();
        FetchData fd_studgroup = new FetchData();
        fd_studgroup.setTask("Select_studgroup_prep");
        fd_studgroup.execute(URL);
        esg = fd_studgroup.entity_studgroup;


        id_statement.setText(String.valueOf(es.getId_statement()));
        title.setText(es.getTitle());


        subject.setText(esubj.getSubject_title());
        student_group.setText(esg.getSgname());








        /* Spinner StudsInStudGroup */
        URL = API_PATH + "api_user.php?action=select_studs_in_studgroup&id_statement=" + es.getId_statement();


        FetchData fd = new FetchData();
        fd.setTask("StudsInStudGroup_SelectAll");
        fd.execute(URL);

        List<MySpinnerData> stinstgrList = fd.spinnerList;


        studsinstudgroupAdapter = new ArrayAdapter<MySpinnerData>(this, android.R.layout.simple_spinner_item, stinstgrList);
        studsinstudgroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner SpinnerStudInStudGroup = null;
        SpinnerStudInStudGroup = (Spinner) findViewById(R.id.spStudInStudGroup);
        SpinnerStudInStudGroup.setAdapter(studsinstudgroupAdapter);
        studsinstudgroupAdapter.notifyDataSetChanged();
        SpinnerStudInStudGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                Log.i("App", "SELECTED ITEM stud");
                MySpinnerData item = (MySpinnerData)parent.getItemAtPosition(position);
                Log.i("App","item stud = " + item.getKey() + ", " + item.getValue());
                eUser.setId_user(item.getKey());

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });







        /* Spinner Marks */
        List<MySpinnerData> defaultMarksList = new ArrayList<>();
            MySpinnerData msd = new MySpinnerData(1, "5");
            defaultMarksList.add(msd);
            MySpinnerData msd1 = new MySpinnerData(1, "4");
            defaultMarksList.add(msd1);
            MySpinnerData msd2 = new MySpinnerData(1, "3");
            defaultMarksList.add(msd2);
            MySpinnerData msd3 = new MySpinnerData(1, "2");
            defaultMarksList.add(msd3);
            MySpinnerData msd4 = new MySpinnerData(1, "1");
            defaultMarksList.add(msd4);


        ArrayAdapter<MySpinnerData> defaultMarksAdapter = new ArrayAdapter<MySpinnerData>(this, android.R.layout.simple_spinner_item, defaultMarksList);
        defaultMarksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner SpinnerDefaultMarks = null;
        SpinnerDefaultMarks = (Spinner) findViewById(R.id.spDefaultMARKS);
        SpinnerDefaultMarks.setAdapter(defaultMarksAdapter);
        SpinnerDefaultMarks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                Log.i("App", "SELECTED ITEM defaultMarks");
                MySpinnerData item = (MySpinnerData)parent.getItemAtPosition(position);
                selectedMark=Integer.valueOf(item.getValue());
                Log.i("App", "Status selected: " + selectedMark);
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
        FetchData fd = new FetchData();
        fd.setTask("SelectAllMarksInStatement");
        URL = API_PATH + "api_user.php?action=select_marks_in_st&id_statement="+es.getId_statement();
        fd.execute(URL);

        List<entityMark> ent_em_List = fd.entityMarkList;
        ListView MarksStatementsList = (ListView) findViewById(R.id.listViewMarksInStatement);
        marksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ent_em_List);
        MarksStatementsList.setAdapter(marksAdapter);

        MarksStatementsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                entityMark em = (entityMark) parent.getAdapter().getItem(position);
                Log.i("App", "item id = " + em.getId_mark());

                GoToPrepMark(em);
            }
        });
    }


    public void GoToPrepMark(entityMark em){
        entityStatement es = (entityStatement) getIntent().getSerializableExtra("StatementItem");
        Intent intent = new Intent(this, PrepMark.class);
        intent.putExtra("link_to", (Serializable) "PrepMark");
        intent.putExtra("MarkID", (Serializable) em.getId_mark());
        intent.putExtra("TheMark", (Serializable) em.getMark());
        intent.putExtra("StatementItem", (Serializable) es);
        startActivity(intent);
    }


    public void MakeStatementReady(View view){
        entityStatement es = (entityStatement) getIntent().getSerializableExtra("StatementItem");
        URL = API_PATH + "api_statement.php?action=change_statement_status&id_statement=" + es.getId_statement() +"&status=ready";
        FetchData fd = new FetchData();
        fd.setTask("MakeStatementReady");
        fd.execute(URL);
        Intent intent = new Intent(this, PrepActivity.class);
        intent.putExtra("link_to", (Serializable) "PrepStatements");
        finish();
        startActivity(intent);
    }



    public void AddMarkToStatement(View view){
        entityStatement es = (entityStatement) getIntent().getSerializableExtra("StatementItem");
        URL = API_PATH + "api_user.php?action=add_mark&id_statement=" + es.getId_statement() +"&id_user="+ eUser.getId_user() +"&mark=" + selectedMark;
        FetchData fd = new FetchData();
        fd.setTask("AddMarkToStatement");
        fd.execute(URL);
        Intent intent = new Intent(this, PrepActivity.class);
        intent.putExtra("link_to", (Serializable) "SelectedStatement");
        intent.putExtra("the_statement", (Serializable) es);
        finish();
        startActivity(intent);
    }
}
