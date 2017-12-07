package com.example.nik_ax.kproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class RegActivity extends AppCompatActivity  implements View.OnClickListener {

    EditText etFio, etEmail, etPassword;
    Button btnReg;
    private String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Регистрация");
        setContentView(R.layout.activity_reg);

        btnReg = (Button) findViewById(R.id.btnReg);
        btnReg.setOnClickListener(this);

        etFio = (EditText) findViewById(R.id.etFio);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    @Override
    public void onClick(View v) {
        String str_etFio = etFio.getText().toString();
        str_etFio = str_etFio.replaceAll("\\s+","_");

        String str_etEmail = etEmail.getText().toString();
        String str_etPassword = etPassword.getText().toString();
        switch (v.getId()) {
            case R.id.btnReg:
                URL = "https://nikaxen.000webhostapp.com/androidproject/api_user.php?action=reg&fio="+ str_etFio +"&email="+ str_etEmail +"&password="+ str_etPassword +"";

                new FetchDataTask().execute(URL);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
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
                String got_response = "success";
                got_response = jsonChildNode.getString("response");

                Log.i("App", "SERVER RESPONSE: "+ got_response);
            }catch(Exception e){
                Log.i("App", "Error parsing data" +e.getMessage());
            }
        }
    }




    /* Intent Activities */
    public void gotoAuth(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
