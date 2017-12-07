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
import com.example.nik_ax.kproject.xo.entityStatement;

import java.io.Serializable;
import java.util.List;

public class PrepActivity extends AppCompatActivity {

    int prep_id=0;

    String API_PATH = "https://nikaxen.000webhostapp.com/androidproject/";
    String URL="";

    private static final String PREFS_FILE = "Account";
    private static final String PREF_IDUSER = "ID_USER";
    SharedPreferences settings;

    public static ArrayAdapter<entityStatement> statementsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prep);

        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        int id_user = settings.getInt(PREF_IDUSER, 0);
        prep_id=id_user;

        if(getIntent().getSerializableExtra("link_to").equals("SelectedStatement")){

            entityStatement es = (entityStatement) getIntent().getSerializableExtra("the_statement");
            Intent intent = new Intent(this, PrepActivityStatementItem.class);
            intent.putExtra("StatementItem", (Serializable) es);
            finish();
            startActivity(intent);
        }else{
            ShowAllPrepStatementsList();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.prep_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_prep_statements:
                ShowAllPrepStatementsList();
                return true;
            case R.id.nav_prep_readyandclosed_statements:
                GoToPrepReadyAndClosedStatements();
                return true;
            case R.id.nav_profile:
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void GoToPrepReadyAndClosedStatements(){
        Intent intent = new Intent(this, PrepGraph.class);
        startActivity(intent);
    }

    public void ShowAllPrepStatementsList(){
        setTitle("Откр. ведомости");
        setContentView(R.layout.activity_prep);
        Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();
        FetchData fd = new FetchData();
        fd.setTask("PrepStatements_SelectAll");
        URL = API_PATH + "api_statement.php?action=select_prepstatements&id_user="+prep_id;
        fd.execute(URL);

        List<entityStatement> ent_es_List = fd.entityStatementsList;
        ListView PrepStatementsList = (ListView) findViewById(R.id.PrepStatementslist);
        statementsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ent_es_List);
        PrepStatementsList.setAdapter(statementsAdapter);

        PrepStatementsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                entityStatement es = (entityStatement) parent.getAdapter().getItem(position);
                Log.i("App", "item id = " + es.getId_statement());
                GoToStatementItem(es);
            }
        });
    }

    public void GoToStatementItem(entityStatement es){
        Intent intent = new Intent(this, PrepActivityStatementItem.class);
        intent.putExtra("StatementItem", (Serializable) es);
        startActivity(intent);
    }

}
