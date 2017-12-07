package com.example.nik_ax.kproject.xo;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nik_ax.kproject.DekActivity;
import com.example.nik_ax.kproject.DekActivityStatementItem;
import com.example.nik_ax.kproject.DekActivityUserItem;
import com.example.nik_ax.kproject.PrepActivity;
import com.example.nik_ax.kproject.PrepActivityStatementItem;
import com.example.nik_ax.kproject.PrepGraph;
import com.example.nik_ax.kproject.PrepGraphItem;
import com.example.nik_ax.kproject.PrepMark;
import com.example.nik_ax.kproject.StudActivity;
import com.example.nik_ax.kproject.StudMark;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FetchData extends AsyncTask<String, Void, String> {

    public String Task;
    public List<MySpinnerData> spinnerList;

    public List<entityStatement> entityStatementsList;
    public List<entityMark> entityMarkList;
    public List<entityMarkStud> entityMarkStudList;
    public List<entityUser> entityUsersList;

    public entityUser entity_prep,entity_stud;
    public entityStudgroup entity_studgroup;
    public entitySubject entity_subject;
    public entityStatement entity_statement;


    public FetchData() {
        spinnerList = new ArrayList<MySpinnerData>();
        entityStatementsList = new ArrayList<entityStatement>();
        entityMarkList = new ArrayList<entityMark>();
        entityMarkStudList = new ArrayList<entityMarkStud>();
        entityUsersList = new ArrayList<entityUser>();
        entity_prep = new entityUser();
        entity_stud = new entityUser();
        entity_studgroup = new entityStudgroup();
        entity_subject = new entitySubject();
        entity_statement = new entityStatement();
    }

    public String getTask(){
        return Task;
    }
    public void setTask(String task){
        this.Task = task;
    }
    public List<MySpinnerData> getSpinnerList(){
        return spinnerList;
    }
    public void setSpinnerList(List<MySpinnerData> spinnerList){
        this.spinnerList = spinnerList;
    }



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
        if(Task=="Users_SelectAll") {
            DekActivity.usersAdapter.notifyDataSetChanged();
        }
        if(Task=="Subjects_SelectAll") {
            DekActivity.subjectAdapter.notifyDataSetChanged();
        }
        if(Task=="Preps_SelectAll") {
            DekActivity.prepAdapter.notifyDataSetChanged();
        }
        if(Task=="Studgroup_SelectAll") {
            DekActivity.studgroupAdapter.notifyDataSetChanged();
        }
        if(Task=="Statements_SelectAll") {
            DekActivity.statementsAdapter.notifyDataSetChanged();
        }
        if(Task=="PrepStatements_SelectAll") {
            PrepActivity.statementsAdapter.notifyDataSetChanged();
        }
        if(Task=="PrepStatements_SelectClosedAndReady") {
            PrepGraph.statementsAdapter.notifyDataSetChanged();
            Log.i("App","List Update");
        }
        if(Task=="SelectAllMarksInStatement") {
            PrepActivityStatementItem.marksAdapter.notifyDataSetChanged();
        }
        if(Task=="SelectSubjectForStudMark") {
            entityMarkStud.subject.setSubject_title(entity_subject.getSubject_title());
            entityMarkStud.AddSubjectToList(entity_subject.getId_subject(),entity_subject.getSubject_title());
            StudActivity.marksAdapter.notifyDataSetChanged();
        }
        if(Task=="GetMarksForStudent") {
            StudActivity.marksAdapter.notifyDataSetChanged();
        }
        if(Task=="SelectStatementForSTUDENT") {
            StudMark.statement_title.setText(entity_statement.getTitle());
            StudMark.esubj.setId_subject(entity_statement.getId_subject());
        }
        if(Task=="SelectAllMarksInStatementDek") {
            DekActivityStatementItem.marksAdapter.notifyDataSetChanged();
        }
        if(Task=="SelectAllMarksInStatementGraph") { // GRAPHICS UPDATE

            BarGraphSeries<DataPoint> series;
            List<BarGraphSeries<DataPoint>> seriesList = new ArrayList<>();

            for(entityMark em : entityMarkList){
                series = new BarGraphSeries<>(new DataPoint[]{
                        new DataPoint(em.getId_user(),em.getMark())
                });
                series.setTitle(String.valueOf(em.getId_user()));
                seriesList.add(series);
                Log.i("App", "Point[" + em.getId_user() + " ; " +em.getMark() + ", " + series.getTitle() +"] added");
            }


            for(BarGraphSeries<DataPoint> s : seriesList){
                Random rnd = new Random();
                s.setColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
                s.setSpacing(1);
                s.setAnimated(true);
                PrepGraphItem.graph.addSeries(s);
                Log.i("App", "Series " + s.getTitle() + " added to Graph");
            }


            PrepGraphItem.graph.getViewport().setXAxisBoundsManual(true);
            PrepGraphItem.graph.getViewport().setMinY(0);
            PrepGraphItem.graph.getViewport().setMaxY(5);
            PrepGraphItem.graph.getViewport().setMinX(0);
            PrepGraphItem.graph.getViewport().setMaxX(50);

            PrepGraphItem.graph.getLegendRenderer().setVisible(true);
            PrepGraphItem.graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            PrepGraphItem.marksAdapter.notifyDataSetChanged();
        }
        if(Task=="Select_prep") {
            DekActivityStatementItem.prep.setText(entity_prep.getFio());
        }
        if(Task=="Select_stud") {
            entityMark.stud.setFio(entity_stud.getFio());
            entityMark.AddStudentToList(entity_stud.getId_user(),entity_stud.getFio());
            PrepActivityStatementItem.marksAdapter.notifyDataSetChanged();
        }
        if(Task=="Select_studGraph") {
            entityMark.stud.setFio(entity_stud.getFio());
            entityMark.AddStudentToList(entity_stud.getId_user(),entity_stud.getFio());
            PrepGraphItem.marksAdapter.notifyDataSetChanged();
        }
        if(Task=="Select_studDEK") {
            entityMark.stud.setFio(entity_stud.getFio());
            entityMark.AddStudentToList(entity_stud.getId_user(),entity_stud.getFio());
            DekActivityStatementItem.marksAdapter.notifyDataSetChanged();
        }
        if(Task=="Select_subject") {
            DekActivityStatementItem.subject.setText(entity_subject.getSubject_title());
        }

        if(Task=="SelectSubjectForStudMarkEA") {
            StudMark.subject_title.setText(entity_subject.getSubject_title());
        }

        if(Task=="Select_studgroup") {
            DekActivityStatementItem.student_group.setText(entity_studgroup.getSgname());
        }
        if(Task=="Select_subject_prep") {
            PrepActivityStatementItem.subject.setText(entity_subject.getSubject_title());
        }
        if(Task=="Select_subject_prep_mark") {
            PrepMark.subject_title.setText(entity_subject.getSubject_title());
        }
        if(Task=="Select_studgroup_prep") {
            PrepActivityStatementItem.student_group.setText(entity_studgroup.getSgname());
        }
        if(Task=="Select_User_Studgroup") {
            if(entity_studgroup.getSgname()==null){
                DekActivityUserItem.tv_st2.setText("не установл.");
            }else{
                DekActivityUserItem.tv_st2.setText(entity_studgroup.getSgname());
            }

        }
        if(Task=="StudGroupUserSelectAll") {
            DekActivityUserItem.studgroupAdapter.notifyDataSetChanged();
        }
        if(Task=="StudsInStudGroup_SelectAll") {
            PrepActivityStatementItem.studsinstudgroupAdapter.notifyDataSetChanged();
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
            JSONArray jsonArray = new JSONArray(data);

            int jsonArrLength = jsonArray.length();

            if(Task=="SelectAllMarksInStatementGraph"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);

                    String idMark = jsonChildNode.getString("id_mark");
                    String idStatement = jsonChildNode.getString("id_statement");
                    String idUser = jsonChildNode.getString("id_user");
                    String mark = jsonChildNode.getString("mark");


                    entityMark es_data = new entityMark(Integer.valueOf(idMark),Integer.valueOf(idStatement),Integer.valueOf(idUser),Integer.valueOf(mark), Task);
                    entityMarkList.add(es_data);
                }

            }




            if(Task=="Users_SelectAll"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);

                    String idUser = jsonChildNode.getString("id_user");
                    String fio = jsonChildNode.getString("fio");
                    String rolename = jsonChildNode.getString("rolename");
                    String password = jsonChildNode.getString("password");
                    String email = jsonChildNode.getString("email");



                    entityUser us_data = new entityUser(Integer.valueOf(idUser),fio,rolename,email,password);
                    entityUsersList.add(us_data);
                }

            }


            if(Task=="Statements_SelectAll" | Task=="PrepStatements_SelectAll" | Task=="PrepStatements_SelectClosedAndReady"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);

                    String idStatement = jsonChildNode.getString("id_statement");
                    String idSubject = jsonChildNode.getString("id_subject");
                    String idUser = jsonChildNode.getString("id_user");
                    String status = jsonChildNode.getString("status");
                    String title = jsonChildNode.getString("title");
                    String grid = jsonChildNode.getString("grid");

                    entityStatement es_data = new entityStatement(Integer.valueOf(idStatement),Integer.valueOf(idSubject),Integer.valueOf(idUser),status,title,Integer.valueOf(grid));
                    entityStatementsList.add(es_data);
                }

            }



            if(Task=="SelectStatementForSTUDENT"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(0);

                    String idStatement = jsonChildNode.getString("id_statement");
                    String idSubject = jsonChildNode.getString("id_subject");
                    String idUser = jsonChildNode.getString("id_user");
                    String status = jsonChildNode.getString("status");
                    String title = jsonChildNode.getString("title");
                    String grid = jsonChildNode.getString("grid");

                    entity_statement.setId_statement(Integer.valueOf(idStatement));
                    entity_statement.setId_subject(Integer.valueOf(idSubject));
                    entity_statement.setId_user(Integer.valueOf(idUser));
                    entity_statement.setStatus(status);
                    entity_statement.setTitle(title);
                    entity_statement.setGrid(Integer.valueOf(grid));
                }

            }


            if(Task=="GetMarksForStudent"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);

                    String idMark = jsonChildNode.getString("id_mark");
                    String idStatement = jsonChildNode.getString("id_statement");
                    String idUser = jsonChildNode.getString("id_user");
                    String mark = jsonChildNode.getString("mark");


                    entityMarkStud es_data = new entityMarkStud(Integer.valueOf(idMark),Integer.valueOf(idStatement),Integer.valueOf(idUser),Integer.valueOf(mark));
                    entityMarkStudList.add(es_data);
                }

            }

            if(Task=="SelectAllMarksInStatement" | Task=="SelectAllMarksInStatementDek"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);

                    String idMark = jsonChildNode.getString("id_mark");
                    String idStatement = jsonChildNode.getString("id_statement");
                    String idUser = jsonChildNode.getString("id_user");
                    String mark = jsonChildNode.getString("mark");


                    entityMark es_data = new entityMark(Integer.valueOf(idMark),Integer.valueOf(idStatement),Integer.valueOf(idUser),Integer.valueOf(mark), Task);
                    entityMarkList.add(es_data);
                }

            }


            if(Task=="Subjects_SelectAll"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
                    String idSubject = jsonChildNode.getString("id_subject");
                    String SubjectTitle = jsonChildNode.getString("subject_title");

                    MySpinnerData sp_data = new MySpinnerData(Integer.valueOf(idSubject),SubjectTitle);
                    spinnerList.add(sp_data);

                }

            }

            if(Task=="Preps_SelectAll"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
                    String key = jsonChildNode.getString("id_user");
                    String value = jsonChildNode.getString("fio");

                    MySpinnerData sp_data = new MySpinnerData(Integer.valueOf(key),value);
                    spinnerList.add(sp_data);

                }

            }
            if(Task=="Studgroup_SelectAll"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
                    String key = jsonChildNode.getString("sgid");
                    String value = jsonChildNode.getString("sgname");

                    MySpinnerData sp_data = new MySpinnerData(Integer.valueOf(key),value);
                    spinnerList.add(sp_data);

                }

            }



            if(Task=="Select_prep"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(0);
                    String idUser = jsonChildNode.getString("id_user");
                    String fio = jsonChildNode.getString("fio");
                    String email = jsonChildNode.getString("email");
                    String rolename = jsonChildNode.getString("rolename");
                    String password = jsonChildNode.getString("password");
                    entity_prep.setId_user(Integer.valueOf(idUser));
                    entity_prep.setFio(fio);
                    entity_prep.setEmail(email);
                    entity_prep.setRolename(rolename);
                    entity_prep.setPassword(password);

                }
            }


            if(Task=="SelectSubjectForStudMark"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(0);
                    String idStatement = jsonChildNode.getString("id_statement");
                    String subjectTitle = jsonChildNode.getString("subject_title");

                    entity_subject.setId_subject(Integer.valueOf(idStatement));
                    entity_subject.setSubject_title(subjectTitle);
                }
            }


            if(Task=="Select_stud" | Task=="Select_studDEK" | Task=="Select_studGraph"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(0);
                    String idUser = jsonChildNode.getString("id_user");
                    String fio = jsonChildNode.getString("fio");
                    String email = jsonChildNode.getString("email");
                    String rolename = jsonChildNode.getString("rolename");
                    String password = jsonChildNode.getString("password");
                    entity_stud.setId_user(Integer.valueOf(idUser));
                    entity_stud.setFio(fio);
                    entity_stud.setEmail(email);
                    entity_stud.setRolename(rolename);
                    entity_stud.setPassword(password);

                }

            }


            if(Task=="Select_subject" | Task=="Select_subject_prep" | Task=="Select_subject_prep_mark" | Task=="SelectSubjectForStudMarkEA"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(0);
                    String idSubject = jsonChildNode.getString("id_subject");
                    String subjectTitle = jsonChildNode.getString("subject_title");
                    entity_subject.setId_subject(Integer.valueOf(idSubject));
                    entity_subject.setSubject_title(subjectTitle);
                }

            }

            if(Task=="StudGroupUserSelectAll"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
                    String sgId = jsonChildNode.getString("sgid");
                    String sgName = jsonChildNode.getString("sgname");
                    MySpinnerData sp_data = new MySpinnerData(Integer.valueOf(sgId),sgName);
                    spinnerList.add(sp_data);
                }

            }

            if(Task=="StudsInStudGroup_SelectAll"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
                    String idUser = jsonChildNode.getString("id_user");
                    String UserFio = jsonChildNode.getString("fio");
                    MySpinnerData sp_data = new MySpinnerData(Integer.valueOf(idUser),UserFio);
                    spinnerList.add(sp_data);
                }

            }

            if(Task=="Select_studgroup" | Task=="Select_studgroup_prep"| Task=="Select_User_Studgroup"){
                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(0);
                    String sgId = jsonChildNode.getString("sgid");
                    String sgName = jsonChildNode.getString("sgname");
                    entity_studgroup.setSgid(Integer.valueOf(sgId));
                    entity_studgroup.setSgname(sgName);
                }

            }



            if(Task=="CreateNewStatement"){
                JSONArray jsonMainNode = new JSONArray(data);
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                String got_response = "success";
                got_response = jsonChildNode.getString("response");

                if(got_response!="fail") {

                    Log.i("App", "Statement has created!");
                }

            }



            if(Task=="ChangeStatementStatus"){
                JSONArray jsonMainNode = new JSONArray(data);
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                String got_response = "success";
                got_response = jsonChildNode.getString("response");

                if(got_response!="fail") {

                    Log.i("App", "Statement status changed successfully");
                }

            }

            if(Task=="ChangeUserStudGroup"){
                JSONArray jsonMainNode = new JSONArray(data);
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                String got_response = "success";
                got_response = jsonChildNode.getString("response");

                if(got_response!="fail") {

                    Log.i("App", "User studgroup changed successfully");
                }

            }

            if(Task=="AddMarkToStatement"){
                JSONArray jsonMainNode = new JSONArray(data);
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                String got_response = "success";
                got_response = jsonChildNode.getString("response");

                if(got_response!="fail") {

                    Log.i("App", "Mark added successfully");
                }

            }

            if(Task=="ChangeUserRole"){
                JSONArray jsonMainNode = new JSONArray(data);
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                String got_response = "success";
                got_response = jsonChildNode.getString("response");

                if(got_response!="fail") {

                    Log.i("App", "User rolename changed successfully");
                }

            }

        }catch(Exception e){
            Log.i("App", "Error parsing data " +e.getMessage());
        }
    }
}