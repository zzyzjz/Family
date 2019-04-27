package com.example.familyapplication;

import android.util.Log;

public class Contacted {
    //为了显示联系人列表，传给adapter的数据类型 为List<Contacted>
    private String name;
    private int image;

    public Contacted(String name,int id){
        Log.e("zjz Contacted", "name ---> "+name );
        this.name = name;
        this.image = id;
    }

    public String getName() {
        return name;
    }
    public int getImage(){
        return image;
    }
}
