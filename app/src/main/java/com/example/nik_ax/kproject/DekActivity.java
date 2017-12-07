package com.example.nik_ax.kproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nik_ax.kproject.xo.FetchData;
import com.example.nik_ax.kproject.xo.MySpinnerData;
import com.example.nik_ax.kproject.xo.entityStatement;
import com.example.nik_ax.kproject.xo.entityUser;

import java.io.Serializable;
import java.util.List;

public class DekActivity extends AppCompatActivity {

    String API_PATH = "https://nikaxen.000webhostapp.com/androidproject/";
    String URL="";
    EditText etStatementTitle;
    Button btnCreateStatement;

    public static ArrayAdapter<entityStatement> statementsAdapter;
    public static ArrayAdapter<MySpinnerData> subjectAdapter;
    public static ArrayAdapter<MySpinnerData> prepAdapter;
    public static ArrayAdapter<MySpinnerData> studgroupAdapter;
    public static ArrayAdapter<entityUser> usersAdapter;

    entityStatement entityStatement = new entityStatement();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dek);
        if(getIntent().getSerializableExtra("link_to").equals("UsersControll")){
            ShowAllUsersList();
        }else{
            ShowAllStatementsList(1);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dek_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dek_create_statement:
                setContentView(R.layout.layout_dek_create_statement);
                setTitle("Создать ведомость");

                etStatementTitle = (EditText) findViewById(R.id.etStatementTitle);

                /* spinners filling */
                /* spinnerSubject */
                Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();
                URL = API_PATH + "api_subject.php?action=select_all";


                FetchData fd = new FetchData();
                fd.setTask("Subjects_SelectAll");
                fd.execute(URL);

                List<MySpinnerData> subjectList = fd.spinnerList;



                subjectAdapter = new ArrayAdapter<MySpinnerData>(this, android.R.layout.simple_spinner_item, subjectList);
                subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner SpinnerSubject = null;
                SpinnerSubject = (Spinner) findViewById(R.id.spinnerSubject);
                SpinnerSubject.setAdapter(subjectAdapter);

                subjectAdapter.notifyDataSetChanged();
                SpinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                        Log.i("App", "SELECTED ITEM subject");
                        MySpinnerData item = (MySpinnerData)parent.getItemAtPosition(position);
                        Log.i("App","item subject = " + item.getKey() + ", " + item.getValue());
                        entityStatement.setId_subject(item.getKey());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

                /* spinnerPrep */
                URL = API_PATH + "api_user.php?action=select_preplist";


                FetchData fd_prep = new FetchData();
                fd_prep.setTask("Preps_SelectAll");
                fd_prep.execute(URL);

                List<MySpinnerData> prepList = fd_prep.spinnerList;



                prepAdapter = new ArrayAdapter<MySpinnerData>(this, android.R.layout.simple_spinner_item, prepList);
                prepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner SpinnerPrep = null;
                SpinnerPrep = (Spinner) findViewById(R.id.spinnerPrep);
                SpinnerPrep.setAdapter(prepAdapter);

                prepAdapter.notifyDataSetChanged();
                SpinnerPrep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                        Log.i("App", "SELECTED ITEM prep");
                        MySpinnerData item = (MySpinnerData)parent.getItemAtPosition(position);
                        Log.i("App","prep subject = " + item.getKey() + ", " + item.getValue());
                        entityStatement.setId_user(item.getKey());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
                /* spinnerStudGroup */
                URL = API_PATH + "api_user.php?action=select_studgrouplist";


                FetchData fd_studgroup = new FetchData();
                fd_studgroup.setTask("Studgroup_SelectAll");
                fd_studgroup.execute(URL);

                List<MySpinnerData> studgroupList = fd_studgroup.spinnerList;



                studgroupAdapter = new ArrayAdapter<MySpinnerData>(this, android.R.layout.simple_spinner_item, studgroupList);
                studgroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner SpinnerStudGroup = null;
                SpinnerStudGroup = (Spinner) findViewById(R.id.spinnerStudGroup);
                SpinnerStudGroup.setAdapter(studgroupAdapter);

                studgroupAdapter.notifyDataSetChanged();
                SpinnerStudGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                        Log.i("App", "SELECTED ITEM studgroup");
                        MySpinnerData item = (MySpinnerData)parent.getItemAtPosition(position);
                        Log.i("App","studgroup = " + item.getKey() + ", " + item.getValue());
                        entityStatement.setGrid(item.getKey());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });



                return true;
            case R.id.nav_dek_all_statements:
                ShowAllStatementsList(1);

                return true;
            case R.id.nav_profile:
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
                return true;
            case R.id.nav_dek_ready_statements:
                ShowAllStatementsList(2);
                setTitle("Сданные ведомости");
                return true;
            case R.id.nav_dek_users_controll:
                ShowAllUsersList();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void ShowAllUsersList(){
        setTitle("Пользователи");
        setContentView(R.layout.activity_dek_userscontroll);
        Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();
        FetchData fd_userscontroll = new FetchData();
        fd_userscontroll.setTask("Users_SelectAll");
        URL = API_PATH + "api_user.php?action=select_all";
        fd_userscontroll.execute(URL);

        List<entityUser> ent_us_List = fd_userscontroll.entityUsersList;
        ListView UsersList = (ListView) findViewById(R.id.UsersControll);
        usersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ent_us_List);
        UsersList.setAdapter(usersAdapter);

        UsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                entityUser us = (entityUser) parent.getAdapter().getItem(position);
                Log.i("App", "item id = " + us.getId_user() + us.getFio());
                GoToUserItem(us);
            }
        });
    }

    public void ShowAllStatementsList(int mode){
        setContentView(R.layout.activity_dek);
        setTitle("Все ведомости");
        Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();
        if(mode ==1){
            URL = API_PATH + "api_statement.php?action=select_all";
        }
        if(mode ==2){
            URL = API_PATH + "api_statement.php?action=select_ready";
        }

        FetchData fd_statements = new FetchData();
        fd_statements.setTask("Statements_SelectAll");
        fd_statements.execute(URL);

        List<entityStatement> ent_st_List = fd_statements.entityStatementsList;
        ListView StatementsList = (ListView) findViewById(R.id.Statementslist);
        statementsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ent_st_List);
        StatementsList.setAdapter(statementsAdapter);
/*


*/


        StatementsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                entityStatement es = (entityStatement) parent.getAdapter().getItem(position);
                Log.i("App", "item id = " + es.getId_statement() + es.getTitle() + es.getTitle());
                GoToStatementItem(es);
            }
        });
    }



    public void GoToStatementItem(entityStatement es){
        Intent intent = new Intent(this, DekActivityStatementItem.class);
        intent.putExtra("StatementItem", (Serializable) es);
        startActivity(intent);
    }

    public void GoToUserItem(entityUser us){
        Intent intent = new Intent(this, DekActivityUserItem.class);
        intent.putExtra("UserItem", (Serializable) us);
        startActivity(intent);
    }

    public void btnCreateNewStatement(View view){
        String str_etStatementTitle = etStatementTitle.getText().toString();
        str_etStatementTitle = str_etStatementTitle.replaceAll("\\s+","_");
        entityStatement.setStatus("opened");
        URL = API_PATH + "api_statement.php?action=create_new_statement&id_subject=" + entityStatement.id_subject + "&id_user=" + entityStatement.id_user + "&grid=" + entityStatement.grid + "&title=" + str_etStatementTitle;


        FetchData fd_cns = new FetchData();
        fd_cns.setTask("CreateNewStatement");
        fd_cns.execute(URL);
        Log.i("App","CREATE NEW STATEMENT BTN CLICKED");
        ShowAllStatementsList(1);

    }



}
