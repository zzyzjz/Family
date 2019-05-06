package com.example.familyapplication.db;

import android.util.Log;

import com.example.familyapplication.db.greendao.ContactsDao;

import java.util.List;

public class ContactsBaseDao {

    private static String TAG = "zzzzzjz-ContactsBaseDao";

    public static void insert(Contacts contacts){
        Log.e(TAG, "12" );
        try{
            DataBase.getDaoSession().getContactsDao().insert(contacts);
            Log.e(TAG, "insert: 成功 "+contacts.getId()+"<><><><>"+
                    contacts.getUserId()+" ---> "+contacts.getContactedId() );

        }catch (Exception e){
            e.printStackTrace();

            Log.i(TAG, "数据插入失败");
        }

    }


    public static List<Contacts> searchAll(){

        List<Contacts> list;
        list = DataBase.getDaoSession().getContactsDao().queryBuilder().list();
        Log.e(TAG, "---------------searchAll-----------------数据查询");
        return list;


    }

    public static void deleteByUserIdAndContactedId(String userId,String contactedId){
        Contacts c = searchByUserIdAndContactedId(userId,contactedId);

        try {
            DataBase.getDaoSession().getContactsDao().deleteByKey(c.getId());
            Log.e(TAG, "delete ->"+c.getUserId()+"<><>"+c.getContactedId());
        }catch (Exception e){
            e.printStackTrace();

            Log.e(TAG, "数据删除失败");
        }
    }

    public static void deleteAll(){
        try {
            DataBase.getDaoSession().getContactsDao().deleteAll();
            Log.e(TAG, "All delete");
        }catch (Exception e){
            e.printStackTrace();

            Log.e(TAG, "数据删除失败");
        }
    }

    public static List<Contacts> searchByUserId(String userId){

        List<Contacts> list;
        list = DataBase.getDaoSession().getContactsDao().queryBuilder()
                .where(ContactsDao.Properties.UserId.eq(userId)).list();
        Log.e(TAG, "---------------searchByUserId-----------------数据查询");

        return list;

    }
    public static Contacts searchByUserIdAndContactedId(String userId,String contactedId){
        Contacts c ;
        try{
            c = DataBase.getDaoSession().getContactsDao().queryBuilder()
                    .where(ContactsDao.Properties.UserId.eq(userId))
                    .where(ContactsDao.Properties.ContactedId.eq(contactedId))
                    .unique();

            Log.e(TAG, "---------searchByUserIdAndContactedId-------数据查询");
            return c;

        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "--------searchByUserIdAndContactedId--------数据查询失败");
        }


        return null;
    }

    public static void update(Contacts contacts) {

        try {

            DataBase.getDaoSession().getContactsDao().update(contacts);

            Log.i(TAG, "Contacts更新成功 -----"+contacts.getUserId()+"<><><><>"+contacts.getContactedId());

        } catch (Exception e) {

            e.printStackTrace();

            Log.i(TAG, "contacts更新失败-----"+contacts.getUserId()+"<><><><>"+contacts.getContactedId());
        }


    }
}
