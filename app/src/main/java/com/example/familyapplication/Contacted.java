package com.example.familyapplication;

import android.util.Log;

public class Contacted {
    //为了显示联系人列表，传给adapter的数据类型 为List<Contacted>
    private String contactedId;
    private String name;
    private int image;

    public Contacted(String contactedId,String name,int id){
        Log.e("zjz Contacted", "name ---> "+name );
        this.contactedId = contactedId;
        this.name = name;
        this.image = id;
    }

    public String getContactedId() {
        return contactedId;
    }
    public String getName() {
        return name;
    }
    public int getImage(){
        return image;
    }
}
