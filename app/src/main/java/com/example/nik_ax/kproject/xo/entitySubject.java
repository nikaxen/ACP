package com.example.nik_ax.kproject.xo;


import java.io.Serializable;

public class entitySubject implements Serializable{
    public int id_subject;
    public String subject_title;

    public void setId_subject(int id_subject){
        this.id_subject = id_subject;
    }
    public void setSubject_title(String subject_title){
        this.subject_title = subject_title;
    }

    public int getId_subject() {
        return id_subject;
    }

    public String getSubject_title() {
        return subject_title;
    }

    @Override
    public String toString() {
        return subject_title;
    }
}
