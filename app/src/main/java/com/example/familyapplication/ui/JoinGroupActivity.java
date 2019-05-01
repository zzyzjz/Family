package com.example.familyapplication.ui;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.familyapplication.R;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class JoinGroupActivity extends AppCompatActivity {
    private static final String TAG = "zzzzzjz--join";

    private EditText ETGroupId;
    private Button join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        ETGroupId = findViewById(R.id.join_group_et_id);

        join = findViewById(R.id.join_group_btn_join);
        Log.e(TAG, " join is :"+(join != null) );
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String groupId = ETGroupId.getText().toString().trim();
                Log.e(TAG, "---36---groupId "+groupId );
                if(TextUtils.isEmpty(groupId)){
                    Log.e(TAG, "---38---groupId is empty " );
                    Toast.makeText(JoinGroupActivity.this,"群id不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                            //加群
                            EMClient.getInstance().groupManager().joinGroup(groupId);
                            Looper.prepare();
                            Toast.makeText(JoinGroupActivity.this, "加入成功！", Toast.LENGTH_SHORT).show();
                            Looper.loop();

                            Log.e(TAG, "---48---groupId "+groupId );

                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            if(e.getErrorCode() == EMError.GROUP_INVALID_ID){
                                Looper.prepare();
                                Toast.makeText(JoinGroupActivity.this, "此群不存在！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }else{
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), "加入群失败！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }
                }.start();
            }
        });
    }

    public void back(View view) {
        finish();
    }

}

