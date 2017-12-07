package com.example.nik_ax.kproject.xo;


import java.io.Serializable;

public class entityUser implements Serializable {
    public int id_user;
    public String fio;
    public String rolename;
    public String email;
    public String password;

    public void setId_user(int id_user){
        this.id_user = id_user;
    }
    public void setFio(String fio){
        this.fio = fio;
    }
    public void setRolename(String rolename){
        this.rolename = rolename;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public int getId_user(){
        return id_user;
    }
    public String getFio(){
        return fio;
    }
    public String getRolename(){
        return rolename;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }

    public entityUser(int id_user, String fio, String rolename, String email, String password){
        this.id_user = id_user;
        this.fio = fio;
        this.rolename = rolename;
        this.email = email;
        this.password = password;
    }

    public entityUser(){

    }

    @Override
    public String toString() {
        return fio;
    }
}
