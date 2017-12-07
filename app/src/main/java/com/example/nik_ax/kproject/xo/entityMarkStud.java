package com.example.nik_ax.kproject.xo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class entityMarkStud implements Serializable {
    public int id_mark;
    public int id_statement;
    public int id_user;
    public int mark;
    public static entitySubject subject = new entitySubject();
    public static List<MySpinnerData> msd_list_subject = new ArrayList<MySpinnerData>();

    public entitySubject getSubject(){
        return subject;
    }

    public void setId_mark(int id_mark){
        this.id_mark=id_mark;
    }
    public void setId_statement(int id_statement){
        this.id_statement=id_statement;
    }
    public void setId_user(int id_user){
        this.id_user=id_user;
    }
    public void setMark(int mark){
        this.mark = mark;
    }

    public int getId_mark(){
        return id_mark;
    }
    public int getId_statement(){
        return id_statement;
    }
    public int getId_user(){
        return id_user;
    }
    public int getMark(){
        return mark;
    }

    @Override
    public String toString() {
        String the_subject="Subject undefined";

        for(MySpinnerData msd : msd_list_subject){
            if(getId_statement()==msd.getKey()){
                the_subject=msd.getValue();
            }
        }

        return mark + " - " + the_subject;
    }

    public entityMarkStud(int id_mark, int id_statement, int id_user, int mark){
        this.id_mark = id_mark;
        this.id_statement = id_statement;
        this.id_user = id_user;
        this.mark = mark;
        FetchData fd = new FetchData();
        fd.setTask("SelectSubjectForStudMark");
        String API_PATH = "https://nikaxen.000webhostapp.com/androidproject/";
        String URL = API_PATH + "api_statement.php?action=select_subject_from_st&id_statement="+id_statement;
        fd.execute(URL);
    }

    public static void AddSubjectToList(int id_statement, String SubjectTitle){
        MySpinnerData msd = new MySpinnerData(id_statement,SubjectTitle);
        msd_list_subject.add(msd);
    }
}
