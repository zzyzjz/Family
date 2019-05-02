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
import com.example.familyapplication.db.Users;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;

import java.util.List;

public class ModifyNickActivity extends AppCompatActivity {

    private static final String TAG = "zzzzzjz---modify nick";

    private Users user;

    private EditText etNick;
    private Button back,modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nick);

        user = UsersBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser());

        etNick = findViewById(R.id.modify_nick_et_nick);
        if(!TextUtils.isEmpty(user.getNickname())){
            etNick.setText(user.getNickname());
        }


        modify = findViewById(R.id.modify_nick_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nick = etNick.getText().toString().trim();

                if(TextUtils.isEmpty(nick)){
                    Toast.makeText(ModifyNickActivity.this,"昵称不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                user.setNickname(nick);
                UsersBaseDao.update(user);
                Log.e(TAG, "修改昵称成功------"+user.getUserId()+"---->"+nick );
                Toast.makeText(ModifyNickActivity.this,"昵称修改成功！",Toast.LENGTH_SHORT).show();


                Log.e(TAG, "---------------------65" );
                List<Users> users = UsersBaseDao.searchAll();
                Log.e(TAG, "-----------------206" );
                for(Users use :users){
                    Log.e(TAG, "id ---> "+use.getId() );
                    Log.e(TAG, "userId ---> "+use.getUserId() );
                    Log.e(TAG, "pass ---> "+use.getPassword() );
                    Log.e(TAG, "nick ---> "+use.getNickname() );
                    Log.e(TAG, "head ---> "+use.getHead() );
                }
                Log.e(TAG, "---------------------75" );

                return;
            }
        });

    }
}
