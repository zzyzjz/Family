package com.example.familyapplication;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.familyapplication.ui.GroupDetailActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

public class ModifyGroupNameActivity extends AppCompatActivity {
    private static final String TAG="zjz--modify group name";

    private EditText name;
    private ImageView back;
    private Button modify;

    private String groupId;
    private EMGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group_name);

        groupId = getIntent().getStringExtra("groupId");
        group = EMClient.getInstance().groupManager().getGroup(groupId);

        name = findViewById(R.id.modify_group_name_et);
        name.setText(group.getGroupName());

        back = findViewById(R.id.modify_group_name_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        ModifyGroupNameActivity.this, GroupDetailActivity.class));
                finish();
            }
        });

        modify = findViewById(R.id.modify_group_name_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EMClient.getInstance().groupManager().changeGroupName(
                            groupId,name.getText().toString().trim());
                } catch (HyphenateException e) {
                    e.printStackTrace();

                }
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            EMClient.getInstance().groupManager().changeGroupName(
                                    groupId,name.getText().toString().trim());
                            Looper.prepare();
                            Toast.makeText(ModifyGroupNameActivity.this,
                                    "群名更新成功！",Toast.LENGTH_SHORT).show();
                            Looper.loop();

                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            Looper.prepare();
                            Toast.makeText(ModifyGroupNameActivity.this,
                                    "群名更新失败！",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            Log.e(TAG, "群名更新失败 -> e:"+e.toString() );
                            return;
                        }
                    }
                }.start();
            }
        });
    }
}
