package com.example.familyapplication.db;

import android.util.Log;

import com.example.familyapplication.db.greendao.UsersDao;

import java.util.List;

public class UsersBaseDao {

    private static final String TAG = "zzzzzjz-UsersBaseDao_";


    public static void insert(Users users) {

        try {

            DataBase.getDaoSession().getUsersDao().insert(users);

            Log.i(TAG, "数据插入成功");

        } catch (Exception e) {

            e.printStackTrace();

            Log.i(TAG, "数据插入失败");
        }


    }

    public static void delete(Users users) {

        try {

            DataBase.getDaoSession().getUsersDao().delete(users);

            Log.i(TAG, "数据删除成功");

        } catch (Exception e) {

            e.printStackTrace();

            Log.i(TAG, "数据删除失败");
        }


    }
    public static void deleteAll(){
        try {
            DataBase.getDaoSession().getUsersDao().deleteAll();
            Log.e(TAG, "All delete");
        }catch (Exception e){
            e.printStackTrace();

            Log.e(TAG, "---------All delete------------数据删除失败");
        }

    }


    /**
     *
     * @param key
     */
    public static void deleteByKey(long key) {

        try {

            DataBase.getDaoSession().getUsersDao().deleteByKey(key);

            Log.i(TAG, "数据删除成功");

        } catch (Exception e) {

            e.printStackTrace();

            Log.i(TAG, "数据删除失败");
        }


    }

    public static void update(Users users) {

        try {

            DataBase.getDaoSession().getUsersDao().update(users);

            Log.i(TAG, "数据更新成功");

        } catch (Exception e) {

            e.printStackTrace();

            Log.i(TAG, "数据更新失败");
        }


    }

    public static void updateNickByUserId(String userId,String nick) {
        Users user = DataBase.getDaoSession().getUsersDao().queryBuilder()
                .where(UsersDao.Properties.UserId.eq(userId)).unique();
        try {
            user.setNickname(nick);

            DataBase.getDaoSession().getUsersDao().update(user);

            Log.i(TAG, "Nick更新成功");

        } catch (Exception e) {

            e.printStackTrace();

            Log.i(TAG, "Nick更新失败");
        }


    }

    public static void updateHeadByUserId(String userId,int head) {
        Users user = DataBase.getDaoSession().getUsersDao().queryBuilder()
                .where(UsersDao.Properties.UserId.eq(userId)).unique();
        try {
            user.setHead(head);

            DataBase.getDaoSession().getUsersDao().update(user);

            Log.i(TAG, "Nick更新成功");

        } catch (Exception e) {

            e.printStackTrace();

            Log.i(TAG, "Nick更新失败");
        }


    }


    public static List<Users> searchAll() {

        try {

            return DataBase.getDaoSession().getUsersDao().queryBuilder().list();

        } catch (Exception e) {

            e.printStackTrace();

            Log.e(TAG, "--------searchAll---------查询数据失败");
        }


        return null;
    }


    /**
     *
     * @param key
     * @return
     */
    public static Users searchByKey(long key) {

        try {

            return DataBase.getDaoSession().getUsersDao().load(key);

        } catch (Exception e) {

            e.printStackTrace();

            Log.i(TAG, "数据查询失败");
        }

        return null;
    }

    public static  Users searchByUserId(String userId){
        Users user;
        try{
            user = DataBase.getDaoSession().getUsersDao().queryBuilder()
                    .where(UsersDao.Properties.UserId.eq(userId)).unique();
            Log.e(TAG, "--------searchByUserId---------查询数据");
            return user;
        }catch (Exception e){
            Log.e(TAG, "--------searchByUserId---------数据查询失败");
        }
        return null;
    }
}
