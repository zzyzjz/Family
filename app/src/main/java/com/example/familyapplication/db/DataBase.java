package com.example.familyapplication.db;

import android.content.Context;

import com.example.familyapplication.db.greendao.DaoMaster;
import com.example.familyapplication.db.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;

public class DataBase {
    private static final String DB_NAME = "db_test";//数据库的名字
    private static DaoSession daoSession;

    public DataBase(){

    }

    public static void initDataBase(Context context){
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context,DB_NAME);
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession(){
        if (daoSession != null){
            return daoSession;
        }else {
            throw new IllegalStateException("DataSession not initialized");
        }
    }
}
