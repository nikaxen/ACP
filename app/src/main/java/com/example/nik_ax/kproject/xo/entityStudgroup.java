package com.example.nik_ax.kproject.xo;

import java.io.Serializable;


public class entityStudgroup implements Serializable {
    public int sgid;
    public String sgname;
    public void setSgid(int sgid){
        this.sgid = sgid;
    }
    public void setSgname(String sgname){
        this.sgname = sgname;
    }
    public int getSgid(){
        return sgid;
    }
    public String getSgname(){
        return sgname;
    }
    public String toString(){
        return sgname;
    }

}
