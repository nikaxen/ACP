package com.example.nik_ax.kproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nik_ax.kproject.xo.FetchData;
import com.example.nik_ax.kproject.xo.MySpinnerData;
import com.example.nik_ax.kproject.xo.entityUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DekActivityUserItem extends AppCompatActivity {
    String API_PATH = "https://nikaxen.000webhostapp.com/androidproject/";
    String URL="";
    TextView id_user, fio, email, tv_st1, tv_st3;
    public static TextView tv_st2;
    Spinner sp_user_studgroup = null;
    Button btnChangeStudGroupForUSER;
    public static TextView rolename;
    String undst_rolename,changeRolename;
    int changeStudGroup;

    public static ArrayAdapter<MySpinnerData> studgroupAdapter;

    public boolean is_student=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dek_user_item);
        entityUser eu = (entityUser) getIntent().getSerializableExtra("UserItem");
        setTitle("Пользователь " + eu.getId_user());

        id_user =(TextView) findViewById(R.id.tv_IDUSER);
        fio =(TextView) findViewById(R.id.tv_USERFIO);
        rolename =(TextView) findViewById(R.id.tv_USERROLENAME);
        email =(TextView) findViewById(R.id.tv_USEREMAIL);


        id_user.setText(String.valueOf(eu.getId_user()));
        fio.setText(eu.getFio());

        if(eu.getRolename().equals("dek")){
            undst_rolename="Сотрудник деканата";
        }
        if(eu.getRolename().equals("prep")){
            undst_rolename="Преподаватель";
        }
        if(eu.getRolename().equals("neizv")){
            undst_rolename="Неизвестный";
        }
        if(eu.getRolename().equals("stud")){
            undst_rolename="Студент";is_student=true;
        }

        rolename.setText(undst_rolename + " (" + eu.getRolename()+")");
        email.setText(eu.getEmail());


        /* spChangeStatus */
        List<MySpinnerData> rolenameList = new ArrayList<>();
        if(!eu.getRolename().equals("dek")){
            MySpinnerData msd = new MySpinnerData(1, "dek");
            rolenameList.add(msd);
        }
        if(!eu.getRolename().equals("prep")){
            MySpinnerData msd = new MySpinnerData(2, "prep");
            rolenameList.add(msd);
        }
        if(!eu.getRolename().equals("stud")){
            MySpinnerData msd = new MySpinnerData(2, "stud");
            rolenameList.add(msd);
        }
        ArrayAdapter<MySpinnerData> rolenameAdapter = new ArrayAdapter<MySpinnerData>(this, android.R.layout.simple_spinner_item, rolenameList);
        rolenameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner SpinnerRolenames = null;
        SpinnerRolenames = (Spinner) findViewById(R.id.spUserChangeUserRole);
        SpinnerRolenames.setAdapter(rolenameAdapter);
        SpinnerRolenames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                Log.i("App", "SELECTED ITEM changeUserRolename");
                MySpinnerData item = (MySpinnerData)parent.getItemAtPosition(position);
                changeRolename=item.getValue();
                Log.i("App", "Rolename selected: " + changeRolename);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        tv_st1 = (TextView) findViewById(R.id.textView24);
        tv_st2 = (TextView) findViewById(R.id.tv_DEK_USER_STUDENTGROUP);
        tv_st3 = (TextView) findViewById(R.id.textView33);
        sp_user_studgroup = (Spinner) findViewById(R.id.sp_user_studgroup);
        btnChangeStudGroupForUSER = (Button) findViewById(R.id.btnChangeStudGroupForUSER);

        if(is_student){
            tv_st1.setVisibility(View.VISIBLE);
            tv_st2.setVisibility(View.VISIBLE);
            tv_st3.setVisibility(View.VISIBLE);
            sp_user_studgroup.setVisibility(View.VISIBLE);
            btnChangeStudGroupForUSER.setVisibility(View.VISIBLE);


            URL = API_PATH + "api_user.php?action=select_user_studgroup&id_user="+ eu.getId_user() +"";


            FetchData fd1 = new FetchData();
            fd1.setTask("Select_User_Studgroup");
            fd1.execute(URL);


            /* spinnerStudGroup */
            URL = API_PATH + "api_user.php?action=select_studgrouplist";


            FetchData fd_studgroup = new FetchData();
            fd_studgroup.setTask("StudGroupUserSelectAll");
            fd_studgroup.execute(URL);

            List<MySpinnerData> studgroupList = fd_studgroup.spinnerList;



            studgroupAdapter = new ArrayAdapter<MySpinnerData>(this, android.R.layout.simple_spinner_item, studgroupList);
            studgroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_user_studgroup.setAdapter(studgroupAdapter);

            studgroupAdapter.notifyDataSetChanged();
            sp_user_studgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                    Log.i("App", "SELECTED ITEM studgroup");
                    MySpinnerData item = (MySpinnerData)parent.getItemAtPosition(position);
                    Log.i("App","studgroup = " + item.getKey() + ", " + item.getValue());

                    changeStudGroup = item.getKey();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
        }



    }


    public void btnAddStudentGroupForUser(View view){
        entityUser eu = (entityUser) getIntent().getSerializableExtra("UserItem");
        URL = API_PATH + "api_user.php?action=add_user_studgroup&id_user="+ eu.getId_user() +"&id_st_gr="+ changeStudGroup +"";
        FetchData fd = new FetchData();
        fd.setTask("ChangeUserStudGroup");
        fd.execute(URL);
        Intent intent = new Intent(this, DekActivity.class);
        intent.putExtra("link_to", (Serializable) "UsersControll");
        finish();
        startActivity(intent);
    }


    public void btnChangeUserRole(View view){
        entityUser eu = (entityUser) getIntent().getSerializableExtra("UserItem");
        URL = API_PATH + "api_user.php?action=change_userrole&id_user="+ eu.getId_user() +"&rolename="+ changeRolename +"";
        Log.i("App", "URL = " + URL);
        FetchData fd = new FetchData();
        fd.setTask("ChangeUserRole");
        fd.execute(URL);
        Intent intent = new Intent(this, DekActivity.class);
        intent.putExtra("link_to", (Serializable) "UsersControll");
        finish();
        startActivity(intent);
    }

}
