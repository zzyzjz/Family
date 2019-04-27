package com.example.familyapplication.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.familyapplication.R;
import com.example.familyapplication.db.DataBase;
import com.example.familyapplication.db.Users;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private String TAG = "zzzzzjz-register";

    private EditText username,password;
    private Button register,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "RegisterActivity" );

        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.register_et_username);
        password = (EditText) findViewById(R.id.register_et_password);

        register = findViewById(R.id.register_btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = username.getText().toString().trim();
                final String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(RegisterActivity.this,R.string.User_name_cannot_be_empty,Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(pass)){
                    Toast.makeText(RegisterActivity.this,R.string.Password_cannot_be_empty,Toast.LENGTH_SHORT).show();
                    return;
                }else if(UsersBaseDao.searchByUserId(name) != null){
                    //该用户名已注册
                    Toast.makeText(RegisterActivity.this,"该用户名已存在！",Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e(TAG, "register....." );

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        //注册失败会抛出HyphenateException
                        try {
                            EMClient.getInstance().createAccount(name, pass);//同步方法
                            Log.e(TAG, "注册成功···"+name );
                            Users u = new Users(null,name,pass,null,R.drawable.head);
                            UsersBaseDao.insert(u);
                            UserTable();
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            Log.e(TAG, "注册失败···"+name +e.toString());
                            Toast.makeText(RegisterActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }.start();
            }
        });

        back = findViewById(R.id.register_btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                finish();
            }
        });


    }
    public void UserTable(){
        List<Users> users = UsersBaseDao.searchAll();
        Log.e(TAG, "`````174" );
        for(Users use :users){
            Log.e(TAG, "id ---> "+use.getId() );
            Log.e(TAG, "userId ---> "+use.getUserId() );
            Log.e(TAG, "pass ---> "+use.getPassword() );
            Log.e(TAG, "nick ---> "+use.getNickname() );
            Log.e(TAG, "head ---> "+use.getHead() );
        }
    }
}
