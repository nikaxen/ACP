package com.example.nik_ax.kproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nik_ax.kproject.xo.FetchData;
import com.example.nik_ax.kproject.xo.entityStatement;

import java.io.Serializable;
import java.util.List;

public class PrepGraph extends AppCompatActivity {

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


        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        int id_user = settings.getInt(PREF_IDUSER, 0);
        prep_id=id_user;

        ShowClosedAndReadyList();
    }

    public void ShowClosedAndReadyList(){
        setTitle("Закрытые ведомости");
        setContentView(R.layout.activity_prep_graph);
        Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();
        FetchData fd = new FetchData();
        fd.setTask("PrepStatements_SelectClosedAndReady");
        URL = API_PATH + "api_statement.php?action=select_prep_rc_statements&id_user="+prep_id;
        Log.i("App", URL);
        fd.execute(URL);
        List<entityStatement> ent_es_List = fd.entityStatementsList;
        ListView PrepStatementsList = (ListView) findViewById(R.id.LVPrepCloseAndReady);
        statementsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ent_es_List);
        PrepStatementsList.setAdapter(statementsAdapter);

        PrepStatementsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                entityStatement es = (entityStatement) parent.getAdapter().getItem(position);
                Log.i("App", "item id = " + es.getId_statement());
                GoToStatementGraphItem(es);
            }
        });
    }

    public void GoToStatementGraphItem(entityStatement es){
        Intent intent = new Intent(this, PrepGraphItem.class);
        intent.putExtra("StatementItem", (Serializable) es);
        startActivity(intent);
    }


}
