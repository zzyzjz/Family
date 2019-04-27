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
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class LoginActivity extends AppCompatActivity {
    protected static final String TAG = "zzzzzjz-Login";
    private EditText username,password;
    private Button login,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "loginActivity" );

        if(EMClient.getInstance().isLoggedInBefore()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            return;
        }
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.login_et_username);
        password = (EditText) findViewById(R.id.login_et_password);

        if(EMClient.getInstance().getCurrentUser() != null){
            //将用户名框内容设为最近登录
            Log.e(TAG, "CurrentUser -> "+EMClient.getInstance().getCurrentUser() );
            username.setText(EMClient.getInstance().getCurrentUser());
        }

        login = findViewById(R.id.login_btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(LoginActivity.this,R.string.User_name_cannot_be_empty,Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this,R.string.Password_cannot_be_empty,Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e(TAG, "login....." );
                EMClient.getInstance().login(name, pass, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e(TAG, "登陆成功···"+EMClient.getInstance().getCurrentUser());

                        // ** manually load all local groups and conversation
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);

                        finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e(TAG,"登录失败···"+EMClient.getInstance().getCurrentUser());
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
        register = findViewById(R.id.login_btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }
}
