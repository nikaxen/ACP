package com.example.nik_ax.kproject.xo;


import java.io.Serializable;

public class entityStatement implements Serializable{
    public int id_statement;
    public int id_subject;
    public int id_user;
    public String status;
    public String title;
    public int grid;

    public entityStatement(int id_statement, int id_subject, int id_user, String status, String title, int grid){
        this.id_statement = id_statement;
        this.id_subject = id_subject;
        this.id_user = id_user;
        this.status = status;
        this.title = title;
        this.grid = grid;
    }
    public entityStatement(){

    }

    public void setId_statement(int id_statement){
        this.id_statement = id_statement;
    }
    public void setId_subject(int id_subject){
        this.id_subject = id_subject;
    }
    public void setId_user(int id_user){
        this.id_user = id_user;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setGrid(int grid){
        this.grid = grid;
    }

    public int getId_statement(){
        return id_statement;
    }

    public int getId_subject(){
        return id_subject;
    }
    public int getId_user(){
        return id_user;
    }
    public String getStatus(){
        return status;
    }
    public String getTitle(){
        return title;
    }
    public int getGrid(){
        return grid;
    }
    public String toString(){
        return title;
    }
}
