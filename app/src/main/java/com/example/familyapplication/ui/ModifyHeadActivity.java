package com.example.familyapplication.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.familyapplication.R;
import com.example.familyapplication.db.Users;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;

public class ModifyHeadActivity extends AppCompatActivity {

    private static final String TAG = "zzzzzjz---modify head";

    private Users user;


    private Button back,modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_head);

        user = UsersBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser());
        //TODO:改头像 用GridViewInScroll
    }
}
