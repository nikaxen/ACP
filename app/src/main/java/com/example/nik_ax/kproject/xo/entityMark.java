package com.example.nik_ax.kproject.xo;


import java.util.ArrayList;
import java.util.List;

public class entityMark {
    public int id_mark;
    public int id_statement;
    public int id_user;
    public int mark;
    public String fdMode;
    public static List<MySpinnerData> msd_list_stud = new ArrayList<MySpinnerData>();
    public static entityUser stud = new entityUser();



    public void setFdMode(String fdMode){
        this.fdMode = fdMode;
    }
    public String getFdMode(){
        return  fdMode;
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
        String the_fio="Student undefined";
        int stid=-1;

        for(MySpinnerData msd : msd_list_stud){
            if(getId_user()==msd.getKey()){
                the_fio=msd.getValue();
                stid=msd.getKey();
            }
        }

        return mark + " - " + the_fio + " ("+ stid +")";
    }

    public entityMark(int id_mark, int id_statement, int id_user, int mark, String fdMode){
        this.id_mark = id_mark;
        this.id_statement = id_statement;
        this.id_user = id_user;
        this.mark = mark;
        FetchData fd = new FetchData();
        if(fdMode=="SelectAllMarksInStatement"){
            fd.setTask("Select_stud");
        }
        if(fdMode=="SelectAllMarksInStatementDek"){
            fd.setTask("Select_studDEK");
        }
        if(fdMode=="SelectAllMarksInStatementGraph"){
            fd.setTask("Select_studGraph");
        }
        String API_PATH = "https://nikaxen.000webhostapp.com/androidproject/";
        String URL = API_PATH + "api_user.php?action=select&id_user="+id_user;
        fd.execute(URL);
    }

    public static void AddStudentToList(int id_user, String fio){
        MySpinnerData msd = new MySpinnerData(id_user,fio);
        msd_list_stud.add(msd);
    }

}
