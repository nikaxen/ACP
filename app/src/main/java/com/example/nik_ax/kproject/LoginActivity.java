package com.example.nik_ax.kproject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

    EditText etEmail, etPassword;
    Button btnAuth;
    private String URL;

    private boolean LoginError = false;



    private static final String PREFS_FILE = "Account";
    private static final String PREF_IDUSER = "ID_USER";
    private static final String PREF_FIO = "FIO";
    private static final String PREF_ROLENAME = "ROLENAME";
    private static final String PREF_EMAIL = "EMAIL";
    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Войти в аккаунт");
        setContentView(R.layout.activity_login);



        btnAuth = (Button) findViewById(R.id.btnAuth);
        btnAuth.setOnClickListener(this);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
    }


    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues();

        String str_etEmail = etEmail.getText().toString();
        String str_etPassword = etPassword.getText().toString();
        switch (v.getId()) {
            case R.id.btnAuth:
                URL = "https://nikaxen.000webhostapp.com/androidproject/api_user.php?action=auth&email="+ str_etEmail +"&password="+ str_etPassword +"";
                Toast.makeText(getApplicationContext(), "CONNECTING TO SERVER", Toast.LENGTH_SHORT).show();
                new FetchDataTask().execute(URL);
                break;
        }
    }


    private class FetchDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            InputStream inputStream = null;
            String result= null;
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);

            try {

                HttpResponse response = client.execute(httpGet);
                inputStream = response.getEntity().getContent();

                // convert inputstream to string
                if(inputStream != null){
                    result = convertInputStreamToString(inputStream);
                    Log.i("App", "Data received:" +result);

                }
                else
                    result = "Failed to fetch data";

                return result;

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String dataFetched) {
            //parse the JSON data and then display

            parseJSON(dataFetched);
            if(LoginError==false){
                OnGoToMain();
            }else{
                NotifyLoginError();
            }

        }


        private String convertInputStreamToString(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

        private void parseJSON(String data){

            try{
                JSONArray jsonMainNode = new JSONArray(data);
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                int id_user=-1;
                String fio="undefined";
                String rolename="undefined";
                String user_email="undefined";

                    id_user = jsonChildNode.getInt("id_user");
                    fio = jsonChildNode.getString("fio");
                    rolename = jsonChildNode.getString("rolename");
                    user_email = jsonChildNode.getString("email");

                if(id_user==-1){
                    LoginError=true;
                }else{
                    Log.i("App", "Login got success");
                    SharedPreferences.Editor prefEditor = settings.edit();
                    prefEditor.putInt(PREF_IDUSER, id_user);
                    prefEditor.putString(PREF_FIO, fio);
                    prefEditor.putString(PREF_ROLENAME, rolename);
                    prefEditor.putString(PREF_EMAIL, user_email);
                    prefEditor.apply();
                }



                Log.i("App", "USER DATA: "+ id_user + " "+ fio +" " + rolename + " " + user_email);
            }catch(Exception e){
                Log.i("App", "Error parsing data" +e.getMessage());
                LoginError=true;
            }
        }
    }


    public void gotoReg(View view){
        Intent intent = new Intent(this, RegActivity.class);
        startActivity(intent);
    }


    public void OnGoToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void NotifyLoginError(){
        Toast.makeText(getApplicationContext(), "Неправильные данные для входа в аккаунт.", Toast.LENGTH_SHORT).show();
        LoginError=false;
    }
}
